package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Provides convenient {@link Function}-related API.
 *
 * @see BoolFunctions
 * @see Predicates
 * @see Consumers
 */
public class Functions {
    /**
     * Returns a constant {@link Function}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <T, R> @NotNull Function<T, R> constant(R value) {
        return t -> value;
    }

    /**
     * Wraps the {@link Function} {@code f} into a new {@link Function} which
     * makes sure {@code f} is not called with {@code null} argument, instead {@code def} is returned.
     */
    public static <T, R> @NotNull Function<T, R> nonNullify(@NotNull Function<T, R> f, @NotNull R def) {
        return t -> t != null ? f.apply(t) : def;
    }

    /**
     * Wraps the {@link Function} {@code f} into a new {@link Function} which
     * makes sure {@code f} is not called with {@code null} argument, instead {@code null} is returned.
     */
    public static <T, R> @NotNull Function<T, R> bypassNull(@NotNull Function<T, R> f) {
        return t -> t != null ? f.apply(t) : null;
    }

    /**
     * Chains two {@link Function}s {@code f1} and {@code f2} (of the result type) forming a higher order {@link Function}.
     */
    public static <A, B, R> @NotNull Function<A, R> chain(@NotNull Function<A, B> f1, @NotNull Function<B, R> f2) {
        return a -> f2.apply(f1.apply(a));
    }

    /**
     * Chains three {@link Function}s {@code f1}, {@code f2} and {@code f3} (of the result types)
     * forming a higher order {@link Function}.
     */
    public static <A, B, C, R> @NotNull Function<A, R> chain(@NotNull Function<A, B> f1,
                                                             @NotNull Function<B, C> f2,
                                                             @NotNull Function<C, R> f3) {
        return a -> f3.apply(f2.apply(f1.apply(a)));
    }

    /**
     * Chains the {@link BiFunction} {@code f1} and the {@link Function} {@code f2} (of the result type)
     * forming a higher order {@link BiFunction}.
     */
    public static <A, B, C, R> @NotNull BiFunction<A, B, R> chain(@NotNull BiFunction<A, B, C> f1,
                                                                  @NotNull Function<C, R> f2) {
        return (a, b) -> f2.apply(f1.apply(a, b));
    }

    /**
     * Chains two {@link Function}s {@code f1} and {@code f2} with a {@link BiFunction} {@code f3} (of the result types)
     * forming a higher order {@link BiFunction}.
     */
    public static <A, B, C, D, R> @NotNull BiFunction<A, C, R> chain(@NotNull Function<A, B> f1,
                                                                     @NotNull Function<C, D> f2,
                                                                     @NotNull BiFunction<B, D, R> f3) {
        return (a, c) -> f3.apply(f1.apply(a), f2.apply(c));
    }
}
