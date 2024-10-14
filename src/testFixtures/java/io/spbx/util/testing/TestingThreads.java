package io.spbx.util.testing;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.spbx.util.func.ThrowRunnable;
import org.jetbrains.annotations.NotNull;

public class TestingThreads {
    public static void startAll(@NotNull Thread @NotNull... threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static void joinAll(@NotNull Thread @NotNull... threads) {
        for (Thread thread : threads) {
            runSilently(thread::join);
        }
    }

    public static void runSilently(@NotNull ThrowRunnable<InterruptedException> runnable) {
        try {
            runnable.run();
        } catch (InterruptedException ignore) {
        }
    }

    public interface ThreadWorker extends Runnable {
        @CheckReturnValue
        default @NotNull Thread newThread() {
            return new Thread(this);
        }

        @CanIgnoreReturnValue
        default @NotNull Thread startInNewThread() {
            Thread thread = newThread();
            thread.start();
            return thread;
        }
    }
}
