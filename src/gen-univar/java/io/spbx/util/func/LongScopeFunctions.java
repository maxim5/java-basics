package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
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
@Stateless
@Generated(value = "$Type$ScopeFunctions.java", date = "2025-01-14T10:07:33.482123700Z")
public class LongScopeFunctions {
    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link LongConsumer} {@code action}.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(count(), val -> log.info().log("count=%d", count));
     * }
     */
    public static long also(long instance, @NotNull LongConsumer action) {
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
    public static long also(long instance, @NotNull Runnable action) {
        action.run();
        return instance;
    }

    /**
     * Applies the specified {@link ToLongFunction} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * The input is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(toList(), list -> list.isEmpty() ? -1 : list.size());
     * }
     */
    public static <T> long with(T instance, @NotNull ToLongFunction<T> action) {
        return action.applyAsLong(instance);
    }

    /**
     * Applies the specified {@link LongFunction} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * The result is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(toList().size(), size -> size == 0 ? -1 : size);
     * }
     */
    public static <R> R with(long instance, @NotNull LongFunction<R> action) {
        return action.apply(instance);
    }

    /**
     * Applies the specified {@link ToLongFunction} {@code action} to the input {@code instance} and returns the result
     * but only if the {@code instance} is non-null. Returns {@code 0} otherwise.
     * <p>
     * The input is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return map(toListOrNull(), list -> list.isEmpty() ? -1 : list.size());
     * }
     */
    public static <T> long map(T instance, @NotNull ToLongFunction<T> action) {
        if (instance != null) {
            return action.applyAsLong(instance);
        }
        return 0;
    }

    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link LongPredicate} {@code predicate}.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return test(count(), val -> 0 < val && val < 2);
     * }
     */
    public static boolean test(long instance, @NotNull LongPredicate predicate) {
        return predicate.test(instance);
    }
}
