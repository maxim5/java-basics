package io.spbx.util.func;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides the functional API similar to Kotlin Scope Functions.
 *
 * @link <a href="https://kotlinlang.org/docs/scope-functions.html">Kotlin Scope Functions</a>
 * @see BoolScopeFunctions
 * @see IntScopeFunctions
 * @see LongScopeFunctions
 */
@Stateless
public class ScopeFunctions {
    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link ThrowConsumer} {@code action}.
     * <p>
     * The input (and hence the result) is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(newSortedMap(), map -> map.put(key, val));
     * }
     */
    public static <T, E extends Throwable> T also(T instance, @NotNull ThrowConsumer<T, E> action) throws E {
        action.accept(instance);
        return instance;
    }

    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link Runnable} {@code action}.
     * <p>
     * The input (and hence the result) is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return also(val, () -> val.setAccessible(true));
     * }
     */
    public static <T, E extends Throwable> T also(T instance, @NotNull ThrowRunnable<E> action) throws E {
        action.run();
        return instance;
    }

    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link ThrowConsumer} {@code action}
     * only if the {@code instance} is non-null.
     * <p>
     * The input (and hence the result) is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return let(getMapOrNull(), map -> map.put(key, val));
     * }
     */
    public static <T, E extends Throwable> @Nullable T let(@Nullable T instance, @NotNull ThrowConsumer<T, E> action) throws E {
        if (instance != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * Pipes the input {@code instance} argument through and calls the specified {@link ThrowRunnable} {@code action}
     * only if the {@code instance} is non-null.
     * <p>
     * The input (and hence the result) is nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return let(getValueOrNull(), () -> val.setAccessible(true));
     * }
     */
    public static <T, E extends Throwable> @Nullable T let(@Nullable T instance, @NotNull ThrowRunnable<E> action) throws E {
        if (instance != null) {
            action.run();
        }
        return instance;
    }

    /**
     * Applies the specified {@link ThrowFunction} {@code action} to the input {@code instance} and returns the result.
     * <p>
     * Both the input and result output are nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return with(stream.toArray(), array -> array.length == 0 ? EMPTY : new Tuple(array));
     * }
     */
    public static <T, R, E extends Throwable> R with(T instance, @NotNull ThrowFunction<T, R, E> action) throws E {
        return action.apply(instance);
    }

    /**
     * Applies the specified {@link ThrowFunction} {@code action} to the input {@code instance}
     * only if the {@code instance} is non-null. Returns {@code null} otherwise.
     * <p>
     * Both the input and result output are nullable.
     * <p>
     * Convenient for one-liners:
     * {@snippet lang = "java":
     *     return map(getValueOrNull(), value -> value.toString());
     * }
     */
    public static <T, R, E extends Throwable> @Nullable R map(@Nullable T instance, @NotNull ThrowFunction<T, R, E> action) throws E {
        if (instance != null) {
            return action.apply(instance);
        }
        return null;
    }
}
