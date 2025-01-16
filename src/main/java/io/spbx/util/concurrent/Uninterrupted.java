package io.spbx.util.concurrent;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.func.ThrowRunnable;
import io.spbx.util.func.ThrowSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Stateless
public class Uninterrupted {
    public static void await(@NotNull CountDownLatch latch) {
        runSilently(latch::await);
    }

    public static void await(@NotNull ExecutorService executorService) {
        runSilently(() -> executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS));
    }

    public static void wait(@NotNull Object monitor) {
        runSilently(monitor::wait);
    }

    public static void runSilently(@NotNull ThrowRunnable<InterruptedException> runnable) {
        try {
            runnable.run();
        } catch (InterruptedException ignore) {
        }
    }

    public static void runRethrow(@NotNull ThrowRunnable<InterruptedException> runnable) {
        try {
            runnable.run();
        } catch (InterruptedException e) {
            Unchecked.rethrow(e);
        }
    }

    public static <T> T getRethrow(@NotNull ThrowSupplier<T, InterruptedException> runnable) {
        try {
            return runnable.get();
        } catch (InterruptedException e) {
            return Unchecked.rethrow(e);
        }
    }
}
