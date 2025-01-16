package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BinaryOperator;

/**
 * Represents an operation upon two {@code double}-valued operands and producing a
 * {@code double}-valued result. This is the primitive type specialization of
 * {@link BinaryOperator} for {@code double}.
 */
@FunctionalInterface
@Generated(value = "$Type$BinaryOperator.java", date = "2025-01-14T10:07:33.453117Z")
public interface DoubleBinaryOperator extends
        java.util.function.DoubleBinaryOperator,
        BinaryOperator<Double>,
        BiDoubleObjFunction<Double, Double> {
    /**
     * Applies this operator to the given operands.
     */
    double applyToDouble(double left, double right);

    @Override
    default Double apply(Double left, Double right) {
        return applyToDouble(left, right);
    }

    @Override
    default Double apply(double left, Double right) {
        return this.applyToDouble(left, right);
    }

    @Override
    default double applyAsDouble(double left, double right) {
        return this.applyToDouble(left, right);
    }
}
