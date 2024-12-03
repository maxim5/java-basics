package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code char}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2024-12-02T15:53:08.825596700Z")
public interface ToCharFunction<T> extends Function<T, Character> {
    /**
     * Applies this function to the given argument.
     */
    char applyToChar(T value);

    @Override
    default Character apply(T value) {
        return applyToChar(value);
    }
}
