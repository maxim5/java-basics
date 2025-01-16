package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code byte} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code byte}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjByteFunction
 * @see ByteFunction
 * @see BiToByteFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2025-01-14T10:07:33.511130300Z")
public interface BiByteObjToByteFunction<T> extends
        BiByteObjFunction<T, Byte>,
        BiToByteFunction<Byte, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    byte applyToByte(byte left, T right);

    @Override
    default Byte apply(byte left, T right) {
        return applyToByte(left, right);
    }

    @Override
    default byte applyToByte(Byte left, T right) {
        return applyToByte((byte) left, right);
    }

    @Override
    default Byte apply(Byte left, T right) {
        return applyToByte((byte) left, right);
    }
}
