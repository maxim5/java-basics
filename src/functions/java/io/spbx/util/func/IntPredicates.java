package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * Provides convenient {@link IntPredicate}-related API.
 *
 * @see Predicates
 */
@Generated(value = "$Type$Predicates.java", date = "2024-10-14T13:46:36.056560424Z")
public class IntPredicates {
    /**
     * A constant predicate which always returns {@code true} regardless of the input.
     */
    public static final IntPredicate TRUE = t -> true;
    /**
     * A constant predicate which always returns {@code false} regardless of the input.
     */
    public static final IntPredicate FALSE = t -> false;

    /**
     * Returns a constant {@link IntPredicate}: evaluates to the same result {@code value} regardless of the input.
     */
    public static @NotNull IntPredicate constant(boolean value) {
        return t -> value;
    }

    /**
     * Returns a {@link IntPredicate} which evaluates the equality to the specified {@code val}.
     */
    public static @NotNull IntPredicate equalTo(int val) {
        return t -> t == val;
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull IntPredicate and(@NotNull IntPredicate p1, @NotNull IntPredicate p2) {
        return t -> p1.test(t) && p2.test(t);
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull IntPredicate and(@NotNull IntPredicate p, boolean value) {
        return t -> value && p.test(t);
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static <T> @NotNull IntPredicate and(boolean value, @NotNull IntPredicate p) {
        return t -> value && p.test(t);
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @NotNull IntPredicate and(@NotNull IntPredicate @NotNull... predicates) {
        return t -> {
            for (IntPredicate predicate : predicates) {
                if (!predicate.test(t)) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @NotNull IntPredicate or(@NotNull IntPredicate p1, @NotNull IntPredicate p2) {
        return t -> p1.test(t) || p2.test(t);
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @NotNull IntPredicate or(@NotNull IntPredicate p, boolean value) {
        return t -> value || p.test(t);
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @NotNull IntPredicate or(boolean value, @NotNull IntPredicate p) {
        return t -> value || p.test(t);
    }

    /**
     * Returns a {@link IntPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     */
    public static @NotNull IntPredicate or(@NotNull IntPredicate @NotNull... predicates) {
        return t -> {
            for (IntPredicate predicate : predicates) {
                if (predicate.test(t)) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Wraps the {@link IntPredicate} {@code p} with an additional {@link Consumer} {@code c},
     * which is called before the evaluation of the predicate with the same input.
     */
    public static @NotNull IntPredicate peek(@NotNull IntConsumer c, @NotNull IntPredicate p) {
        return t -> {
            c.accept(t);
            return p.test(t);
        };
    }

    /**
     * Wraps the {@link IntConsumer} {@code c} as a {@link IntPredicate} which calls {@code c} and
     * returns {@code true}.
     */
    public static @NotNull IntPredicate peekAndReturnTrue(@NotNull IntConsumer c) {
        return t -> {
            c.accept(t);
            return true;
        };
    }

    /**
     * Wraps the {@link IntConsumer}s {@code c1} and {@code c2} as a {@link IntPredicate} which
     * calls both {@code c1} and {@code c2} in sequence and returns {@code true}.
     */
    public static @NotNull IntPredicate peekAndReturnTrue(@NotNull IntConsumer c1, @NotNull IntConsumer c2) {
        return t -> {
            c1.accept(t);
            c2.accept(t);
            return true;
        };
    }

    /**
     * Wraps the {@link IntConsumer}s as a {@link IntPredicate} which calls all {@code consumers} in sequence
     * and returns {@code true}.
     */
    public static @NotNull IntPredicate peekAndReturnTrue(@NotNull IntConsumer @NotNull... consumers) {
        return t -> {
            for (IntConsumer consumer : consumers) {
                consumer.accept(t);
            }
            return true;
        };
    }

    /**
     * Wraps the {@link IntConsumer} {@code c} as a {@link IntPredicate} which calls {@code c} and
     * returns {@code false}.
     */
    public static @NotNull IntPredicate peekAndReturnFalse(@NotNull IntConsumer c) {
        return t -> {
            c.accept(t);
            return false;
        };
    }

    /**
     * Wraps the {@link IntConsumer}s {@code c1} and {@code c2} as a {@link IntPredicate} which
     * calls both {@code c1} and {@code c2} in sequence and returns {@code false}.
     */
    public static @NotNull IntPredicate peekAndReturnFalse(@NotNull IntConsumer c1, @NotNull IntConsumer c2) {
        return t -> {
            c1.accept(t);
            c2.accept(t);
            return false;
        };
    }

    /**
     * Wraps the {@link IntConsumer}s as a {@link IntPredicate} which calls all {@code consumers} in sequence
     * and returns {@code false}.
     */
    public static @NotNull IntPredicate peekAndReturnFalse(@NotNull IntConsumer @NotNull... consumers) {
        return t -> {
            for (IntConsumer consumer : consumers) {
                consumer.accept(t);
            }
            return false;
        };
    }

    /**
     * Chains the {@link ToIntFunction} and {@link IntPredicate} (of the result type)
     * forming a higher order {@link Predicate}.
     */
    public static <T> @NotNull Predicate<T> chainObj(@NotNull ToIntFunction<T> f, @NotNull IntPredicate p) {
        return t -> p.test(f.applyAsInt(t));
    }

    /**
     * Chains the {@link IntFunction} and {@link Predicate} (of the result type)
     * forming a higher order {@link IntPredicate}.
     */
    public static <T> @NotNull IntPredicate chainInt(@NotNull IntFunction<T> f, @NotNull Predicate<T> p) {
        return t -> p.test(f.apply(t));
    }

    /**
     * Chains the {@link IntUnaryOperator} and {@link IntPredicate} (of the result type)
     * forming a higher order {@link IntPredicate}.
     */
    public static @NotNull IntPredicate chainInts(@NotNull IntUnaryOperator op, @NotNull IntPredicate p) {
        return t -> p.test(op.applyAsInt(t));
    }
}
