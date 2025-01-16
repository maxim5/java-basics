package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BinaryOperator;

/**
 * Represents an operation upon two {@code long}-valued operands and producing a
 * {@code long}-valued result. This is the primitive type specialization of
 * {@link BinaryOperator} for {@code long}.
 */
@FunctionalInterface
@Generated(value = "$Type$BinaryOperator.java", date = "2025-01-14T10:07:33.453117Z")
public interface LongBinaryOperator extends
        java.util.function.LongBinaryOperator,
        BinaryOperator<Long>,
        BiLongObjFunction<Long, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(long left, long right);

    @Override
    default Long apply(Long left, Long right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(long left, Long right) {
        return this.applyToLong(left, right);
    }

    @Override
    default long applyAsLong(long left, long right) {
        return this.applyToLong(left, right);
    }
}
