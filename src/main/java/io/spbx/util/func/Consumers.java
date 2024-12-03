package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides convenient {@link Consumer}-related API.
 *
 * @see Functions
 * @see BoolFunctions
 * @see Predicates
 */
@Stateless
public class Consumers {
    /**
     * Returns a {@link Consumer} which calls both {@code first} and {@code second} in sequence.
     * Both are guaranteed to be called unless an exception is thrown.
     */
    public static <T> @NotNull Consumer<T> fanOut(@NotNull Consumer<T> first, @NotNull Consumer<T> second) {
        return t -> {
            first.accept(t);
            second.accept(t);
        };
    }

    /**
     * Returns a {@link Consumer} which calls all {@code first}, {@code second} and {@code third} in sequence.
     * All three are guaranteed to be called unless an exception is thrown.
     */
    public static <T> @NotNull Consumer<T> fanOut(
            @NotNull Consumer<T> first, @NotNull Consumer<T> second, @NotNull Consumer<T> third) {
        return t -> {
            first.accept(t);
            second.accept(t);
            third.accept(t);
        };
    }

    /**
     * Returns a {@link Consumer} which calls all {@code consumers} in sequence.
     * All consumers guaranteed to be called unless an exception is thrown.
     */
    public static @SafeVarargs <T> @NotNull Consumer<T> fanOut(@NotNull Consumer<T> @NotNull... consumers) {
        return t -> {
            for (Consumer<T> consumer : consumers) {
                consumer.accept(t);
            }
        };
    }

    /**
     * Chains the predicate {@code p} and consumer {@code c} into a new {@link Consumer}
     * which delegates to {@code c} only if {@code p} is evaluated to {@code true}.
     */
    public static <T> @NotNull Consumer<T> conditional(@NotNull Predicate<T> p, @NotNull Consumer<T> c) {
        return t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        };
    }

    /**
     * Chains the {@code condition} and consumer {@code c} into a new {@link Consumer}
     * which delegates to {@code c} only if {@code condition} is evaluated to {@code true}.
     */
    public static <T> @NotNull Consumer<T> conditional(boolean condition, @NotNull Consumer<T> c) {
        return t -> {
            if (condition) {
                c.accept(t);
            }
        };
    }

    /**
     * Wraps the {@code consumer} into a new {@link Consumer} which makes sure that
     * {@code consumer} is called only for non-null inputs.
     */
    public static <T> @NotNull Consumer<T> onlyNonNull(@NotNull Consumer<T> consumer) {
        return t -> {
            if (t != null) {
                consumer.accept(t);
            }
        };
    }

    /**
     * Chains the {@link Function} and {@link Consumer} (of the result type) forming a higher order {@link Consumer}.
     */
    public static <T, U> @NotNull Consumer<T> chain(@NotNull Function<T, U> f, @NotNull Consumer<U> c) {
        return t -> c.accept(f.apply(t));
    }
}
