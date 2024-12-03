package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code int} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code int}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjIntFunction
 * @see IntFunction
 * @see BiToIntFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2024-12-02T15:53:08.796590100Z")
public interface BiIntObjToIntFunction<T> extends BiIntObjFunction<T, Integer>, BiToIntFunction<Integer, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    int applyToInt(int left, T right);

    @Override
    default Integer apply(int left, T right) {
        return applyToInt(left, right);
    }

    @Override
    default int applyToInt(Integer left, T right) {
        return applyToInt((int) left, right);
    }

    @Override
    default Integer apply(Integer left, T right) {
        return applyToInt((int) left, right);
    }
}
