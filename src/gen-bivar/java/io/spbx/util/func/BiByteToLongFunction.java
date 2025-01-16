package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code byte}-valued operands and producing a
 * {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiByteToLongFunction extends
        BiFunction<Byte, Byte, Long>,
        BiByteFunction<Long>,
        BiToLongFunction<Byte, Byte>,
        BiByteObjFunction<Byte, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(byte left, byte right);

    @Override
    default long applyToLong(Byte left, Byte right) {
        return applyToLong((byte) left, (byte) right);
    }

    @Override
    default Long apply(byte left, byte right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(Byte left, Byte right) {
        return applyToLong((byte) left, (byte) right);
    }

    @Override
    default Long apply(byte left, Byte right) {
        return applyToLong(left, (byte) right);
    }
}
