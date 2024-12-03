package io.spbx.util.testing.concurrent;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.func.ThrowRunnable;
import org.jetbrains.annotations.NotNull;

@Stateless
@Pure
@CheckReturnValue
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
}
