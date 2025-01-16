package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;

/**
 * Provides convenient {@link LongConsumer}-related API.
 *
 * @see Consumers
 */
@Stateless
@Generated(value = "$Type$Consumers.java", date = "2025-01-14T10:07:33.463119500Z")
public class LongConsumers {
    /**
     * Returns a {@link LongConsumer} which calls both {@code first} and {@code second} in sequence.
     * Both are guaranteed to be called unless an exception is thrown.
     */
    public static @NotNull LongConsumer fanOut(@NotNull LongConsumer first, @NotNull LongConsumer second) {
        return t -> {
            first.accept(t);
            second.accept(t);
        };
    }

    /**
     * Returns a {@link LongConsumer} which calls all {@code first}, {@code second} and {@code third} in sequence.
     * All three are guaranteed to be called unless an exception is thrown.
     */
    public static @NotNull LongConsumer fanOut(
            @NotNull LongConsumer first, @NotNull LongConsumer second, @NotNull LongConsumer third) {
        return t -> {
            first.accept(t);
            second.accept(t);
            third.accept(t);
        };
    }

    /**
     * Returns a {@link LongConsumer} which calls all {@code consumers} in sequence.
     * All consumers guaranteed to be called unless an exception is thrown.
     */
    public static @NotNull LongConsumer fanOut(@NotNull LongConsumer @NotNull... consumers) {
        return t -> {
            for (LongConsumer consumer : consumers) {
                consumer.accept(t);
            }
        };
    }

    /**
     * Chains the predicate {@code p} and consumer {@code c} into a new {@link LongConsumer}
     * which delegates to {@code c} only if {@code p} is evaluated to {@code true}.
     */
    public static @NotNull LongConsumer conditional(@NotNull LongPredicate p, @NotNull LongConsumer c) {
        return t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        };
    }

    /**
     * Chains the {@code condition} and consumer {@code c} into a new {@link LongConsumer}
     * which delegates to {@code c} only if {@code condition} is evaluated to {@code true}.
     */
    public static @NotNull LongConsumer conditional(boolean condition, @NotNull LongConsumer c) {
        return t -> {
            if (condition) {
                c.accept(t);
            }
        };
    }

    /**
     * Chains the {@link ToLongFunction} and {@link LongConsumer} (of the result type)
     * forming a higher order {@link Consumer}.
     */
    public static <T> @NotNull Consumer<T> chainObj(@NotNull ToLongFunction<T> op, @NotNull LongConsumer c) {
        return t -> c.accept(op.applyAsLong(t));
    }

    /**
     * Chains the {@link LongFunction} and {@link Consumer} (of the result type)
     * forming a higher order {@link LongConsumer}.
     */
    public static <T> @NotNull LongConsumer chainLong(@NotNull LongFunction<T> f, @NotNull Consumer<T> c) {
        return t -> c.accept(f.apply(t));
    }

    /**
     * Chains the {@link LongUnaryOperator} and {@link LongConsumer} (of the result type)
     * forming a higher order {@link LongConsumer}.
     */
    public static @NotNull LongConsumer chainLongs(@NotNull LongUnaryOperator op, @NotNull LongConsumer c) {
        return t -> c.accept(op.applyAsLong(t));
    }
}
