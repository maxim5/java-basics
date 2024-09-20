package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides the functional API similar to Kotlin Scope Functions, specialized for {@code boolean} values.
 *
 * @link <a href="https://kotlinlang.org/docs/scope-functions.html">Kotlin Scope Functions</a>
 * @see ScopeFunctions
 */
public class BoolScopeFunctions {
    public static boolean alsoApply(boolean instance, @NotNull Consumer<Boolean> action) {
        action.accept(instance);
        return instance;
    }

    public static boolean alsoRun(boolean instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    public static <T> boolean with(T instance, @NotNull Predicate<T> action) {
        return action.test(instance);
    }

    public static <R> R with(boolean instance, @NotNull Function<Boolean, R> action) {
        return action.apply(instance);
    }
}
