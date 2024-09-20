package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

/**
 * Provides convenient {@link LongPredicate}-related API.
 *
 * @see Predicates
 */
@Generated(value = "$Type$Predicates.java", date = "2024-09-20T11:07:11.293556362Z")
public class LongPredicates {
    public static final LongPredicate TRUE = t -> true;
    public static final LongPredicate FALSE = t -> false;

    public static @NotNull LongPredicate constant(boolean value) {
        return t -> value;
    }

    public static @NotNull LongPredicate equalTo(long t) {
        return value -> value == t;
    }

    public static @NotNull LongPredicate chain(@NotNull LongConsumer c, @NotNull LongPredicate p) {
        return t -> {
            c.accept(t);
            return p.test(t);
        };
    }
}
