package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code double} and {@code int},
 * and the result is {@code double}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2025-01-14T10:38:22.045301100Z")
public interface BiDoubleIntToDoubleFunction extends
        BiFunction<Double, Integer, Double>,
        BiToDoubleFunction<Double, Integer>,
        BiDoubleIntFunction<Double> {
    /**
     * Applies this function to the given argument.
     */
    double applyToDouble(double left, int right);

    @Override
    default double applyToDouble(Double left, Integer right) {
        return applyToDouble((double) left, (int) right);
    }

    @Override
    default Double apply(Double left, Integer right) {
        return applyToDouble((double) left, (int) right);
    }

    @Override
    default Double apply(double left, int right) {
        return applyToDouble(left, right);
    }
}
