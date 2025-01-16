package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments: {@code short} and {@code int}.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate}.
 *
 * @see BiPredicate
 * @see BiShortPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Predicate.java", date = "2025-01-14T10:38:22.038299500Z")
public interface BiShortIntPredicate extends
        BiPredicate<Short, Integer>,
        BiShortObjPredicate<Integer>,
        BiShortIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code short} and {@code int} arguments.
     */
    boolean test(short left, int right);

    @Override
    default boolean test(Short left, Integer right) {
        return test((short) left, (int) right);
    }

    @Override
    default boolean test(short left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(short left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Short left, Integer right) {
        return test((short) left, (int) right);
    }
}
