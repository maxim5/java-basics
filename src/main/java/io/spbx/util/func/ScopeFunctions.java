package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides the functional API similar to Kotlin Scope Functions.
 *
 * @link <a href="https://kotlinlang.org/docs/scope-functions.html">Kotlin Scope Functions</a>
 * @see BoolScopeFunctions
 * @see IntScopeFunctions
 * @see LongScopeFunctions
 */
public class ScopeFunctions {
    public static <T> T alsoApply(T instance, @NotNull Consumer<T> action) {
        action.accept(instance);
        return instance;
    }

    public static <T> T alsoRun(T instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    public static <T, R> R with(T instance, @NotNull Function<T, R> action) {
        return action.apply(instance);
    }
}
