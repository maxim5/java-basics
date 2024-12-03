package io.spbx.util.testing.concurrent;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.spbx.util.base.error.Unchecked.Runnables.runRethrow;

public class TraceExecutor implements ExecutorService {
    private static final Logger log = Logger.forEnclosingClass();

    private final ExecutorService delegate;
    private final List<Future<?>> futures = Collections.synchronizedList(new ArrayList<>());

    public TraceExecutor(@NotNull ExecutorService delegate) {
        this.delegate = delegate;
    }

    public static @NotNull TraceExecutor wrap(@NotNull ExecutorService delegate) {
        return new TraceExecutor(delegate);
    }

    public @NotNull List<Future<?>> futures() {
        return ImmutableList.copyOf(futures);
    }

    public boolean isAllDone() {
        return futures.stream().allMatch(Future::isDone);
    }

    public boolean isExecuting() {
        return !isAllDone();
    }

    public boolean isAnyCancelled() {
        return futures.stream().anyMatch(Future::isCancelled);
    }

    public void waitForAllDone() {
        runRethrow(() -> {
            for (Future<?> future : futures) {
                future.get();
            }
        });
    }

    public void waitForAllDone(long timeout, TimeUnit unit) {
        runRethrow(() -> {
            for (Future<?> future : futures) {
                future.get(timeout, unit);
            }
        });
    }

    @CanIgnoreReturnValue
    public boolean evictCompletedTasks() {
        return futures.removeIf(Future::isDone);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        log.warn().log("Task %s submitted via execute() is not traceable");
        delegate.execute(command);
    }

    @Override
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
        return save(delegate.submit(task));
    }

    @Override
    public <T> @NotNull Future<T> submit(@NotNull Runnable task, T result) {
        return save(delegate.submit(task, result));
    }

    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        return save(delegate.submit(task));
    }

    @Override
    public <T> @NotNull List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        return saveAll(delegate.invokeAll(tasks));
    }

    @Override
    public <T> @NotNull List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks,
                                                  long timeout,
                                                  @NotNull TimeUnit unit) throws InterruptedException {
        return saveAll(delegate.invokeAll(tasks, timeout, unit));
    }

    @Override
    public <T> @Nullable T invokeAny(@NotNull Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return delegate.invokeAny(tasks);
    }

    @Override
    public <T> @Nullable T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(tasks, timeout, unit);
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public @NotNull List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public void close() {
        delegate.close();
    }

    private <T> @NotNull Future<T> save(@NotNull Future<T> future) {
        futures.add(future);
        return future;
    }

    private <T> @NotNull List<Future<T>> saveAll(@NotNull List<Future<T>> list) {
        futures.addAll(list);
        return list;
    }
}
