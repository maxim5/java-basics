package io.spbx.util.code.gen;

import io.spbx.util.code.gen.Directive.Modifier;
import io.spbx.util.code.gen.Directive.Predefined;
import io.spbx.util.code.gen.Directive.Type;
import org.jetbrains.annotations.NotNull;

record DirectiveBuilder(@NotNull Directive dir) {
    public static @NotNull DirectiveBuilder builderOf(@NotNull Directive dir) {
        return new DirectiveBuilder(dir);
    }

    public static @NotNull DirectiveBuilder builderOf(@NotNull String name,
                                                      @NotNull Predefined predefined,
                                                      @NotNull Modifier modifier,
                                                      @NotNull Type type,
                                                      @NotNull String attrs) {
        return builderOf(new Directive(name, predefined, modifier, type, attrs));
    }

    public static @NotNull DirectiveBuilder commentBlock(@NotNull String text) {
        return builderOf(Directive.commentOf(text, Type.COMMENT_BLOCK));
    }

    public static @NotNull DirectiveBuilder commentInline(@NotNull String text) {
        return builderOf(Directive.commentOf(text, Type.COMMENT_INLINE));
    }

    public static @NotNull DirectiveBuilder block() {
        return builderOf("", Predefined.NONE, Modifier.NONE, Type.BLOCK, "");
    }

    public static @NotNull DirectiveBuilder inline() {
        return builderOf("", Predefined.NONE, Modifier.NONE, Type.INLINE, "");
    }

    public @NotNull DirectiveBuilder predef(@NotNull Predefined predef) {
        return builderOf("", predef, dir.modifier(), dir.type(), dir.attrs());
    }

    public @NotNull DirectiveBuilder name(@NotNull String name) {
        return builderOf(name, dir.predefined(), dir.modifier(), dir.type(), dir.attrs());
    }

    public @NotNull DirectiveBuilder with(@NotNull Modifier modifier) {
        return builderOf(dir.name(), dir.predefined(), modifier, dir.type(), dir.attrs());
    }

    public @NotNull DirectiveBuilder attrs(@NotNull String attrs) {
        return builderOf(dir.name(), dir.predefined(), dir.modifier(), dir.type(), attrs);
    }

    public @NotNull Directive build() {
        return dir;
    }
}
