package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public record UnaryPrefixExpr(@NotNull PrefixOp op, @NotNull Expr expr) implements Expr {
    public static @NotNull Expr wrapIfNonNull(@Nullable PrefixOp op, @NotNull Expr expr) {
        return op != null ? new UnaryPrefixExpr(op, expr) : expr;
    }

    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(op, expr.eval(evaluator));
    }
}
