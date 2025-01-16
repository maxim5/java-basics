package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments: {@code long} and {@code int}.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate}.
 *
 * @see BiPredicate
 * @see BiLongPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Predicate.java", date = "2025-01-14T10:38:22.038299500Z")
public interface BiLongIntPredicate extends
        BiPredicate<Long, Integer>,
        BiLongObjPredicate<Integer>,
        BiLongIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code long} and {@code int} arguments.
     */
    boolean test(long left, int right);

    @Override
    default boolean test(Long left, Integer right) {
        return test((long) left, (int) right);
    }

    @Override
    default boolean test(long left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(long left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Long left, Integer right) {
        return test((long) left, (int) right);
    }
}
