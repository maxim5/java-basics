package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code long}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2025-01-14T10:07:33.497126600Z")
public interface BiLongFunction<R> extends
        BiFunction<Long, Long, R>,
        BiLongObjFunction<Long, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(long left, long right);

    @Override
    default R apply(Long left, Long right) {
        return this.apply((long) left, (long) right);
    }

    @Override
    default R apply(long left, Long right) {
        return this.apply(left, (long) right);
    }
}
