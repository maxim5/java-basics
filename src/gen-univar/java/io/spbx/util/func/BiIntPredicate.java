package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code int}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code int}.
 *
 * @see BiPredicate
 * @see BiIntObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2024-12-02T15:53:08.800591400Z")
public interface BiIntPredicate extends
        BiPredicate<Integer, Integer>,
        BiIntObjPredicate<Integer>,
        BiIntObjFunction<Integer, Boolean>,
        BiIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code int} arguments.
     */
    boolean test(int left, int right);

    @Override
    default boolean test(Integer left, Integer right) {
        return test(left, right);
    }

    @Override
    default boolean test(int left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(int left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(int left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Integer left, Integer right) {
        return test((int) left, (int) right);
    }
}
