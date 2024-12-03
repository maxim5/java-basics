package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.tuple.Pair;
import io.spbx.util.base.tuple.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ExprEvaluator {
    @Nullable Object eval(@NotNull Identifier identifier);

    @Nullable Object eval(@Nullable Object left, @NotNull InfixOp op, @Nullable Object right);

    @Nullable Object eval(@Nullable Object start, @NotNull List<Pair<InfixOp, @Nullable Object>> rest);

    @Nullable Object eval(@NotNull PrefixOp op, @Nullable Object val);

    @Nullable Object eval(@Nullable Object val, @NotNull PostfixOp op);

    default @Nullable Object eval(@NotNull List<@Nullable Object> terms, @NotNull Sequence.Separator sep) {
        return Tuple.from(terms);
    }

    default @NotNull Object eval(@NotNull Numeric numeric) {
        return numeric.value();
    }

    default @NotNull Object eval(@NotNull Literal literal) {
        return literal.value();
    }

    default @Nullable Object eval(@NotNull Brackets brackets, @Nullable Object nested) {
        return nested;
    }
}
