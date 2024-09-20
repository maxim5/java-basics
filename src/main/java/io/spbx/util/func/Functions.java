package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides convenient {@link Function}-related API.
 */
public class Functions {
    public static <T, R> @NotNull Function<T, R> constant(R value) {
        return t -> value;
    }

    public static <A, B, C> @NotNull Function<A, C> chain(@NotNull Function<A, B> f1, @NotNull Function<B, C> f2) {
        return a -> f2.apply(f1.apply(a));
    }

    public static <T, R> @NotNull Function<T, R> chain(@NotNull Predicate<T> p, @NotNull Function<Boolean, R> f) {
        return t -> f.apply(p.test(t));
    }

    public static <T, R> @NotNull Function<T, R> nonNullify(@NotNull Function<T, R> f, @NotNull R def) {
        return t -> t != null ? f.apply(t) : def;
    }

    public static <T, R> @NotNull Function<T, R> bypassNull(@NotNull Function<T, R> f) {
        return t -> t != null ? f.apply(t) : null;
    }
}
