package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.array.CharArray;
import io.spbx.util.collect.BasicMaps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Immutable
public enum PrefixOp {
    NOT("!"),
    BANG("!!"),
    TILDA("~"),
    PLUS("+"),
    PLUS2("++"),
    MINUS("-"),
    MINUS2("--"),
    AMP("&"),
    STAR("*"),
    AT("@");

    private final CharArray op;
    private final char ch;

    PrefixOp(@NotNull String op) {
        this.op = CharArray.of(op);
        this.ch = op.length() == 1 ? op.charAt(0) : 0;
    }

    public @NotNull CharArray op() {
        return op;
    }

    public char ch() {
        return ch;
    }

    static final Map<CharArray, PrefixOp> BY_CHARS = BasicMaps.indexBy(values(), PrefixOp::op);
}
