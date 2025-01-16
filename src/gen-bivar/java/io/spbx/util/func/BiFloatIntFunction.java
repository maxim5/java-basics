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
@Generated(value = "Bi$Left$$Right$Function.java", date = "2025-01-14T10:38:22.030297800Z")
public interface BiFloatIntFunction<R> extends
    BiFunction<Float, Integer, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(float left, int right);

    @Override
    default R apply(Float left, Integer right) {
        return apply((float) left, (int) right);
    }
}
