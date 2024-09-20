package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.LongFunction;

/**
 * Provides convenient {@link LongFunction}-related API.
 *
 * @see Functions
 */
@Generated(value = "$Type$Functions.java", date = "2024-09-20T11:07:11.302983713Z")
public class LongFunctions {
    public static <R> @NotNull LongFunction<R> constant(R value) {
        return t -> value;
    }

    public static <T, R> @NotNull LongFunction<R> chain(@NotNull LongFunction<T> f1, @NotNull Function<T, R> f2) {
        return i -> f2.apply(f1.apply(i));
    }
}
