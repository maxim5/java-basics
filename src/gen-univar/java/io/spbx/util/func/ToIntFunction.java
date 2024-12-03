package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2024-12-02T15:53:08.825596700Z")
public interface ToIntFunction<T> extends Function<T, Integer> {
    /**
     * Applies this function to the given argument.
     */
    int applyToInt(T value);

    @Override
    default Integer apply(T value) {
        return applyToInt(value);
    }
}
