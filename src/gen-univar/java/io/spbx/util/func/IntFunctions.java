package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * Provides convenient {@link IntFunction}-related API.
 *
 * @see Functions
 */
@Stateless
@Generated(value = "$Type$Functions.java", date = "2025-01-14T10:07:33.468120600Z")
public class IntFunctions {
    /**
     * Returns a constant {@link IntFunction}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <R> @NotNull IntFunction<R> constant(R value) {
        return t -> value;
    }

    /**
     * Returns a constant {@link ToIntFunction}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <T> @NotNull ToIntFunction<T> constant(int value) {
        return t -> value;
    }

    /**
     * Wraps the {@link ToIntFunction} {@code f} into a new {@link ToIntFunction} which
     * makes sure {@code f} is not called with {@code null} argument, instead {@code def} is returned.
     */
    public static <R> @NotNull ToIntFunction<R> nonNullify(@NotNull ToIntFunction<R> f, int def) {
        return t -> t != null ? f.applyAsInt(t) : def;
    }

    /**
     * Wraps the {@link ToIntFunction} {@code f} into a new {@link ToIntFunction} which
     * makes sure {@code f} is not called with {@code null} argument, instead {@code 0} is returned.
     */
    public static <R> @NotNull ToIntFunction<R> bypassNull(@NotNull ToIntFunction<R> f) {
        return t -> t != null ? f.applyAsInt(t) : 0;
    }

    /**
     * Chains the {@link IntFunction} {@code f1} and {@link Function} {@code f2} (of the result type)
     * forming a higher order {@link IntFunction}.
     */
    public static <T, R> @NotNull IntFunction<R> chainObj(@NotNull IntFunction<T> f1, @NotNull Function<T, R> f2) {
        return t -> f2.apply(f1.apply(t));
    }

    /**
     * Chains the {@link Function} {@code f1} and {@link ToIntFunction} {@code f2} (of the result type)
     * forming a higher order {@link ToIntFunction}.
     */
    public static <T, U> @NotNull ToIntFunction<T> chainInt(@NotNull Function<T, U> f1, @NotNull ToIntFunction<U> f2) {
        return t -> f2.applyAsInt(f1.apply(t));
    }

    /**
     * Chains two {@link IntUnaryOperator}s {@code f1} and {@code f2} forming a higher order {@link IntUnaryOperator}.
     */
    public static @NotNull IntUnaryOperator chainInts(@NotNull IntUnaryOperator op1, @NotNull IntUnaryOperator op2) {
        return t -> op2.applyAsInt(op1.applyAsInt(t));
    }

    /**
     * Chains the {@link IntPredicate} {@code f1} and boolean {@link Function} {@code f2}
     * forming a higher order {@link IntFunction}.
     */
    public static <R> @NotNull IntFunction<R> chainBool(@NotNull IntPredicate p, @NotNull Function<Boolean, R> f) {
        return t -> f.apply(p.test(t));
    }
}
