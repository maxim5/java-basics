package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code long} and {@code int},
 * and the result is {@code long}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Function.java", date = "2025-01-14T10:38:22.030297800Z")
public interface BiLongIntFunction<R> extends
    BiFunction<Long, Integer, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(long left, int right);

    @Override
    default R apply(Long left, Integer right) {
        return apply((long) left, (int) right);
    }
}
