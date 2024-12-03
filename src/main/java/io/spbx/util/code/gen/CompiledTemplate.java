package io.spbx.util.code.gen;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.lang.BasicNulls;
import io.spbx.util.base.tuple.OneOf;
import io.spbx.util.code.gen.Directive.Predefined;
import io.spbx.util.collect.iter.BasicIterables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static io.spbx.util.base.lang.BasicNulls.firstNonNullIfExists;

@Immutable
record CompiledTemplate(@NotNull List<Block> blocks) {
    static @NotNull CompiledTemplate of(@NotNull Iterable<Block> blocks) {
        return new CompiledTemplate(ImmutableList.copyOf(blocks));
    }

    @Nullable DirectiveBlock findBlockByNameOrNull(@NotNull String name) {
        return firstNonNullIfExists(blocks, block -> block.findBlockByNameOrNull(name));
    }

    void forEachReference(@NotNull Consumer<String> consumer) {
        blocks.forEach(block -> block.forEachReference(consumer));
    }

    @NotNull List<String> references() {
        return BasicIterables.forEachToList(this::forEachReference);
    }

    @Immutable
    record CompiledDirective(@NotNull String name,
                             @NotNull Predefined predefined,
                             @NotNull String attrs,
                             @NotNull Attrs parsedAttrs,
                             @NotNull Map<String, String> namedAttrs) {
        public static @NotNull CompiledDirective from(@NotNull Directive directive) {
            String attrs = directive.attrs();
            Attrs parsedAttrs = directive.type().isComment() ? Attrs.EMPTY : Attrs.parse(attrs);
            Map<String, String> namedAttrs = switch (directive.predefined()) {
                case IMPORT -> parsedAttrs.toNamedMap("file", "block");
                case PLACEHOLDER -> parsedAttrs.toNamedMap("file");
                case IF, ELSE, REMOVE, ASSUME, ASSERT, COMMENT, EOD_OF_TEMPLATE -> Map.of();
                default -> parsedAttrs.toNamedMap();
            };
            return new CompiledDirective(directive.name(), directive.predefined(), attrs, parsedAttrs, namedAttrs);
        }

        public boolean is(@NotNull Predefined predefined) {
            return predefined() == predefined;
        }

        public @NotNull String importReference() {
            assert is(Predefined.IMPORT) : "Must be an `import` directive: " + this;
            return namedAttrs.get("file");
        }

        public @Nullable String importBlockName() {
            assert is(Predefined.IMPORT) : "Must be an `import` directive: " + this;
            return namedAttrs.get("block");
        }

        public @NotNull String placeholder() {
            assert is(Predefined.PLACEHOLDER) : "Must be an `placeholder` directive: " + this;
            return namedAttrs.get("file");
        }

        public boolean evalCondition(@NotNull Variables vars) {
            assert is(Predefined.IF) || is(Predefined.ELSE) || is(Predefined.ASSUME) || is(Predefined.ASSERT) :
                "Must be an `if`, `else`, `assume` or `assert` directive: " + this;
            return parsedAttrs.eval(vars);
        }

        @Override
        public String toString() {
            return "%s%s `%s`".formatted(name, predefined, attrs);
        }
    }

    interface Block {
        @Nullable DirectiveBlock findBlockByNameOrNull(@NotNull String name);

        void forEachReference(@NotNull Consumer<String> consumer);

        default @NotNull List<String> references() {
            return BasicIterables.forEachToList(this::forEachReference);
        }
    }

    @Immutable
    record DirectiveBlock(@NotNull CompiledDirective directive, @NotNull List<Block> inner) implements Block {
        public static @NotNull DirectiveBlock of(@NotNull CompiledDirective directive, @NotNull Iterable<Block> inner) {
            return new DirectiveBlock(directive, ImmutableList.copyOf(inner));
        }

        @Override
        public @Nullable DirectiveBlock findBlockByNameOrNull(@NotNull String name) {
            if (name.equals(directive.name())) {
                return this;
            }
            return firstNonNullIfExists(inner, block -> block.findBlockByNameOrNull(name));
        }

        @Override
        public void forEachReference(@NotNull Consumer<String> consumer) {
            if (directive.is(Predefined.IMPORT)) {
                consumer.accept(directive.importReference());
            }
            inner.forEach(block -> block.forEachReference(consumer));
        }
    }

    @Immutable
    record LiteralBlock(@NotNull List<String> literals) implements Block {
        public static @NotNull LiteralBlock of(@NotNull Iterable<String> values) {
            return new LiteralBlock(ImmutableList.copyOf(values));
        }

        @Override
        public @Nullable DirectiveBlock findBlockByNameOrNull(@NotNull String name) {
            return null;
        }

        @Override
        public void forEachReference(@NotNull Consumer<String> consumer) {}
    }

    @Immutable
    record Node(@NotNull OneOf<String, Directive> oneOf) {
        public static @NotNull Node literalOf(@NotNull String literal) {
            return new Node(OneOf.ofFirst(literal));
        }

        public static @NotNull Node directiveOf(@NotNull Directive directive) {
            return new Node(OneOf.ofSecond(directive));
        }

        public boolean isEmpty() {
            return oneOf.testFirstIfSet(String::isBlank);
        }

        public boolean isLiteral() {
            return oneOf.hasFirst();
        }

        public boolean isDirective() {
            return oneOf.hasSecond();
        }

        public @NotNull String literal() {
            return oneOf.first();
        }

        public @NotNull Directive directive() {
            return oneOf.second();
        }

        public @Nullable Node nullifyIf(@NotNull Predicate<Node> predicate) {
            return BasicNulls.nullifyIf(this, predicate);
        }
    }
}
