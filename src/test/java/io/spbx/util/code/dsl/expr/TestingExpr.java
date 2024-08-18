package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.CharArray;
import io.spbx.util.base.Pair;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.testing.TestingBasics.listOf;
import static io.spbx.util.testing.TestingBasics.streamOf;

public class TestingExpr {
    public static @NotNull Lexem @NotNull[] toLexems(@NotNull String @NotNull ... values) {
        return streamOf(values).map(CharArray::of).map(Lexem::of).toArray(Lexem[]::new);
    }

    public static @NotNull Lexem toLexem(@NotNull String value) {
        return new Lexem(CharArray.of(value));
    }

    public static @NotNull ExprLexer newLexer(@NotNull String @NotNull ... values) {
        return new ExprLexer(streamOf(values).map(CharArray::of).map(Lexem::of).toList());
    }

    public static @NotNull ExprLexer newLexer(@NotNull Object @NotNull ... values) {
        return new ExprLexer(streamOf(values).map(TestingExpr::toString).map(CharArray::of).map(Lexem::of).toList());
    }

    public static @NotNull ExprParser newParser(@NotNull SyntaxOptions options, @NotNull String @NotNull ... values) {
        return new ExprParser(options, newLexer(values));
    }

    public static @NotNull ExprParser newParser(@NotNull SyntaxOptions options, @NotNull Object @NotNull ... values) {
        return new ExprParser(options, newLexer(values));
    }

    public static @NotNull Identifier identifier(@NotNull String value) {
        return new Identifier(CharArray.of(value));
    }

    public static @NotNull Numeric numeric(@NotNull String value) {
        return new Numeric(CharArray.of(value));
    }

    public static @NotNull Numeric numeric(long value) {
        return new Numeric(CharArray.of(Long.toString(value)));
    }

    public static @NotNull Literal literal(@NotNull Quotes quotes, @NotNull String value) {
        return new Literal(quotes, CharArray.of(value));
    }

    public static @NotNull UnaryPrefixExpr prefix(@NotNull PrefixOp prefixOp, @NotNull Expr expr) {
        return new UnaryPrefixExpr(prefixOp, expr);
    }

    public static @NotNull UnaryPostfixExpr postfix(@NotNull Expr expr, @NotNull PostfixOp postfixOp) {
        return new UnaryPostfixExpr(expr, postfixOp);
    }

    public static @NotNull BinaryExpr binary(@NotNull Expr left, @NotNull InfixOp infixOp, @NotNull Expr right) {
        return new BinaryExpr(left, infixOp, right);
    }

    public static @NotNull MultiAriExpr multi(@NotNull Expr a, @NotNull InfixOp op1, @NotNull Expr b,
                                              @NotNull InfixOp op2, @NotNull Expr c) {
        return new MultiAriExpr(a, listOf(Pair.of(op1, b), Pair.of(op2, c)));
    }

    public static @NotNull MultiAriExpr multi(@NotNull Expr a, @NotNull InfixOp op1, @NotNull Expr b,
                                              @NotNull InfixOp op2, @NotNull Expr c, @NotNull InfixOp op3, @NotNull Expr d) {
        return new MultiAriExpr(a, listOf(Pair.of(op1, b), Pair.of(op2, c), Pair.of(op3, d)));
    }

    public static @NotNull ExprInBrackets inBrackets(@NotNull Brackets brackets, @NotNull Expr nested) {
        return new ExprInBrackets(brackets, nested);
    }

    public static @NotNull Sequence sequence(@NotNull Expr first, @NotNull Sequence.Separator sep, @NotNull Expr second) {
        return new Sequence(listOf(first, second), sep);
    }

    public static @NotNull Sequence sequence(@NotNull Sequence.Separator sep, @NotNull Expr @NotNull ... terms) {
        return new Sequence(listOf(terms), sep);
    }

    private static @NotNull CharSequence toString(@NotNull Object any) {
        return switch (any) {
            case Identifier identifier -> identifier.name();
            case Numeric numeric -> numeric.value();
            default -> any.toString();
        };
    }
}
