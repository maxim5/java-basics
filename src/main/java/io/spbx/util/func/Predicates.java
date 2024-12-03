package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
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
@Stateless
public class Predicates {
    /**
     * Returns a constant {@link Predicate}: evaluates to the same result {@code value} regardless of the input.
     */
    public static <T> @NotNull Predicate<T> constant(boolean value) {
        return t -> value;
    }

    /**
     * Returns a constant {@link Predicate} which always returns {@code true} regardless of the input.
     */
    public static <T> @NotNull Predicate<T> alwaysTrue() {
        return t -> true;
    }

    /**
     * Returns a constant {@link Predicate} which always returns {@code false} regardless of the input.
     */
    public static <T> @NotNull Predicate<T> alwaysFalse() {
        return t -> false;
    }

    /**
     * Returns a {@link Predicate} which evaluates the equality to the specified {@code val}. Allows null.
     */
    public static <T> @NotNull Predicate<T> equalTo(@Nullable T val) {
        return t -> Objects.equals(t, val);
    }

    /**
     * Returns a constant {@link Predicate} which returns {@code true} iff the input is {@code null}.
     */
    public static <T> @NotNull Predicate<T> isNull() {
        return Objects::isNull;
    }

    /**
     * Returns a constant {@link Predicate} which returns {@code true} iff the input is non-{@code null}.
     */
    public static <T> @NotNull Predicate<T> isNonNull() {
        return Objects::nonNull;
    }

    /**
     * Non-nullizes the predicate {@code p} by returning the {@link #alwaysTrue()} if {@code p == null}.
     */
    public static <T> @NotNull Predicate<T> constTrueIfNull(@Nullable Predicate<T> p) {
        return p == null ? alwaysTrue() : p;
    }

    /**
     * Non-nullizes the predicate {@code p} by returning the {@link #alwaysFalse()} if {@code p == null}.
     */
    public static <T> @NotNull Predicate<T> constFalseIfNull(@Nullable Predicate<T> p) {
        return p == null ? alwaysFalse() : p;
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static <T> @NotNull Predicate<T> and(@Nullable Predicate<T> p1, @Nullable Predicate<T> p2) {
        return p1 == null ? constTrueIfNull(p2) : p2 == null ? p1 : t -> p1.test(t) && p2.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static <T> @NotNull Predicate<T> and(@Nullable Predicate<T> p, boolean value) {
        return p == null ? constant(value) : t -> value && p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static <T> @NotNull Predicate<T> and(boolean value, @Nullable Predicate<T> p) {
        return p == null ? constant(value) : t -> value && p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static @SafeVarargs <T> @NotNull Predicate<T> and(@Nullable Predicate<T> @NotNull... predicates) {
        return t -> {
            for (Predicate<T> predicate : predicates) {
                if (predicate != null && !predicate.test(t)) {
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
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static <T> @NotNull Predicate<T> or(@Nullable Predicate<T> p1, @Nullable Predicate<T> p2) {
        return p1 == null ? constTrueIfNull(p2) : p2 == null ? p1 : t -> p1.test(t) || p2.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static <T> @NotNull Predicate<T> or(@Nullable Predicate<T> p, boolean value) {
        return p == null ? constant(value) : t -> value || p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static <T> @NotNull Predicate<T> or(boolean value, @Nullable Predicate<T> p) {
        return p == null ? constant(value) : t -> value || p.test(t);
    }

    /**
     * Returns a {@link Predicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static @SafeVarargs <T> @NotNull Predicate<T> or(@Nullable Predicate<T> @NotNull... predicates) {
        return t -> {
            for (Predicate<T> predicate : predicates) {
                if (predicate != null && predicate.test(t)) {
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
