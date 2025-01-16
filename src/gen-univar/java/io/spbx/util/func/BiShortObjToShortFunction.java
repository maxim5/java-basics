package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code short} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code short}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjShortFunction
 * @see ShortFunction
 * @see BiToShortFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2025-01-14T10:07:33.511130300Z")
public interface BiShortObjToShortFunction<T> extends
        BiShortObjFunction<T, Short>,
        BiToShortFunction<Short, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    short applyToShort(short left, T right);

    @Override
    default Short apply(short left, T right) {
        return applyToShort(left, right);
    }

    @Override
    default short applyToShort(Short left, T right) {
        return applyToShort((short) left, right);
    }

    @Override
    default Short apply(Short left, T right) {
        return applyToShort((short) left, right);
    }
}
