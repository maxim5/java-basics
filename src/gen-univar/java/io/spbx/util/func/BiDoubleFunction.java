package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code double}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2025-01-14T10:07:33.497126600Z")
public interface BiDoubleFunction<R> extends
        BiFunction<Double, Double, R>,
        BiDoubleObjFunction<Double, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(double left, double right);

    @Override
    default R apply(Double left, Double right) {
        return this.apply((double) left, (double) right);
    }

    @Override
    default R apply(double left, Double right) {
        return this.apply(left, (double) right);
    }
}
