package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments: {@code char} and {@code int}.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate}.
 *
 * @see BiPredicate
 * @see BiCharPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Predicate.java", date = "2025-01-14T10:38:22.038299500Z")
public interface BiCharIntPredicate extends
        BiPredicate<Character, Integer>,
        BiCharObjPredicate<Integer>,
        BiCharIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code char} and {@code int} arguments.
     */
    boolean test(char left, int right);

    @Override
    default boolean test(Character left, Integer right) {
        return test((char) left, (int) right);
    }

    @Override
    default boolean test(char left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(char left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Character left, Integer right) {
        return test((char) left, (int) right);
    }
}
