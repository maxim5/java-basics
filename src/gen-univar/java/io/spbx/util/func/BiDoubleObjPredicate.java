package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code double}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code double} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiDoublePredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2024-12-02T15:53:08.791588800Z")
public interface BiDoubleObjPredicate<T> extends
        BiPredicate<Double, T>,
        BiDoubleObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code double} arguments.
     */
    boolean test(double left, T right);

    @Override
    default boolean test(Double left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(double left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Double left, T right) {
        return test((double) left, right);
    }
}
