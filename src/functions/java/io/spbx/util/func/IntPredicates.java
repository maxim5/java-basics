package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Provides convenient {@link IntPredicate}-related API.
 *
 * @see Predicates
 */
@Generated(value = "$Type$Predicates.java", date = "2024-09-20T11:07:11.293556362Z")
public class IntPredicates {
    public static final IntPredicate TRUE = t -> true;
    public static final IntPredicate FALSE = t -> false;

    public static @NotNull IntPredicate constant(boolean value) {
        return t -> value;
    }

    public static @NotNull IntPredicate equalTo(int t) {
        return value -> value == t;
    }

    public static @NotNull IntPredicate chain(@NotNull IntConsumer c, @NotNull IntPredicate p) {
        return t -> {
            c.accept(t);
            return p.test(t);
        };
    }
}
