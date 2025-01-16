package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code short}-valued operands and producing a
 * {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiShortToLongFunction extends
        BiFunction<Short, Short, Long>,
        BiShortFunction<Long>,
        BiToLongFunction<Short, Short>,
        BiShortObjFunction<Short, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(short left, short right);

    @Override
    default long applyToLong(Short left, Short right) {
        return applyToLong((short) left, (short) right);
    }

    @Override
    default Long apply(short left, short right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(Short left, Short right) {
        return applyToLong((short) left, (short) right);
    }

    @Override
    default Long apply(short left, Short right) {
        return applyToLong(left, (short) right);
    }
}
