package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple predicate for the primitive {@code long} values.
 */
@FunctionalInterface
@Generated(value = "$Type$Predicate.java", date = "2024-12-02T15:53:08.750579500Z")
public interface LongPredicate extends
        java.util.function.LongPredicate,
        Predicate<Long>,
        Function<Long, Boolean> {
    /**
     * Evaluates this predicate on the given argument.
     */
    boolean test(long value);

    @Override
    default boolean test(Long value) {
        return this.test((long) value);
    }

    @Override
    default Boolean apply(Long value) {
        return this.test((long) value);
    }

    @Override
    default @NotNull LongPredicate negate() {
        return value -> !test(value);
    }

    @Override
    default @NotNull LongPredicate and(@NotNull java.util.function.LongPredicate that) {
        return value -> this.test(value) && that.test(value);
    }

    @Override
    default @NotNull LongPredicate and(@NotNull Predicate<? super Long> that) {
        return value -> this.test(value) && that.test(value);
    }

    @Override
    default @NotNull LongPredicate or(@NotNull java.util.function.LongPredicate that) {
        return value -> this.test(value) || that.test(value);
    }

    @Override
    default @NotNull LongPredicate or(@NotNull Predicate<? super Long> that) {
        return value -> this.test(value) || that.test(value);
    }
}
