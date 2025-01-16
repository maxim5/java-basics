package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple predicate for the primitive {@code float} values.
 */
@FunctionalInterface
@Generated(value = "$Type$Predicate.java", date = "2025-01-14T10:07:33.473121600Z")
public interface FloatPredicate extends
        Predicate<Float>,
        Function<Float, Boolean> {
    /**
     * Evaluates this predicate on the given argument.
     */
    boolean test(float value);

    @Override
    default boolean test(Float value) {
        return this.test((float) value);
    }

    @Override
    default Boolean apply(Float value) {
        return this.test((float) value);
    }
}
