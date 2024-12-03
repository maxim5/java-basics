package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code char}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code char}.
 *
 * @see BiPredicate
 * @see BiCharObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2024-12-02T15:53:08.800591400Z")
public interface BiCharPredicate extends
        BiPredicate<Character, Character>,
        BiCharObjPredicate<Character>,
        BiCharObjFunction<Character, Boolean>,
        BiCharFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code char} arguments.
     */
    boolean test(char left, char right);

    @Override
    default boolean test(Character left, Character right) {
        return test(left, right);
    }

    @Override
    default boolean test(char left, Character right) {
        return test(left, (char) right);
    }

    @Override
    default Boolean apply(char left, Character right) {
        return test(left, (char) right);
    }

    @Override
    default Boolean apply(char left, char right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Character left, Character right) {
        return test((char) left, (char) right);
    }
}
