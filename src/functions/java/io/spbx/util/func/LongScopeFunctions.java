package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

/**
 * Provides the functional API similar to Kotlin Scope Functions, specialized for {@code long} values.
 *
 * @link <a href="https://kotlinlang.org/docs/scope-functions.html">Kotlin Scope Functions</a>
 * @see ScopeFunctions
 */
@Generated(value = "$Type$ScopeFunctions.java", date = "2024-09-18T16:02:26.720074721Z")
public class LongScopeFunctions {
    public static long alsoApply(long instance, @NotNull LongConsumer action) {
        action.accept(instance);
        return instance;
    }

    public static long alsoRun(long instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    public static <T> long with(T instance, @NotNull ToLongFunction<T> action) {
        return action.applyAsLong(instance);
    }

    public static <R> R with(long instance, @NotNull LongFunction<R> action) {
        return action.apply(instance);
    }
}
