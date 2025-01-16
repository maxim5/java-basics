package io.spbx.util.concurrent;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.func.ThrowSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

@Stateless
public class Tasks {
    public static <T> @NotNull Callable<T> toCallable(@NotNull Runnable runnable) {
        return toCallable(runnable, null);
    }

    public static <T> @NotNull Callable<T> toCallable(@NotNull Runnable runnable, @Nullable T result) {
        return () -> {
            runnable.run();
            return result;
        };
    }

    public static <T> @NotNull Callable<T> toCallable(@NotNull Supplier<T> supplier) {
        return supplier::get;
    }

    public static <T, E extends Exception> @NotNull Callable<T> toCallable(@NotNull ThrowSupplier<T, E> supplier) {
        return supplier::get;
    }

    public static <T> @NotNull Runnable toRunnable(@NotNull Callable<T> callable) {
        return Unchecked.Runnables.rethrow(callable::call);
    }

    public static <T> @NotNull Runnable toRunnable(@NotNull Supplier<T> supplier) {
        return supplier::get;
    }

    public static <T, E extends Exception> @NotNull Runnable toRunnable(@NotNull ThrowSupplier<T, E> supplier) {
        return Unchecked.Runnables.rethrow(supplier::get);
    }
}
