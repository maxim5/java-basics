package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code byte}-valued operands and producing a
 * {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiByteToIntFunction extends
        BiFunction<Byte, Byte, Integer>,
        BiByteFunction<Integer>,
        BiToIntFunction<Byte, Byte>,
        BiByteObjFunction<Byte, Integer> {
    /**
     * Applies this operator to the given operands.
     */
    int applyToInt(byte left, byte right);

    @Override
    default int applyToInt(Byte left, Byte right) {
        return applyToInt((byte) left, (byte) right);
    }

    @Override
    default Integer apply(byte left, byte right) {
        return applyToInt(left, right);
    }

    @Override
    default Integer apply(Byte left, Byte right) {
        return applyToInt((byte) left, (byte) right);
    }

    @Override
    default Integer apply(byte left, Byte right) {
        return applyToInt(left, (byte) right);
    }
}
