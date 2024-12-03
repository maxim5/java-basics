package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code float} and {@code int},
 * and the result is {@code float}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2024-11-26T15:08:01.477872300Z")
public interface BiFloatIntToFloatFunction extends
        BiFunction<Float, Integer, Float>,
        BiToFloatFunction<Float, Integer> {
    /**
     * Applies this function to the given argument.
     */
    float applyToFloat(float left, int right);

    @Override
    default float applyToFloat(Float left, Integer right) {
        return applyToFloat(left, right);
    }

    @Override
    default Float apply(Float left, Integer right) {
        return applyToFloat(left, right);
    }
}
