package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code byte}.
 *
 * @param <T> the type of one of the arguments to the function
 * @param <R> the type of the result of the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ByteFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjFunction.java", date = "2025-01-14T10:07:33.504128700Z")
public interface BiByteObjFunction<T, R> extends BiFunction<Byte, T, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    R apply(byte left, T right);

    @Override
    default R apply(Byte left, T right) {
        return apply((byte) left, right);
    }
}
