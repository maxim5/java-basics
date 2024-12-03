package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.str.CharArray;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

@Immutable
public record Identifier(@NotNull CharArray name) implements Expr {
    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(this);
    }

    @Override
    public String toString() {
        return "`%s`".formatted(name);
    }
}
