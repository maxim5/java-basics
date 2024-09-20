package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Provides convenient {@link Consumer}-related API.
 */
public class Consumers {
    public static <T> @NotNull Consumer<T> chain(@NotNull Predicate<T> p, @NotNull Consumer<T> c) {
        return t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        };
    }

    public static <T> @NotNull Consumer<T> fanOut(@NotNull Consumer<T> first, @NotNull Consumer<T> second) {
        return first.andThen(second);
    }

    public static <T> @NotNull Consumer<T> fanOut(
            @NotNull Consumer<T> first, @NotNull Consumer<T> second, @NotNull Consumer<T> third) {
        return first.andThen(second).andThen(third);
    }
}
