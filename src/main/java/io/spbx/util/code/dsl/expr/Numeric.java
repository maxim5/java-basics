package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.array.CharArray;
import org.jetbrains.annotations.NotNull;

@Immutable
public record Numeric(@NotNull CharArray value) implements Expr {
    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(this);
    }

    @Override
    public String toString() {
        return "`%s`".formatted(value);
    }
}
