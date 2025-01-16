package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code int}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2025-01-14T10:07:33.497126600Z")
public interface BiIntFunction<R> extends
        BiFunction<Integer, Integer, R>,
        BiIntObjFunction<Integer, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(int left, int right);

    @Override
    default R apply(Integer left, Integer right) {
        return this.apply((int) left, (int) right);
    }

    @Override
    default R apply(int left, Integer right) {
        return this.apply(left, (int) right);
    }
}
