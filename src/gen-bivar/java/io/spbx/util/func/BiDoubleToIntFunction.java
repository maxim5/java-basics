package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code double}-valued operands and producing a
 * {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiDoubleToIntFunction extends
        BiFunction<Double, Double, Integer>,
        BiDoubleFunction<Integer>,
        BiToIntFunction<Double, Double>,
        BiDoubleObjFunction<Double, Integer> {
    /**
     * Applies this operator to the given operands.
     */
    int applyToInt(double left, double right);

    @Override
    default int applyToInt(Double left, Double right) {
        return applyToInt((double) left, (double) right);
    }

    @Override
    default Integer apply(double left, double right) {
        return applyToInt(left, right);
    }

    @Override
    default Integer apply(Double left, Double right) {
        return applyToInt((double) left, (double) right);
    }

    @Override
    default Integer apply(double left, Double right) {
        return applyToInt(left, (double) right);
    }
}
