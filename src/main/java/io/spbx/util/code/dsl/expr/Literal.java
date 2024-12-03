package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.str.CharArray;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

@Immutable
public record Literal(@NotNull Quotes quotes, @NotNull CharArray value) implements Expr {
    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(this);
    }
}
