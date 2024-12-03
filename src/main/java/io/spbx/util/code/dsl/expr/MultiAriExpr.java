package io.spbx.util.code.dsl.expr;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public record MultiAriExpr(@NotNull Expr start, @NotNull List<Pair<InfixOp, Expr>> rest) implements Expr {
    public static @NotNull MultiAriExpr of(@NotNull Expr start, @NotNull Iterable<Pair<InfixOp, Expr>> rest) {
        return new MultiAriExpr(start, ImmutableList.copyOf(rest));
    }

    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(start.eval(evaluator), rest.stream().map(pair -> pair.mapSecond(e -> e.eval(evaluator))).toList());
    }
}
