package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple predicate for the primitive {@code char} values.
 */
@FunctionalInterface
@Generated(value = "$Type$Predicate.java", date = "2025-01-14T10:07:33.473121600Z")
public interface CharPredicate extends
        Predicate<Character>,
        Function<Character, Boolean> {
    /**
     * Evaluates this predicate on the given argument.
     */
    boolean test(char value);

    @Override
    default boolean test(Character value) {
        return this.test((char) value);
    }

    @Override
    default Boolean apply(Character value) {
        return this.test((char) value);
    }
}
