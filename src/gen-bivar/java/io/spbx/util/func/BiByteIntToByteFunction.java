package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code byte} and {@code int},
 * and the result is {@code byte}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2025-01-14T10:38:22.045301100Z")
public interface BiByteIntToByteFunction extends
        BiFunction<Byte, Integer, Byte>,
        BiToByteFunction<Byte, Integer>,
        BiByteIntFunction<Byte> {
    /**
     * Applies this function to the given argument.
     */
    byte applyToByte(byte left, int right);

    @Override
    default byte applyToByte(Byte left, Integer right) {
        return applyToByte((byte) left, (int) right);
    }

    @Override
    default Byte apply(Byte left, Integer right) {
        return applyToByte((byte) left, (int) right);
    }

    @Override
    default Byte apply(byte left, int right) {
        return applyToByte(left, right);
    }
}
