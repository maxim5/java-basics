package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a binary function that produces a primitive {@code float}-valued result.
 */
@FunctionalInterface
@Generated(value = "BiTo$Type$Function.java", date = "2025-01-14T10:07:33.520131900Z")
public interface BiToFloatFunction<T, U> extends BiFunction<T, U, Float> {
    /**
     * Applies this function to the given arguments.
     */
    float applyToFloat(T left, U right);

    @Override
    default Float apply(T left, U right) {
        return applyToFloat(left, right);
    }
}
