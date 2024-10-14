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
 * @see IntScopeFunctions
 * @see LongScopeFunctions
 */
public class BoolScopeFunctions {
    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link Consumer} {@code action}.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(isEmpty(), state -> log.info().log("isEmpty=%s", state));
     * }
     */
    public static boolean also(boolean instance, @NotNull Consumer<Boolean> action) {
        action.accept(instance);
        return instance;
    }

    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link Runnable} {@code action}.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(isEmpty(), () -> log.info().log("Completed"));
     * }
     */
    public static boolean also(boolean instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    /**
     * Applies the specified {@link Predicate} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * The input is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(toList(), list -> list.isEmpty() || list.contains(null));
     * }
     */
    public static <T> boolean with(T instance, @NotNull Predicate<T> action) {
        return action.test(instance);
    }

    /**
     * Applies the specified {@link Function} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * The result is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(toList().isEmpty(), state -> checkFoo(state) && checkBar(state) ? -1 : 1);
     * }
     */
    public static <R> R with(boolean instance, @NotNull Function<Boolean, R> action) {
        return action.apply(instance);
    }

    /**
     * Applies the specified {@link Predicate} {@code action} to the input {@code instance} and returns the result
     * but only if the {@code instance} is non-null. Returns {@code false} otherwise.
     * <p>
     * The input is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return map(toListOrNull(), list -> list.isEmpty() || list.contains(null));
     * }
     */
    public static <T> boolean map(T instance, @NotNull Predicate<T> action) {
        if (instance != null) {
            return action.test(instance);
        }
        return false;
    }
}
