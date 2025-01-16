package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code float}.
 *
 * @param <T> the type of one of the arguments to the function
 * @param <R> the type of the result of the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see FloatFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjFunction.java", date = "2025-01-14T10:07:33.504128700Z")
public interface BiFloatObjFunction<T, R> extends BiFunction<Float, T, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    R apply(float left, T right);

    @Override
    default R apply(Float left, T right) {
        return apply((float) left, right);
    }
}
