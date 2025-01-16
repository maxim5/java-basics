package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code long}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code long}.
 *
 * @see BiPredicate
 * @see BiLongObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2025-01-14T10:07:33.514130900Z")
public interface BiLongPredicate extends
        BiPredicate<Long, Long>,
        BiLongObjPredicate<Long>,
        BiLongObjFunction<Long, Boolean>,
        BiLongFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code long} arguments.
     */
    boolean test(long left, long right);

    @Override
    default boolean test(Long left, Long right) {
        return test(left, right);
    }

    @Override
    default boolean test(long left, Long right) {
        return test(left, (long) right);
    }

    @Override
    default Boolean apply(long left, Long right) {
        return test(left, (long) right);
    }

    @Override
    default Boolean apply(long left, long right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Long left, Long right) {
        return test((long) left, (long) right);
    }
}
