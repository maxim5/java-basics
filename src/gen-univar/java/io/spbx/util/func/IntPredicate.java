package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple predicate for the primitive {@code int} values.
 */
@FunctionalInterface
@Generated(value = "$Type$Predicate.java", date = "2025-01-14T10:07:33.473121600Z")
public interface IntPredicate extends
        java.util.function.IntPredicate,
        Predicate<Integer>,
        Function<Integer, Boolean> {
    /**
     * Evaluates this predicate on the given argument.
     */
    boolean test(int value);

    @Override
    default boolean test(Integer value) {
        return this.test((int) value);
    }

    @Override
    default Boolean apply(Integer value) {
        return this.test((int) value);
    }

    @Override
    default @NotNull IntPredicate negate() {
        return value -> !test(value);
    }

    @Override
    default @NotNull IntPredicate and(@NotNull java.util.function.IntPredicate that) {
        return value -> this.test(value) && that.test(value);
    }

    @Override
    default @NotNull IntPredicate and(@NotNull Predicate<? super Integer> that) {
        return value -> this.test(value) && that.test(value);
    }

    @Override
    default @NotNull IntPredicate or(@NotNull java.util.function.IntPredicate that) {
        return value -> this.test(value) || that.test(value);
    }

    @Override
    default @NotNull IntPredicate or(@NotNull Predicate<? super Integer> that) {
        return value -> this.test(value) || that.test(value);
    }
}
