package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code float}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code float} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiFloatPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2025-01-14T10:07:33.507129300Z")
public interface BiFloatObjPredicate<T> extends
        BiPredicate<Float, T>,
        BiFloatObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code float} and {@code T} arguments.
     */
    boolean test(float left, T right);

    @Override
    default boolean test(Float left, T right) {
        return test((float) left, right);
    }

    @Override
    default Boolean apply(float left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Float left, T right) {
        return test((float) left, right);
    }
}
