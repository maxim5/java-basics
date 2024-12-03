package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.str.CharArray;
import io.spbx.util.collect.map.BasicMaps;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

@Immutable
public enum PostfixOp {
    PLUS("+"),
    PLUS2("++"),
    MINUS("-"),
    MINUS2("--"),
    QUESTION("?"),
    BANG("!!");

    private final CharArray op;
    private final char ch;

    PostfixOp(@NotNull String op) {
        this.op = CharArray.of(op);
        this.ch = op.length() == 1 ? op.charAt(0) : 0;
    }

    public @NotNull CharArray op() {
        return op;
    }

    public char ch() {
        return ch;
    }

    static final Map<CharArray, PostfixOp> BY_CHARS = BasicMaps.indexBy(values(), PostfixOp::op);
}
