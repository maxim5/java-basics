package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code long}-valued operands and producing a
 * {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiLongToIntFunction extends
        BiFunction<Long, Long, Integer>,
        BiLongFunction<Integer>,
        BiToIntFunction<Long, Long>,
        BiLongObjFunction<Long, Integer> {
    /**
     * Applies this operator to the given operands.
     */
    int applyToInt(long left, long right);

    @Override
    default int applyToInt(Long left, Long right) {
        return applyToInt((long) left, (long) right);
    }

    @Override
    default Integer apply(long left, long right) {
        return applyToInt(left, right);
    }

    @Override
    default Integer apply(Long left, Long right) {
        return applyToInt((long) left, (long) right);
    }

    @Override
    default Integer apply(long left, Long right) {
        return applyToInt(left, (long) right);
    }
}
