package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2025-01-14T10:07:33.539136500Z")
public interface ToLongFunction<T> extends Function<T, Long> {
    /**
     * Applies this function to the given argument.
     */
    long applyToLong(T value);

    @Override
    default Long apply(T value) {
        return applyToLong(value);
    }
}
