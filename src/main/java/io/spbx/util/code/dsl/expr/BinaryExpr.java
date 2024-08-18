package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
public record BinaryExpr(@NotNull Expr left, @NotNull InfixOp op, @NotNull Expr right) implements Expr {
    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(left.eval(evaluator), op, right.eval(evaluator));
    }
}
