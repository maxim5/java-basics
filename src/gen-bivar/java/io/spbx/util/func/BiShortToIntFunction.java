package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code short}-valued operands and producing a
 * {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiShortToIntFunction extends
        BiFunction<Short, Short, Integer>,
        BiShortFunction<Integer>,
        BiToIntFunction<Short, Short>,
        BiShortObjFunction<Short, Integer> {
    /**
     * Applies this operator to the given operands.
     */
    int applyToInt(short left, short right);

    @Override
    default int applyToInt(Short left, Short right) {
        return applyToInt((short) left, (short) right);
    }

    @Override
    default Integer apply(short left, short right) {
        return applyToInt(left, right);
    }

    @Override
    default Integer apply(Short left, Short right) {
        return applyToInt((short) left, (short) right);
    }

    @Override
    default Integer apply(short left, Short right) {
        return applyToInt(left, (short) right);
    }
}
