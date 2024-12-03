package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code float}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code float}.
 *
 * @see BiPredicate
 * @see BiFloatObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2024-12-02T15:53:08.800591400Z")
public interface BiFloatPredicate extends
        BiPredicate<Float, Float>,
        BiFloatObjPredicate<Float>,
        BiFloatObjFunction<Float, Boolean>,
        BiFloatFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code float} arguments.
     */
    boolean test(float left, float right);

    @Override
    default boolean test(Float left, Float right) {
        return test(left, right);
    }

    @Override
    default boolean test(float left, Float right) {
        return test(left, (float) right);
    }

    @Override
    default Boolean apply(float left, Float right) {
        return test(left, (float) right);
    }

    @Override
    default Boolean apply(float left, float right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Float left, Float right) {
        return test((float) left, (float) right);
    }
}
