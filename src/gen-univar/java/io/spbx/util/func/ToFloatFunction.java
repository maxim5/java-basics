package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code float}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2025-01-14T10:07:33.539136500Z")
public interface ToFloatFunction<T> extends Function<T, Float> {
    /**
     * Applies this function to the given argument.
     */
    float applyToFloat(T value);

    @Override
    default Float apply(T value) {
        return applyToFloat(value);
    }
}
