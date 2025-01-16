package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code double}-valued operands and producing a
 * {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiDoubleToLongFunction extends
        BiFunction<Double, Double, Long>,
        BiDoubleFunction<Long>,
        BiToLongFunction<Double, Double>,
        BiDoubleObjFunction<Double, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(double left, double right);

    @Override
    default long applyToLong(Double left, Double right) {
        return applyToLong((double) left, (double) right);
    }

    @Override
    default Long apply(double left, double right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(Double left, Double right) {
        return applyToLong((double) left, (double) right);
    }

    @Override
    default Long apply(double left, Double right) {
        return applyToLong(left, (double) right);
    }
}
