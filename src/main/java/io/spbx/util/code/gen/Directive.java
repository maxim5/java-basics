package io.spbx.util.code.gen;

import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.base.tuple.OneOf;
import io.spbx.util.base.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.concurrent.Immutable;
import java.util.stream.Stream;

@Immutable
record Directive(@NotNull String name, @NotNull Predefined predefined, @NotNull Modifier modifier, @NotNull Type type, @NotNull String attrs) {
    static @NotNull Directive predefinedOf(@NotNull Predefined predefined, @NotNull Type type, @NotNull String attrs) {
        return new Directive("", predefined, Modifier.NONE, type, attrs);
    }

    static @NotNull Directive customOf(@NotNull String name, @NotNull Modifier modifier, @NotNull Type type, @NotNull String attrs) {
        return new Directive(name, Predefined.NONE, modifier, type, attrs);
    }

    static @NotNull Directive commentOf(@NotNull String text, @NotNull Type type) {
        return new Directive("", Predefined.COMMENT, Modifier.NONE, type, text);
    }

    static @NotNull Directive parseDirective(@NotNull String content, @NotNull Type type) {
        int space = content.indexOf(' ');
        String key = space == -1 ? content : content.substring(0, space);
        String rest = space == -1 ? "" : content.substring(space + 1);
        OneOf<Predefined, Pair<String, Modifier>> parsedKey = Directive.parseKey(key);
        return parsedKey.mapToObj(
            predefined -> Directive.predefinedOf(predefined, type, rest),
            pair -> pair.mapToObj((name, modifier) -> Directive.customOf(name, modifier, type, rest))
        );
    }

    @VisibleForTesting
    static @NotNull OneOf<Predefined, Pair<String, Modifier>> parseKey(@NotNull String key) {
        Predefined predefined = Stream.of(Predefined.values()).filter(val -> val.matches(key)).findAny().orElse(Predefined.NONE);
        if (predefined.isSet()) {
            return OneOf.ofFirst(predefined);
        }
        Modifier modifier = Stream.of(Modifier.values()).filter(val -> val.matches(key)).findAny().orElse(Modifier.NONE);
        String name = modifier.cutOffModifier(key);
        return OneOf.ofSecond(Pair.of(name, modifier));
    }

    public boolean is(@NotNull Predefined predefined) {
        return predefined() == predefined;
    }

    public boolean is(@NotNull Modifier modifier) {
        return modifier() == modifier;
    }

    boolean isTreatedInline() {
        return predefined().isForceInline() || (type().isInline() && !predefined().isForceBlock());
    }

    boolean canBeClosedBy(@NotNull Directive that) {
        return this.name().equals(that.name()) && Modifier.isClosure(this.modifier(), that.modifier());
    }

    @NotNull Directive withAttrs(@NotNull String attrs) {
        return new Directive(name, predefined, modifier, type, attrs);
    }

    enum Type {
        INLINE,
        BLOCK,
        COMMENT_INLINE,
        COMMENT_BLOCK;

        public boolean isInline() {
            return this == INLINE || this == COMMENT_INLINE;
        }

        public boolean isBlock() {
            return this == BLOCK || this == COMMENT_BLOCK;
        }

        public boolean isComment() {
            return this == COMMENT_INLINE || this == COMMENT_BLOCK;
        }
    }

    enum Predefined {
        NONE("", Scope.DEFAULT),
        IF("if", Scope.FORCE_BLOCK),
        ELSE("else", Scope.FORCE_BLOCK),
        IMPORT("import", Scope.FORCE_INLINE),
        PLACEHOLDER("placeholder", Scope.FORCE_INLINE),
        ASSUME("assume", Scope.FORCE_INLINE),
        ASSERT("assert", Scope.FORCE_INLINE),
        WITH("with", Scope.FORCE_BLOCK),
        REMOVE("remove", Scope.FORCE_INLINE),
        COMMENT("", Scope.FORCE_INLINE),
        EOD_OF_TEMPLATE("EOT", Scope.FORCE_BLOCK);

        private final String lookupName;
        private final Scope scope;

        Predefined(@NotNull String name, @NotNull Scope scope) {
            this.lookupName = name;
            this.scope = scope;
        }

        public boolean isSet() {
            return this != NONE;
        }

        private boolean isForceInline() {
            return scope == Scope.FORCE_INLINE;
        }

        private boolean isForceBlock() {
            return scope == Scope.FORCE_BLOCK;
        }

        private boolean matches(@NotNull String str) {
            return lookupName.equals(str);
        }

        @Override
        public String toString() {
            return lookupName;
        }

        private enum Scope {
            DEFAULT,
            FORCE_INLINE,
            FORCE_BLOCK,
        }
    }

    enum Modifier {
        NONE(""),
        START("start"),
        END("end");

        private final String mod;
        private final String suffix;

        Modifier(@NotNull String mod) {
            this.mod = mod;
            this.suffix = mod.isEmpty() ? "" : "-" + mod;
        }

        public boolean isSet() {
            return this != NONE;
        }

        public boolean isEnd() {
            return this == END;
        }

        static boolean isClosure(@NotNull Modifier first, @NotNull Modifier second) {
            return first != END && second == END;
        }

        @NotNull String makeKey(@NotNull String name) {
            return name.isEmpty() ? mod : name + suffix;
        }

        private boolean matches(@NotNull String str) {
            return isSet() && (str.equals(mod) || str.endsWith(suffix));
        }

        private @NotNull String cutOffModifier(@NotNull String str) {
            if (str.equals(mod)) {
                return "";
            }
            return BasicStrings.removeSuffix(str, suffix);
        }
    }
}
