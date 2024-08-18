package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
public record ExprInBrackets(@NotNull Brackets brackets, @NotNull Expr nested) implements Expr {
    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(brackets, nested.eval(evaluator));
    }
}
