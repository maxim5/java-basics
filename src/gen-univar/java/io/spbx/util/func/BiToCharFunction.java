package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a binary function that produces a primitive {@code char}-valued result.
 */
@FunctionalInterface
@Generated(value = "BiTo$Type$Function.java", date = "2024-12-02T15:53:08.808592300Z")
public interface BiToCharFunction<T, U> extends BiFunction<T, U, Character> {
    /**
     * Applies this function to the given arguments.
     */
    char applyToChar(T left, U right);

    @Override
    default Character apply(T left, U right) {
        return applyToChar(left, right);
    }
}
