package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code long}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code long} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiLongPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2024-12-02T15:53:08.791588800Z")
public interface BiLongObjPredicate<T> extends
        BiPredicate<Long, T>,
        BiLongObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code long} arguments.
     */
    boolean test(long left, T right);

    @Override
    default boolean test(Long left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(long left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Long left, T right) {
        return test((long) left, right);
    }
}
