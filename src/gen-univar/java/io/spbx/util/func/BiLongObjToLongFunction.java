package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code long} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code long}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjLongFunction
 * @see LongFunction
 * @see BiToLongFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2024-12-02T15:53:08.796590100Z")
public interface BiLongObjToLongFunction<T> extends BiLongObjFunction<T, Long>, BiToLongFunction<Long, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    long applyToLong(long left, T right);

    @Override
    default Long apply(long left, T right) {
        return applyToLong(left, right);
    }

    @Override
    default long applyToLong(Long left, T right) {
        return applyToLong((long) left, right);
    }

    @Override
    default Long apply(Long left, T right) {
        return applyToLong((long) left, right);
    }
}
