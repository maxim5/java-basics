package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * @link <a href="https://kotlinlang.org/docs/scope-functions.html">Kotlin Scope Functions</a>
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

    public static class Ints {
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

    public static class Longs {
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

    public static class Bools {
        public static <T> boolean with(T instance, @NotNull Predicate<T> action) {
            return action.test(instance);
        }
    }
}
