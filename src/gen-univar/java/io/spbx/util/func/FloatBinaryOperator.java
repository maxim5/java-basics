package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BinaryOperator;

/**
 * Represents an operation upon two {@code float}-valued operands and producing a
 * {@code float}-valued result. This is the primitive type specialization of
 * {@link BinaryOperator} for {@code float}.
 */
@FunctionalInterface
@Generated(value = "$Type$BinaryOperator.java", date = "2025-01-14T10:07:33.453117Z")
public interface FloatBinaryOperator extends
        BinaryOperator<Float>,
        BiFloatObjFunction<Float, Float> {
    /**
     * Applies this operator to the given operands.
     */
    float applyToFloat(float left, float right);

    @Override
    default Float apply(Float left, Float right) {
        return applyToFloat(left, right);
    }

    @Override
    default Float apply(float left, Float right) {
        return this.applyToFloat(left, right);
    }
}
