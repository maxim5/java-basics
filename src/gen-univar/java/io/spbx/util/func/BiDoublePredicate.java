package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code double}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code double}.
 *
 * @see BiPredicate
 * @see BiDoubleObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2024-12-02T15:53:08.800591400Z")
public interface BiDoublePredicate extends
        BiPredicate<Double, Double>,
        BiDoubleObjPredicate<Double>,
        BiDoubleObjFunction<Double, Boolean>,
        BiDoubleFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code double} arguments.
     */
    boolean test(double left, double right);

    @Override
    default boolean test(Double left, Double right) {
        return test(left, right);
    }

    @Override
    default boolean test(double left, Double right) {
        return test(left, (double) right);
    }

    @Override
    default Boolean apply(double left, Double right) {
        return test(left, (double) right);
    }

    @Override
    default Boolean apply(double left, double right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Double left, Double right) {
        return test((double) left, (double) right);
    }
}
