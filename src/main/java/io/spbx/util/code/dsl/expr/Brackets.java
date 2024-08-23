package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.array.CharArray;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.extern.guava.GuavaMaps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 *
 * @link <a href="https://en.wikipedia.org/wiki/Bracket">Wiki</a>
 */
@Immutable
public enum Brackets {
    PARENTHESIS('(', ')'),
    SQUARE('[', ']'),
    CURLY('{', '}'),
    ANGLE('<', '>');

    private final char open;
    private final char close;

    Brackets(char open, char close) {
        this.open = open;
        this.close = close;
    }

    private @NotNull CharArray open() {
        return CharArray.of(open);
    }

    private @NotNull CharArray close() {
        return CharArray.of(close);
    }

    static final Map<CharArray, Brackets> BY_CHARS_OPEN = BasicMaps.indexBy(values(), Brackets::open);
    static final Map<CharArray, Brackets> BY_CHARS_CLOSE = BasicMaps.indexBy(values(), Brackets::close);
    static final Map<CharArray, Brackets> BY_CHARS = GuavaMaps.mergeToImmutableMap(BY_CHARS_OPEN, BY_CHARS_CLOSE);

    public static boolean isOpen(@NotNull CharArray value) {
        return value.contentEquals('(') || value.contentEquals('[') || value.contentEquals('{');
    }

    public static boolean isClose(@NotNull CharArray value) {
        return value.contentEquals(')') || value.contentEquals(']') || value.contentEquals('}');
    }

    public boolean matches(@NotNull Brackets brackets) {
        return this == brackets;
    }
}
