package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
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
@Stateless
@Generated(value = "$Type$ScopeFunctions.java", date = "2024-12-02T15:53:08.761582400Z")
public class IntScopeFunctions {
    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link IntConsumer} {@code action}.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(count(), val -> log.info().log("count=%d", count));
     * }
     */
    public static int also(int instance, @NotNull IntConsumer action) {
        action.accept(instance);
        return instance;
    }

    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link Runnable} {@code action}.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(count(), () -> log.info().log("Completed"));
     * }
     */
    public static int also(int instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    /**
     * Applies the specified {@link ToIntFunction} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * The input is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(toList(), list -> list.isEmpty() ? -1 : list.size());
     * }
     */
    public static <T> int with(T instance, @NotNull ToIntFunction<T> action) {
        return action.applyAsInt(instance);
    }

    /**
     * Applies the specified {@link IntFunction} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * The result is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(toList().size(), size -> size == 0 ? -1 : size);
     * }
     */
    public static <R> R with(int instance, @NotNull IntFunction<R> action) {
        return action.apply(instance);
    }

    /**
     * Applies the specified {@link ToIntFunction} {@code action} to the input {@code instance} and returns the result
     * but only if the {@code instance} is non-null. Returns {@code 0} otherwise.
     * <p>
     * The input is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return map(toListOrNull(), list -> list.isEmpty() ? -1 : list.size());
     * }
     */
    public static <T> int map(T instance, @NotNull ToIntFunction<T> action) {
        if (instance != null) {
            return action.applyAsInt(instance);
        }
        return 0;
    }
}
