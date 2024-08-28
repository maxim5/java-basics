package io.spbx.util.code.gen;

import com.google.common.flogger.FluentLogger;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.io.BasicFiles;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static io.spbx.util.base.BasicExceptions.notImplemented;

public class StandardTemplateCodegen {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public static @NotNull Map<String, String> defaults(@NotNull Path template) {
        return Map.of(
            "now", Instant.now().toString(),
            "source_template", template.getFileName().toString()
        );
    }

    public static void generateAll(@NotNull File root,
                                   @NotNull File dest,
                                   @NotNull List<Map<String, String>> vars) {
        generateAll(root.toPath(), dest.toPath(), vars);
    }

    public static void generateAll(@NotNull Path root,
                                   @NotNull Path dest,
                                   @NotNull List<Map<String, String>> vars) {
        log.at(Level.INFO).log("Generate all: %s -> %s", root, dest);
        TemplateEngine engine = new TemplateEngine(root, dest);
        BasicFiles.walkRegularFiles(root, path -> {
            Map<String, String> defaults = defaults(path);
            List<TemplateVars> varSet = vars.stream().map(map -> toTemplateVars(map, defaults)).toList();
            engine.render(path, varSet);
        });
    }

    private static @NotNull TemplateVars toTemplateVars(@NotNull Map<String, String> map,
                                                        @NotNull Map<String, String> defaults) {
        Map<String, String> merged = BasicMaps.mergeToMap(map, defaults);
        Variables variables = Variables.fixUpKeys(merged);
        return TemplateVars.of(variables);
    }

    public static void main(String[] args) {
        throw notImplemented("Command line API not available yet");
    }
}