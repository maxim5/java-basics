package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code int}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code int} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiIntPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2024-12-02T15:53:08.791588800Z")
public interface BiIntObjPredicate<T> extends
        BiPredicate<Integer, T>,
        BiIntObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code int} arguments.
     */
    boolean test(int left, T right);

    @Override
    default boolean test(Integer left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(int left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Integer left, T right) {
        return test((int) left, right);
    }
}
