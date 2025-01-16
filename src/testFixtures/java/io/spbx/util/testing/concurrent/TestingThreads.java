package io.spbx.util.testing.concurrent;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.concurrent.Uninterrupted;
import org.jetbrains.annotations.NotNull;

@Stateless
@CheckReturnValue
public class TestingThreads {
    public static void startAll(@NotNull Thread @NotNull... threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static void joinAll(@NotNull Thread @NotNull... threads) {
        for (Thread thread : threads) {
            Uninterrupted.runSilently(thread::join);
        }
    }
}
