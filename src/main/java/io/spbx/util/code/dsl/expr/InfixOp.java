package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.array.CharArray;
import io.spbx.util.collect.BasicMaps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Immutable
public enum InfixOp {
    PLUS("+", Type.ARITHMETIC),
    MINUS("-", Type.ARITHMETIC),
    MULT("*", Type.ARITHMETIC),
    POW("**", Type.ARITHMETIC),
    DIV("/", Type.ARITHMETIC),
    DIV2("//", Type.ARITHMETIC),
    MOD("%", Type.ARITHMETIC),
    MOD2("%%", Type.ARITHMETIC),

    AND("&", Type.BITWISE),
    AND2("&&", Type.LOGICAL),
    OR("|", Type.BITWISE),
    OR2("||", Type.LOGICAL),
    XOR("^", Type.BITWISE),
    XOR2("^^", Type.BITWISE),
    LSH("<<", Type.BITWISE),
    LSH3("<<<", Type.BITWISE),
    RSH(">>", Type.BITWISE),
    RSH3(">>>", Type.BITWISE),

    EQ("=", Type.COMPARE),
    EQ2("==", Type.COMPARE),
    EQ3("===", Type.COMPARE),
    NEQ("!=", Type.COMPARE),
    NEQ3("!==", Type.COMPARE),
    GT(">", Type.COMPARE),
    LT("<", Type.COMPARE),
    GE(">=", Type.COMPARE),
    LE("<=", Type.COMPARE),

    ASSIGN(":=", Type.OTHER),
    ARROW("->", Type.OTHER),
    QUESTION("?", Type.OTHER),
    COLON(":", Type.OTHER);

    private final CharArray op;
    private final char ch;
    private final Type type;

    InfixOp(@NotNull String op, @NotNull Type type) {
        this.op = CharArray.of(op);
        this.ch = op.length() == 1 ? op.charAt(0) : 0;
        this.type = type;
    }

    public @NotNull CharArray op() {
        return op;
    }

    public char ch() {
        return ch;
    }

    public @NotNull Type type() {
        return type;
    }

    static final Map<CharArray, InfixOp> BY_CHARS = BasicMaps.indexBy(values(), InfixOp::op);

    public enum Type {
        ARITHMETIC,
        LOGICAL,
        BITWISE,
        COMPARE,
        OTHER,
    }
}
