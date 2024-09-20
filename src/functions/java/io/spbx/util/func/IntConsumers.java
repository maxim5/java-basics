package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Provides convenient {@link IntConsumer}-related API.
 *
 * @see Consumers
 */
@Generated(value = "$Type$Consumers.java", date = "2024-09-18T16:02:26.716685443Z")
public class IntConsumers {
    public static @NotNull IntConsumer chain(@NotNull IntPredicate p, @NotNull IntConsumer c) {
        return t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        };
    }

    public static @NotNull IntConsumer fanOut(@NotNull IntConsumer first, @NotNull IntConsumer second) {
        return first.andThen(second);
    }

    public static @NotNull IntConsumer fanOut(
            @NotNull IntConsumer first, @NotNull IntConsumer second, @NotNull IntConsumer third) {
        return first.andThen(second).andThen(third);
    }
}
