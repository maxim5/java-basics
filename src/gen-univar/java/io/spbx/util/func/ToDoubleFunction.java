package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code double}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2025-01-14T10:07:33.539136500Z")
public interface ToDoubleFunction<T> extends Function<T, Double> {
    /**
     * Applies this function to the given argument.
     */
    double applyToDouble(T value);

    @Override
    default Double apply(T value) {
        return applyToDouble(value);
    }
}
