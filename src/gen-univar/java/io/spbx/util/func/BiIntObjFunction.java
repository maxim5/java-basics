package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code int}.
 *
 * @param <T> the type of one of the arguments to the function
 * @param <R> the type of the result of the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see IntFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjFunction.java", date = "2025-01-14T10:07:33.504128700Z")
public interface BiIntObjFunction<T, R> extends BiFunction<Integer, T, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    R apply(int left, T right);

    @Override
    default R apply(Integer left, T right) {
        return apply((int) left, right);
    }
}
