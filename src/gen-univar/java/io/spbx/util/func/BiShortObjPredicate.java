package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code short}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code short} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiShortPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2024-12-02T15:53:08.791588800Z")
public interface BiShortObjPredicate<T> extends
        BiPredicate<Short, T>,
        BiShortObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code short} arguments.
     */
    boolean test(short left, T right);

    @Override
    default boolean test(Short left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(short left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Short left, T right) {
        return test((short) left, right);
    }
}
