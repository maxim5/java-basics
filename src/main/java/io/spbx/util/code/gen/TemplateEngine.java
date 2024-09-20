package io.spbx.util.code.gen;

import com.google.common.collect.ImmutableMap;
import com.google.common.flogger.FluentLogger;
import io.spbx.util.base.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.code.gen.CompiledTemplate.Block;
import io.spbx.util.code.gen.CompiledTemplate.CompiledDirective;
import io.spbx.util.code.gen.CompiledTemplate.DirectiveBlock;
import io.spbx.util.code.gen.CompiledTemplate.LiteralBlock;
import io.spbx.util.collect.BasicIterables;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.collect.Streamer;
import io.spbx.util.func.Functions;
import io.spbx.util.io.BasicFiles;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;

import static io.spbx.util.base.BasicExceptions.newIllegalStateException;
import static io.spbx.util.base.BasicExceptions.newInternalError;
import static io.spbx.util.code.gen.SkipTemplateException.newSkipTemplateException;

public class TemplateEngine {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Path sourceRoot;
    private final Path destRoot;
    private final Marking marking;
    private final Map<TemplateId, CompiledTemplate> cache = BasicMaps.newConcurrentMap();

    public TemplateEngine(@NotNull Path sourceRoot, @NotNull Path destRoot) {
        this.sourceRoot = sourceRoot;
        this.destRoot = destRoot;
        this.marking = JavaMarking.JAVA_MARK;  // right now it's the only one
    }

    public @NotNull List<Path> render(@NotNull Path path, @NotNull Iterable<TemplateVars> varSet) {
        return renderAll(toTemplateId(path), varSet);
    }

    public @NotNull List<Path> render(@NotNull File file, @NotNull Iterable<TemplateVars> varSet) {
        return renderAll(toTemplateId(file), varSet);
    }

    public @NotNull Path render(@NotNull Path path, @NotNull TemplateVars vars) {
        return renderToDest(toTemplateId(path), vars);
    }

    public @NotNull Path render(@NotNull File file, @NotNull TemplateVars vars) {
        return renderToDest(toTemplateId(file), vars);
    }

    public @NotNull String renderToString(@NotNull Path path, @NotNull TemplateVars vars) {
        return renderToLines(toTemplateId(path), vars).joinLines();
    }

    public @NotNull String renderToString(@NotNull File file, @NotNull TemplateVars vars) {
        return renderToLines(toTemplateId(file), vars).joinLines();
    }

    private @NotNull List<Path> renderAll(TemplateId templateId, @NotNull Iterable<TemplateVars> varsList) {
        return Streamer.of(varsList).mapIfNonNull(vars -> {
            try {
                return renderToDest(templateId, vars);
            } catch (SkipTemplateException e) {
                log.at(Level.FINE).log("Skip template %s for vars: %s", templateId.relativePath(), vars);
                return null;
            }
        }).toArrayList();
    }

    private @NotNull Path renderToDest(@NotNull TemplateId templateId, @NotNull TemplateVars vars) {
        CodeBuilder rendered = renderToLines(templateId, vars);
        Path dest = destRoot.resolve(vars.vars().interpolate(templateId.relativePath().toString()));
        BasicFiles.createDirs(dest.getParent());
        BasicFiles.writeAllLines(dest, rendered);
        return dest;
    }

    private @NotNull CodeBuilder renderToLines(@NotNull TemplateId templateId, @NotNull TemplateVars vars) {
        Map<TemplateId, CompiledTemplate> map = compileAllWithRefs(List.of(templateId));
        CompileContext compiled = CompileContext.of(map);
        RenderContext context = RenderContext.create(compiled, vars);
        renderTemplate(templateId, context);
        return context.builder().mapToNew(Functions.chain(vars.vars()::interpolate, String::stripTrailing));
    }

    private void renderTemplate(@NotNull TemplateId templateId, @NotNull RenderContext ctx) {
        CompiledTemplate template = ctx.compiled().getOrDie(templateId);
        renderTemplate(template, templateId, ctx);
    }

    private void renderTemplate(@NotNull CompiledTemplate template, @NotNull TemplateId id, @NotNull RenderContext ctx) {
        log.at(Level.INFO).log("Render template: %s", id);
        log.at(Level.FINE).log("Vars: %s", ctx.vars());
        template.blocks().forEach(blk -> renderBlock(blk, ctx));
    }

    private void renderBlocks(@NotNull List<Block> blocks, @NotNull RenderContext ctx) {
        blocks.forEach(blk -> renderBlock(blk, ctx));
    }

    private void renderBlock(@NotNull Block block, @NotNull RenderContext ctx) {
        switch (block) {
            case LiteralBlock literalBlock -> {
                log.at(Level.FINER).log("Render literal block: %s", literalBlock.literals().size());
                ctx.builder().appendLines(literalBlock.literals());
            }
            case DirectiveBlock directiveBlock -> {
                log.at(Level.FINE).log("Render directive block: %s", directiveBlock.directive());
                CompiledDirective directive = directiveBlock.directive();
                List<Block> innerBlocks = directiveBlock.inner();
                switch (directive.predefined()) {
                    case IMPORT -> {
                        String reference = directive.importReference();
                        TemplateId templateId = toTemplateId(reference);
                        CompiledTemplate template = ctx.compiled().getOrDie(templateId);
                        RenderContext importContext = ctx.copyForImport(templateId);
                        Optional.ofNullable(directive.importBlockName())
                            .map(template::findBlockByNameOrNull)
                            .ifPresentOrElse(blk -> renderBlock(blk, importContext),
                                             () -> renderTemplate(template, templateId, importContext));
                        ctx.builder().removeLastIfBlank().appendLines(importContext.builder());
                    }
                    case REMOVE ->
                        ctx.builder().removeLast();
                    case IF -> {
                        boolean eval = directive.evalCondition(ctx.vars().vars());
                        log.at(Level.FINER).log("If condition evaluated=%s from: %s", eval, ctx.vars().vars());
                        if (eval) {
                            renderBlocks(innerBlocks, ctx);
                        } else {
                            ctx.builder().removeLastIfBlank();
                        }
                    }
                    case ELSE -> {
                        boolean eval = directive.evalCondition(ctx.vars().vars());
                        log.at(Level.FINER).log("Else condition evaluated=%s from: %s", eval, ctx.vars().vars());
                        if (eval) {
                            ctx.builder().removeLastIfBlank();
                        } else {
                            renderBlocks(innerBlocks, ctx);
                        }
                    }
                    case PLACEHOLDER -> {
                        ctx.builder().removeLastIfBlank();
                        String multilineValue = ctx.vars().vars().get(directive.placeholder());
                        IllegalStateExceptions.assureNonNull(multilineValue, "Unresolved placeholder:", directive);
                        ctx.builder().appendMultiline(multilineValue);
                    }
                    case ASSUME -> {
                        ctx.builder().removeLastIfBlank();
                        boolean eval = directive.evalCondition(ctx.vars().vars());
                        if (!eval) {
                            throw newSkipTemplateException("Assumption not met:", directive);
                        }
                    }
                    case ASSERT -> {
                        ctx.builder().removeLastIfBlank();
                        assert directive.evalCondition(ctx.vars().vars()) : "Assertion failed: " + directive;
                    }
                    case WITH -> {
                        Map<String, String> attrs = directive.namedAttrs();
                        RenderContext withContext = ctx.copyForWith(attrs);
                        renderBlocks(innerBlocks, withContext);
                        ctx.builder().appendLines(withContext.builder().mapToNew(withContext.vars().vars()::interpolate));
                    }
                    case COMMENT ->
                        ctx.builder().removeLastIfBlank();
                    case EOD_OF_TEMPLATE ->
                        // Do not add anything, only make sure nothing is appended after that
                        ctx.builder().seal();
                    case NONE ->
                        renderBlocks(innerBlocks, ctx);
                    default ->
                        throw newInternalError("Unrecognized directive:", directive);
                }
            }
            default -> throw newIllegalStateException("Unrecognized block:", block);
        }
    }

    private record RenderContext(@NotNull CompileContext compiled,
                                 @NotNull TemplateVars vars,
                                 @NotNull CodeBuilder builder) {
        public static @NotNull RenderContext create(@NotNull CompileContext compile, @NotNull TemplateVars vars) {
            return new RenderContext(compile, vars, CodeBuilder.allocate(256));
        }

        public @NotNull RenderContext copyForImport(@NotNull TemplateId templateId) {
            Variables importVars = vars.contextVars().get(templateId.id());
            if (importVars == null) {
                return RenderContext.create(compiled, vars);
            }
            return RenderContext.create(compiled, vars.withVarsOverwrittenBy(importVars));
        }

        public @NotNull RenderContext copyForWith(@NotNull Map<String, String> attrs) {
            Variables withVars = Variables.of(attrs);
            return RenderContext.create(compiled, vars.withVarsOverwrittenBy(withVars));
        }
    }

    private @NotNull Map<TemplateId, CompiledTemplate> compileAllWithRefs(@NotNull Iterable<TemplateId> ids) {
        Map<TemplateId, CompiledTemplate> result = BasicMaps.newOrderedMap();
        Queue<TemplateId> queue = new ArrayDeque<>(BasicIterables.asCollection(ids));
        while (!queue.isEmpty()) {
            TemplateId templateId = queue.poll();
            if (result.containsKey(templateId)) {
                continue;
            }
            CompiledTemplate compiled = cache.computeIfAbsent(templateId, this::compile);
            result.put(templateId, compiled);
            compiled.forEachReference(ref -> queue.add(toTemplateId(ref)));
        }
        return result;
    }

    private @NotNull CompiledTemplate compile(@NotNull TemplateId templateId) {
        return TemplateCompiler.compile(templateId.readLines().stream(), marking);
    }

    private record CompileContext(@NotNull Map<TemplateId, CompiledTemplate> map) {
        public static @NotNull CompileContext of(@NotNull Map<TemplateId, CompiledTemplate> map) {
            return new CompileContext(ImmutableMap.copyOf(map));
        }

        public @NotNull CompiledTemplate getOrDie(@NotNull TemplateId templateId) {
            CompiledTemplate compiledTemplate = map.get(templateId);
            assert compiledTemplate != null : "Template not found: " + templateId;
            return compiledTemplate;
        }
    }

    private @NotNull TemplateId toTemplateId(@NotNull Path path) {
        return path.isAbsolute() ? absolutePathToId(path) : relativePathToId(path);
    }

    private @NotNull TemplateId toTemplateId(@NotNull File file) {
        return toTemplateId(file.toPath());
    }

    private @NotNull TemplateId toTemplateId(@NotNull String id) {
        return relativePathToId(Path.of(id));
    }

    private @NotNull TemplateId relativePathToId(@NotNull Path path) {
        assert !path.isAbsolute() : "Expected a relative path: " + path;
        return new TemplateId(sourceRoot.resolve(path));
    }

    private @NotNull TemplateId absolutePathToId(@NotNull Path path) {
        assert path.isAbsolute() : "Expected an absolute path: " + path;
        assert BasicFiles.isParentOf(sourceRoot, path) : "Must be in a source root: " + sourceRoot;
        return new TemplateId(path);
    }

    private class TemplateId {
        private final @NotNull Path path;

        private TemplateId(@NotNull Path path) {
            assert Files.exists(path) : "Path not found: " + path;
            assert Files.isRegularFile(path) : "Path is not a file: " + path;
            this.path = path;
        }

        public @NotNull Path relativePath() {
            return sourceRoot.relativize(path);
        }

        public @NotNull String id() {
            return relativePath().toString();
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof TemplateId that && Objects.equals(this.path, that.path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        @Override
        public String toString() {
            return id();
        }

        public @NotNull List<String> readLines() {
            return BasicFiles.readAllLinesOrDie(path);
        }
    }
}
