package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides convenient boolean {@link Function} and {@link Predicate} related API.
 *
 * @see Functions
 * @see Predicates
 * @see Consumers
 */
public class BoolFunctions {
    /**
     * Chains the {@link Predicate} {@code p} and boolean {@link Function} {@code f}
     * forming a higher order {@link Function}.
     */
    public static <A, R> @NotNull Function<A, R> chain(@NotNull Predicate<A> p, @NotNull Function<Boolean, R> f) {
        return a -> f.apply(p.test(a));
    }

    /**
     * Chains the {@link BiPredicate} {@code p} and the boolean {@link Function} {@code f}
     * forming a higher order {@link BiFunction}.
     */
    public static <A, B, R> @NotNull BiFunction<A, B, R> chain(@NotNull BiPredicate<A, B> p,
                                                               @NotNull Function<Boolean, R> f) {
        return (a, b) -> f.apply(p.test(a, b));
    }

    /**
     * Chains two {@link Predicate}s {@code p1} and {@code p2} with a boolean {@link BiFunction} {@code f}
     * forming a higher order {@link BiFunction}.
     */
    public static <A, B, R> @NotNull BiFunction<A, B, R> chain(@NotNull Predicate<A> p1,
                                                               @NotNull Predicate<B> p2,
                                                               @NotNull BiFunction<Boolean, Boolean, R> f) {
        return (a, b) -> f.apply(p1.test(a), p2.test(b));
    }
}
