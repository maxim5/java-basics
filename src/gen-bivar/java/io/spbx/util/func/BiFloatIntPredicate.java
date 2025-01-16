package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments: {@code float} and {@code int}.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate}.
 *
 * @see BiPredicate
 * @see BiFloatPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Predicate.java", date = "2025-01-14T10:38:22.038299500Z")
public interface BiFloatIntPredicate extends
        BiPredicate<Float, Integer>,
        BiFloatObjPredicate<Integer>,
        BiFloatIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code float} and {@code int} arguments.
     */
    boolean test(float left, int right);

    @Override
    default boolean test(Float left, Integer right) {
        return test((float) left, (int) right);
    }

    @Override
    default boolean test(float left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(float left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Float left, Integer right) {
        return test((float) left, (int) right);
    }
}
