package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Provides convenient {@link IntFunction}-related API.
 *
 * @see Functions
 */
@Generated(value = "$Type$Functions.java", date = "2024-09-18T16:02:26.718445406Z")
public class IntFunctions {
    public static <R> @NotNull IntFunction<R> constant(R value) {
        return t -> value;
    }

    public static <T, R> @NotNull IntFunction<R> chain(@NotNull IntFunction<T> f1, @NotNull Function<T, R> f2) {
        return i -> f2.apply(f1.apply(i));
    }
}
