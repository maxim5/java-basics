package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a binary function that produces a primitive {@code short}-valued result.
 */
@FunctionalInterface
@Generated(value = "BiTo$Type$Function.java", date = "2024-12-02T15:53:08.808592300Z")
public interface BiToShortFunction<T, U> extends BiFunction<T, U, Short> {
    /**
     * Applies this function to the given arguments.
     */
    short applyToShort(T left, U right);

    @Override
    default Short apply(T left, U right) {
        return applyToShort(left, right);
    }
}
