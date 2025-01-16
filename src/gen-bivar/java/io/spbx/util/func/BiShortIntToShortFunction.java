package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code short} and {@code int},
 * and the result is {@code short}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2025-01-14T10:38:22.045301100Z")
public interface BiShortIntToShortFunction extends
        BiFunction<Short, Integer, Short>,
        BiToShortFunction<Short, Integer>,
        BiShortIntFunction<Short> {
    /**
     * Applies this function to the given argument.
     */
    short applyToShort(short left, int right);

    @Override
    default short applyToShort(Short left, Integer right) {
        return applyToShort((short) left, (int) right);
    }

    @Override
    default Short apply(Short left, Integer right) {
        return applyToShort((short) left, (int) right);
    }

    @Override
    default Short apply(short left, int right) {
        return applyToShort(left, right);
    }
}
