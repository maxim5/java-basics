package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

/**
 * Provides convenient {@link IntConsumer}-related API.
 *
 * @see Consumers
 */
@Stateless
@Generated(value = "$Type$Consumers.java", date = "2024-12-02T15:53:08.740577500Z")
public class IntConsumers {
    /**
     * Returns a {@link IntConsumer} which calls both {@code first} and {@code second} in sequence.
     * Both are guaranteed to be called unless an exception is thrown.
     */
    public static @NotNull IntConsumer fanOut(@NotNull IntConsumer first, @NotNull IntConsumer second) {
        return t -> {
            first.accept(t);
            second.accept(t);
        };
    }

    /**
     * Returns a {@link IntConsumer} which calls all {@code first}, {@code second} and {@code third} in sequence.
     * All three are guaranteed to be called unless an exception is thrown.
     */
    public static @NotNull IntConsumer fanOut(
            @NotNull IntConsumer first, @NotNull IntConsumer second, @NotNull IntConsumer third) {
        return t -> {
            first.accept(t);
            second.accept(t);
            third.accept(t);
        };
    }

    /**
     * Returns a {@link IntConsumer} which calls all {@code consumers} in sequence.
     * All consumers guaranteed to be called unless an exception is thrown.
     */
    public static @NotNull IntConsumer fanOut(@NotNull IntConsumer @NotNull... consumers) {
        return t -> {
            for (IntConsumer consumer : consumers) {
                consumer.accept(t);
            }
        };
    }

    /**
     * Chains the predicate {@code p} and consumer {@code c} into a new {@link IntConsumer}
     * which delegates to {@code c} only if {@code p} is evaluated to {@code true}.
     */
    public static @NotNull IntConsumer conditional(@NotNull IntPredicate p, @NotNull IntConsumer c) {
        return t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        };
    }

    /**
     * Chains the {@code condition} and consumer {@code c} into a new {@link IntConsumer}
     * which delegates to {@code c} only if {@code condition} is evaluated to {@code true}.
     */
    public static @NotNull IntConsumer conditional(boolean condition, @NotNull IntConsumer c) {
        return t -> {
            if (condition) {
                c.accept(t);
            }
        };
    }

    /**
     * Chains the {@link ToIntFunction} and {@link IntConsumer} (of the result type)
     * forming a higher order {@link Consumer}.
     */
    public static <T> @NotNull Consumer<T> chainObj(@NotNull ToIntFunction<T> op, @NotNull IntConsumer c) {
        return t -> c.accept(op.applyAsInt(t));
    }

    /**
     * Chains the {@link IntFunction} and {@link Consumer} (of the result type)
     * forming a higher order {@link IntConsumer}.
     */
    public static <T> @NotNull IntConsumer chainInt(@NotNull IntFunction<T> f, @NotNull Consumer<T> c) {
        return t -> c.accept(f.apply(t));
    }

    /**
     * Chains the {@link IntUnaryOperator} and {@link IntConsumer} (of the result type)
     * forming a higher order {@link IntConsumer}.
     */
    public static @NotNull IntConsumer chainInts(@NotNull IntUnaryOperator op, @NotNull IntConsumer c) {
        return t -> c.accept(op.applyAsInt(t));
    }
}
