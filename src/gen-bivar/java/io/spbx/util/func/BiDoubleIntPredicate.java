package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments: {@code double} and {@code int}.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate}.
 *
 * @see BiPredicate
 * @see BiDoublePredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Predicate.java", date = "2025-01-14T10:38:22.038299500Z")
public interface BiDoubleIntPredicate extends
        BiPredicate<Double, Integer>,
        BiDoubleObjPredicate<Integer>,
        BiDoubleIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code double} and {@code int} arguments.
     */
    boolean test(double left, int right);

    @Override
    default boolean test(Double left, Integer right) {
        return test((double) left, (int) right);
    }

    @Override
    default boolean test(double left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(double left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Double left, Integer right) {
        return test((double) left, (int) right);
    }
}
