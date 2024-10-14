package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides convenient {@link Predicate}-related API.
 *
 * @see BoolFunctions
 * @see Functions
 * @see Consumers
 */
public class Predicates {
    /**
     * Returns a constant {@link Predicate}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <T> @NotNull Predicate<T> constant(boolean value) {
        return t -> value;
    }

    /**
     * Returns a {@link Predicate} which evaluates the equality to the specified {@code val}. Allows null.
     */
    public static <T> @NotNull Predicate<T> equalTo(@Nullable T val) {
        return t -> Objects.equals(t, val);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> p1, @NotNull Predicate<T> p2) {
        return t -> p1.test(t) && p2.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> p, boolean value) {
        return t -> value && p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull Predicate<T> and(boolean value, @NotNull Predicate<T> p) {
        return t -> value && p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @SafeVarargs <T> @NotNull Predicate<T> and(@NotNull Predicate<T> @NotNull... predicates) {
        return t -> {
            for (Predicate<T> predicate : predicates) {
                if (!predicate.test(t)) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> p1, @NotNull Predicate<T> p2) {
        return t -> p1.test(t) || p2.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> p, boolean value) {
        return t -> value || p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull Predicate<T> or(boolean value, @NotNull Predicate<T> p) {
        return t -> value || p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @SafeVarargs <T> @NotNull Predicate<T> or(@NotNull Predicate<T> @NotNull... predicates) {
        return t -> {
            for (Predicate<T> predicate : predicates) {
                if (predicate.test(t)) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Wraps the {@link Predicate} {@code p} with an additional {@link Consumer} {@code c},
     * which is called before the evaluation of the predicate with the same input.
     */
    public static <T> @NotNull Predicate<T> peek(@NotNull Consumer<T> c, @NotNull Predicate<T> p) {
        return t -> {
            c.accept(t);
            return p.test(t);
        };
    }

    /**
     * Wraps the {@link Consumer} {@code c} as a predicate which calls {@code c} and returns {@code true}.
     */
    public static <T> @NotNull Predicate<T> peekAndReturnTrue(@NotNull Consumer<T> c) {
        return t -> {
            c.accept(t);
            return true;
        };
    }

    /**
     * Wraps the {@link Consumer}s {@code c1} and {@code c2} as a predicate which
     * calls both {@code c1} and {@code c2} in sequence and returns {@code true}.
     */
    public static <T> @NotNull Predicate<T> peekAndReturnTrue(@NotNull Consumer<T> c1, @NotNull Consumer<T> c2) {
        return t -> {
            c1.accept(t);
            c2.accept(t);
            return true;
        };
    }

    /**
     * Wraps the {@link Consumer}s as a {@link Predicate} which calls all {@code consumers} in sequence
     * and returns {@code true}.
     */
    public static @SafeVarargs <T> @NotNull Predicate<T> peekAndReturnTrue(@NotNull Consumer<T> @NotNull... consumers) {
        return t -> {
            for (Consumer<T> consumer : consumers) {
                consumer.accept(t);
            }
            return true;
        };
    }

    /**
     * Wraps the {@link Consumer} {@code c} as a {@link Predicate} which calls {@code c} and returns {@code false}.
     */
    public static <T> @NotNull Predicate<T> peekAndReturnFalse(@NotNull Consumer<T> c) {
        return t -> {
            c.accept(t);
            return false;
        };
    }

    /**
     * Wraps the {@link Consumer}s {@code c1} and {@code c2} as a {@link Predicate} which
     * calls both {@code c1} and {@code c2} in sequence and returns {@code false}.
     */
    public static <T> @NotNull Predicate<T> peekAndReturnFalse(@NotNull Consumer<T> c1, @NotNull Consumer<T> c2) {
        return t -> {
            c1.accept(t);
            c2.accept(t);
            return false;
        };
    }

    /**
     * Wraps the {@link Consumer}s as a {@link Predicate} which calls all {@code consumers} in sequence
     * and returns {@code false}.
     */
    public static @SafeVarargs <T> @NotNull Predicate<T> peekAndReturnFalse(@NotNull Consumer<T> @NotNull... consumers) {
        return t -> {
            for (Consumer<T> consumer : consumers) {
                consumer.accept(t);
            }
            return false;
        };
    }

    /**
     * Chains the {@link Function} and {@link Predicate} (of the result type) forming a higher order {@link Predicate}.
     */
    public static <T, U> @NotNull Predicate<T> chain(@NotNull Function<T, U> f, @NotNull Predicate<U> p) {
        return t -> p.test(f.apply(t));
    }
}
