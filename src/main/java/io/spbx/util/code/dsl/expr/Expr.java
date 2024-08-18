package io.spbx.util.code.dsl.expr;

import org.jetbrains.annotations.NotNull;

public interface Expr {
    Object eval(@NotNull ExprEvaluator evaluator);
}
