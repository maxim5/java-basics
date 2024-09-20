package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

/**
 * Provides convenient {@link LongConsumer}-related API.
 *
 * @see Consumers
 */
@Generated(value = "$Type$Consumers.java", date = "2024-09-20T11:07:11.299179299Z")
public class LongConsumers {
    public static @NotNull LongConsumer chain(@NotNull LongPredicate p, @NotNull LongConsumer c) {
        return t -> {
            if (p.test(t)) {
                c.accept(t);
            }
        };
    }

    public static @NotNull LongConsumer fanOut(@NotNull LongConsumer first, @NotNull LongConsumer second) {
        return first.andThen(second);
    }

    public static @NotNull LongConsumer fanOut(
            @NotNull LongConsumer first, @NotNull LongConsumer second, @NotNull LongConsumer third) {
        return first.andThen(second).andThen(third);
    }
}
