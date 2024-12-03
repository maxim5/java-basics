package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.Generated;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

/**
 * Provides convenient {@link LongPredicate}-related API.
 *
 * @see Predicates
 */
@Stateless
@Generated(value = "$Type$Predicates.java", date = "2024-12-02T15:53:08.755579700Z")
public class LongPredicates {
    /**
     * A constant predicate which always returns {@code true} regardless of the input.
     */
    public static final LongPredicate TRUE = t -> true;
    /**
     * A constant predicate which always returns {@code false} regardless of the input.
     */
    public static final LongPredicate FALSE = t -> false;

    /**
     * Returns a constant {@link LongPredicate}: evaluates to the same result {@code value} regardless of the input.
     */
    public static @NotNull LongPredicate constant(boolean value) {
        return t -> value;
    }

    /**
     * Returns a {@link LongPredicate} which evaluates the equality to the specified {@code val}.
     */
    public static @NotNull LongPredicate equalTo(long val) {
        return t -> t == val;
    }

    /**
     * Non-nullizes the predicate {@code p} by returning the {@link #alwaysTrue()} if {@code p == null}.
     */
    public static @NotNull LongPredicate constTrueIfNull(@Nullable LongPredicate p) {
        return p == null ? TRUE : p;
    }

    /**
     * Non-nullizes the predicate {@code p} by returning the {@link #alwaysFalse()} if {@code p == null}.
     */
    public static @NotNull LongPredicate constFalseIfNull(@Nullable LongPredicate p) {
        return p == null ? FALSE : p;
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static <T> @NotNull LongPredicate and(@Nullable LongPredicate p1, @Nullable LongPredicate p2) {
        return p1 == null ? constTrueIfNull(p2) : p2 == null ? p1 : t -> p1.test(t) && p2.test(t);
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static <T> @NotNull LongPredicate and(@Nullable LongPredicate p, boolean value) {
        return p == null ? constant(value) : t -> value && p.test(t);
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static <T> @NotNull LongPredicate and(boolean value, @Nullable LongPredicate p) {
        return p == null ? constant(value) : t -> value && p.test(t);
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code &&} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysTrue()}.
     */
    public static @NotNull LongPredicate and(@Nullable LongPredicate @NotNull... predicates) {
        return t -> {
            for (LongPredicate predicate : predicates) {
                if (predicate != null && !predicate.test(t)) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that both {@code p1} and {@code p2} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static @NotNull LongPredicate or(@Nullable LongPredicate p1, @Nullable LongPredicate p2) {
        return p1 == null ? constTrueIfNull(p2) : p2 == null ? p1 : t -> p1.test(t) || p2.test(t);
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static @NotNull LongPredicate or(@Nullable LongPredicate p, boolean value) {
        return p == null ? constant(value) : t -> value || p.test(t);
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that {@code p} must not have side effects.
     * It is not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static @NotNull LongPredicate or(boolean value, @Nullable LongPredicate p) {
        return p == null ? constant(value) : t -> value || p.test(t);
    }

    /**
     * Returns a {@link LongPredicate} which evaluates a short-circuiting {@code ||} operator.
     * This means that all {@code predicates} must not have side effects.
     * They are not guaranteed to be called if the result can be evaluated earlier (short-circuiting).
     * The {@code null} predicates are ignored, i.e., treated as {@link #alwaysFalse()}.
     */
    public static @NotNull LongPredicate or(@Nullable LongPredicate @NotNull... predicates) {
        return t -> {
            for (LongPredicate predicate : predicates) {
                if (predicate != null && predicate.test(t)) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Wraps the {@link LongPredicate} {@code p} with an additional {@link Consumer} {@code c},
     * which is called before the evaluation of the predicate with the same input.
     */
    public static @NotNull LongPredicate peek(@NotNull LongConsumer c, @NotNull LongPredicate p) {
        return t -> {
            c.accept(t);
            return p.test(t);
        };
    }

    /**
     * Wraps the {@link LongConsumer} {@code c} as a {@link LongPredicate} which calls {@code c} and
     * returns {@code true}.
     */
    public static @NotNull LongPredicate peekAndReturnTrue(@NotNull LongConsumer c) {
        return t -> {
            c.accept(t);
            return true;
        };
    }

    /**
     * Wraps the {@link LongConsumer}s {@code c1} and {@code c2} as a {@link LongPredicate} which
     * calls both {@code c1} and {@code c2} in sequence and returns {@code true}.
     */
    public static @NotNull LongPredicate peekAndReturnTrue(@NotNull LongConsumer c1, @NotNull LongConsumer c2) {
        return t -> {
            c1.accept(t);
            c2.accept(t);
            return true;
        };
    }

    /**
     * Wraps the {@link LongConsumer}s as a {@link LongPredicate} which calls all {@code consumers} in sequence
     * and returns {@code true}.
     */
    public static @NotNull LongPredicate peekAndReturnTrue(@NotNull LongConsumer @NotNull... consumers) {
        return t -> {
            for (LongConsumer consumer : consumers) {
                consumer.accept(t);
            }
            return true;
        };
    }

    /**
     * Wraps the {@link LongConsumer} {@code c} as a {@link LongPredicate} which calls {@code c} and
     * returns {@code false}.
     */
    public static @NotNull LongPredicate peekAndReturnFalse(@NotNull LongConsumer c) {
        return t -> {
            c.accept(t);
            return false;
        };
    }

    /**
     * Wraps the {@link LongConsumer}s {@code c1} and {@code c2} as a {@link LongPredicate} which
     * calls both {@code c1} and {@code c2} in sequence and returns {@code false}.
     */
    public static @NotNull LongPredicate peekAndReturnFalse(@NotNull LongConsumer c1, @NotNull LongConsumer c2) {
        return t -> {
            c1.accept(t);
            c2.accept(t);
            return false;
        };
    }

    /**
     * Wraps the {@link LongConsumer}s as a {@link LongPredicate} which calls all {@code consumers} in sequence
     * and returns {@code false}.
     */
    public static @NotNull LongPredicate peekAndReturnFalse(@NotNull LongConsumer @NotNull... consumers) {
        return t -> {
            for (LongConsumer consumer : consumers) {
                consumer.accept(t);
            }
            return false;
        };
    }

    /**
     * Chains the {@link ToLongFunction} and {@link LongPredicate} (of the result type)
     * forming a higher order {@link Predicate}.
     */
    public static <T> @NotNull Predicate<T> chainObj(@NotNull ToLongFunction<T> f, @NotNull LongPredicate p) {
        return t -> p.test(f.applyAsLong(t));
    }

    /**
     * Chains the {@link LongFunction} and {@link Predicate} (of the result type)
     * forming a higher order {@link LongPredicate}.
     */
    public static <T> @NotNull LongPredicate chainLong(@NotNull LongFunction<T> f, @NotNull Predicate<T> p) {
        return t -> p.test(f.apply(t));
    }

    /**
     * Chains the {@link LongUnaryOperator} and {@link LongPredicate} (of the result type)
     * forming a higher order {@link LongPredicate}.
     */
    public static @NotNull LongPredicate chainLongs(@NotNull LongUnaryOperator op, @NotNull LongPredicate p) {
        return t -> p.test(op.applyAsLong(t));
    }
}
