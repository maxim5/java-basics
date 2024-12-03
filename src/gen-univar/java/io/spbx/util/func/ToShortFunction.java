package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code short}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2024-12-02T15:53:08.825596700Z")
public interface ToShortFunction<T> extends Function<T, Short> {
    /**
     * Applies this function to the given argument.
     */
    short applyToShort(T value);

    @Override
    default Short apply(T value) {
        return applyToShort(value);
    }
}
