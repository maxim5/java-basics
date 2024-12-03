package io.spbx.util.testing.concurrent;

import com.google.common.util.concurrent.Futures;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DirectExecutor implements ExecutorService {
    private static final Logger log = Logger.forEnclosingClass();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static @NotNull DirectExecutor newInstance() {
        return new DirectExecutor();
    }

    @Override
    public void execute(@NotNull Runnable command) {
        command.run();
    }

    @Override
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
        try {
            T value = task.call();
            return Futures.immediateFuture(value);
        } catch (Throwable e) {
            return Futures.immediateFailedFuture(e);
        }
    }

    @Override
    public <T> @NotNull Future<T> submit(@NotNull Runnable task, T result) {
        try {
            task.run();
            return Futures.immediateFuture(result);
        } catch (Throwable e) {
            return Futures.immediateFailedFuture(e);
        }
    }

    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        return submit(task, null);
    }

    @Override
    public <T> @NotNull List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::submit).toList();
    }

    @Override
    public <T> @NotNull List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks,
                                                  long timeout,
                                                  @NotNull TimeUnit unit) {
        return invokeAll(tasks);
    }

    @Override
    public <T> @Nullable T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::submit).filter(Future::isDone).map(Futures::getUnchecked).findFirst().orElse(null);
    }

    @Override
    public <T> @Nullable T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) {
        return invokeAny(tasks);
    }

    @Override
    public boolean isShutdown() {
        return closed.get();
    }

    @Override
    public boolean isTerminated() {
        return closed.get();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) {
        shutdown();
        return true;
    }

    @Override
    public void shutdown() {
        closed.set(true);
    }

    @Override
    public @NotNull List<Runnable> shutdownNow() {
        shutdown();
        return List.of();
    }

    @Override
    public void close() {
        shutdown();
    }
}
