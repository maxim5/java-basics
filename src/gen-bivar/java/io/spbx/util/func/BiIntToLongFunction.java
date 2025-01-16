package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code int}-valued operands and producing a
 * {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiIntToLongFunction extends
        BiFunction<Integer, Integer, Long>,
        BiIntFunction<Long>,
        BiToLongFunction<Integer, Integer>,
        BiIntObjFunction<Integer, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(int left, int right);

    @Override
    default long applyToLong(Integer left, Integer right) {
        return applyToLong((int) left, (int) right);
    }

    @Override
    default Long apply(int left, int right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(Integer left, Integer right) {
        return applyToLong((int) left, (int) right);
    }

    @Override
    default Long apply(int left, Integer right) {
        return applyToLong(left, (int) right);
    }
}
