package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code char}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code char} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiCharPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2025-01-14T10:07:33.507129300Z")
public interface BiCharObjPredicate<T> extends
        BiPredicate<Character, T>,
        BiCharObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code char} and {@code T} arguments.
     */
    boolean test(char left, T right);

    @Override
    default boolean test(Character left, T right) {
        return test((char) left, right);
    }

    @Override
    default Boolean apply(char left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Character left, T right) {
        return test((char) left, right);
    }
}
