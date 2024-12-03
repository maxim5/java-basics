package io.spbx.util.code.dsl.expr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

@Immutable
public record UnaryPostfixExpr(@NotNull Expr expr, @NotNull PostfixOp op) implements Expr {
    public static @NotNull Expr wrapIfNonNull(@NotNull Expr expr, @Nullable PostfixOp op) {
        return op != null ? new UnaryPostfixExpr(expr, op) : expr;
    }

    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(expr.eval(evaluator), op);
    }
}
