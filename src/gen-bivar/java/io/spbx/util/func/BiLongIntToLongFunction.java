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
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2025-01-14T10:38:22.045301100Z")
public interface BiLongIntToLongFunction extends
        BiFunction<Long, Integer, Long>,
        BiToLongFunction<Long, Integer>,
        BiLongIntFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    long applyToLong(long left, int right);

    @Override
    default long applyToLong(Long left, Integer right) {
        return applyToLong((long) left, (int) right);
    }

    @Override
    default Long apply(Long left, Integer right) {
        return applyToLong((long) left, (int) right);
    }

    @Override
    default Long apply(long left, int right) {
        return applyToLong(left, right);
    }
}
