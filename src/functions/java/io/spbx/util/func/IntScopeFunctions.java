package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

/**
 * Provides the functional API similar to Kotlin Scope Functions, specialized for {@code int} values.
 *
 * @link <a href="https://kotlinlang.org/docs/scope-functions.html">Kotlin Scope Functions</a>
 * @see ScopeFunctions
 */
@Generated(value = "$Type$ScopeFunctions.java", date = "2024-09-20T11:07:11.307434954Z")
public class IntScopeFunctions {
    public static int alsoApply(int instance, @NotNull IntConsumer action) {
        action.accept(instance);
        return instance;
    }

    public static int alsoRun(int instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    public static <T> int with(T instance, @NotNull ToIntFunction<T> action) {
        return action.applyAsInt(instance);
    }

    public static <R> R with(int instance, @NotNull IntFunction<R> action) {
        return action.apply(instance);
    }
}
