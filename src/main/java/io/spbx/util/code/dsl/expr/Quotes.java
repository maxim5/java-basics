package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.str.CharArray;
import io.spbx.util.collect.map.BasicMaps;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

@Immutable
public enum Quotes {
    SINGLE('\''),
    DOUBLE('"'),
    BACKTICK('`');

    private final CharArray array;
    private final char ch;

    Quotes(char ch) {
        this.array = CharArray.of(ch);
        this.ch = ch;
    }

    public @NotNull CharArray array() {
        return array;
    }

    public boolean matches(@NotNull Quotes that) {
        return this.ch == that.ch;
    }

    static final Map<CharArray, Quotes> BY_CHARS = BasicMaps.indexBy(values(), Quotes::array);
}
