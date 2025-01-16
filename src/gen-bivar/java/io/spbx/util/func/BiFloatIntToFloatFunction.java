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
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2025-01-14T10:38:22.045301100Z")
public interface BiFloatIntToFloatFunction extends
        BiFunction<Float, Integer, Float>,
        BiToFloatFunction<Float, Integer>,
        BiFloatIntFunction<Float> {
    /**
     * Applies this function to the given argument.
     */
    float applyToFloat(float left, int right);

    @Override
    default float applyToFloat(Float left, Integer right) {
        return applyToFloat((float) left, (int) right);
    }

    @Override
    default Float apply(Float left, Integer right) {
        return applyToFloat((float) left, (int) right);
    }

    @Override
    default Float apply(float left, int right) {
        return applyToFloat(left, right);
    }
}
