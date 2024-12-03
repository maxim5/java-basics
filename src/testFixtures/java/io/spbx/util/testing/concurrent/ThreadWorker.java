package io.spbx.util.testing.concurrent;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

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
