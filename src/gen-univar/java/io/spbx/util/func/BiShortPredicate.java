package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code short}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code short}.
 *
 * @see BiPredicate
 * @see BiShortObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2025-01-14T10:07:33.514130900Z")
public interface BiShortPredicate extends
        BiPredicate<Short, Short>,
        BiShortObjPredicate<Short>,
        BiShortObjFunction<Short, Boolean>,
        BiShortFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code short} arguments.
     */
    boolean test(short left, short right);

    @Override
    default boolean test(Short left, Short right) {
        return test(left, right);
    }

    @Override
    default boolean test(short left, Short right) {
        return test(left, (short) right);
    }

    @Override
    default Boolean apply(short left, Short right) {
        return test(left, (short) right);
    }

    @Override
    default Boolean apply(short left, short right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Short left, Short right) {
        return test((short) left, (short) right);
    }
}
