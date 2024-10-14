package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.ToLongFunction;

/**
 * Provides convenient {@link LongFunction}-related API.
 *
 * @see Functions
 */
@Generated(value = "$Type$Functions.java", date = "2024-10-14T13:46:36.070162272Z")
public class LongFunctions {
    /**
     * Returns a constant {@link LongFunction}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <R> @NotNull LongFunction<R> constant(R value) {
        return t -> value;
    }

    /**
     * Returns a constant {@link ToLongFunction}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <T> @NotNull ToLongFunction<T> constant(long value) {
        return t -> value;
    }

    /**
     * Wraps the {@link ToLongFunction} {@code f} into a new {@link ToLongFunction} which
     * makes sure {@code f} is not called with {@code null} argument, instead {@code def} is returned.
     */
    public static <R> @NotNull ToLongFunction<R> nonNullify(@NotNull ToLongFunction<R> f, long def) {
        return t -> t != null ? f.applyAsLong(t) : def;
    }

    /**
     * Wraps the {@link ToLongFunction} {@code f} into a new {@link ToLongFunction} which
     * makes sure {@code f} is not called with {@code null} argument, instead {@code 0} is returned.
     */
    public static <R> @NotNull ToLongFunction<R> bypassNull(@NotNull ToLongFunction<R> f) {
        return t -> t != null ? f.applyAsLong(t) : 0;
    }

    /**
     * Chains the {@link LongFunction} {@code f1} and {@link Function} {@code f2} (of the result type)
     * forming a higher order {@link LongFunction}.
     */
    public static <T, R> @NotNull LongFunction<R> chainObj(@NotNull LongFunction<T> f1, @NotNull Function<T, R> f2) {
        return t -> f2.apply(f1.apply(t));
    }

    /**
     * Chains the {@link Function} {@code f1} and {@link ToLongFunction} {@code f2} (of the result type)
     * forming a higher order {@link ToLongFunction}.
     */
    public static <T, U> @NotNull ToLongFunction<T> chainLong(@NotNull Function<T, U> f1, @NotNull ToLongFunction<U> f2) {
        return t -> f2.applyAsLong(f1.apply(t));
    }

    /**
     * Chains two {@link LongUnaryOperator}s {@code f1} and {@code f2} forming a higher order {@link LongUnaryOperator}.
     */
    public static @NotNull LongUnaryOperator chainLongs(@NotNull LongUnaryOperator op1, @NotNull LongUnaryOperator op2) {
        return t -> op2.applyAsLong(op1.applyAsLong(t));
    }

    /**
     * Chains the {@link LongPredicate} {@code f1} and boolean {@link Function} {@code f2}
     * forming a higher order {@link LongFunction}.
     */
    public static <R> @NotNull LongFunction<R> chainBool(@NotNull LongPredicate p, @NotNull Function<Boolean, R> f) {
        return t -> f.apply(p.test(t));
    }
}
