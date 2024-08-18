package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.CharArray;
import org.jetbrains.annotations.NotNull;

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
