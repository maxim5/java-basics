package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple predicate for the primitive {@code short} values.
 */
@FunctionalInterface
@Generated(value = "$Type$Predicate.java", date = "2025-01-14T10:07:33.473121600Z")
public interface ShortPredicate extends
        Predicate<Short>,
        Function<Short, Boolean> {
    /**
     * Evaluates this predicate on the given argument.
     */
    boolean test(short value);

    @Override
    default boolean test(Short value) {
        return this.test((short) value);
    }

    @Override
    default Boolean apply(Short value) {
        return this.test((short) value);
    }
}
