package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Provides convenient {@link Predicate}-related API.
 */
public class Predicates {
    public static <T> @NotNull Predicate<T> constant(boolean value) {
        return t -> value;
    }

    public static <T> @NotNull Predicate<T> equalTo(@Nullable T t) {
        return value -> Objects.equals(value, t);
    }

    public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> p1, @NotNull Predicate<T> p2) {
        return p1.and(p2);
    }

    public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> p1, boolean value) {
        return p1.and(constant(value));
    }

    public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> p1, @NotNull Predicate<T> p2) {
        return p1.or(p2);
    }

    public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> p1, boolean value) {
        return p1.or(constant(value));
    }

    public static <T> @NotNull Predicate<T> chain(@NotNull Consumer<T> c, @NotNull Predicate<T> p) {
        return t -> {
            c.accept(t);
            return p.test(t);
        };
    }
}
