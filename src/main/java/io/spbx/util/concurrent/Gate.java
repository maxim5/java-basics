package io.spbx.util.concurrent;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.logging.Logger;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides the stop-and-go semantics: one thread to stop and continue only when the another one permits.
 * Example usage:
 * {@snippet lang = "java":
 *    Gate gate = new Gate();
 *    window.addWindowListener(new WindowAdapter() {
 *        @Override public void windowClosed(WindowEvent e) {
 *            gate.release();
 *        }
 *    });
 *    gate.stop();    // blocks the thread unless `.release()` has already been called, otherwise continues
 * }
 * <p>
 * The {@code Gate} instances are not reusable, all methods should be called once.
 */
@ThreadSafe
@CanIgnoreReturnValue
public class Gate {
    private static final Logger log = Logger.forEnclosingClass();

    private final AtomicBoolean open = new AtomicBoolean(false);
    private final Semaphore semaphore = new Semaphore(0);

    public boolean stop() {
        if (open.get()) {
            log.warn().log("Gate is already open (attempt to stop)");
            return false;
        }
        assert !semaphore.hasQueuedThreads() : "Must not have queued threads";
        semaphore.acquireUninterruptibly();
        assert open.get() : "Gate is still closed";
        return true;
    }

    public boolean release() {
        if (!open.compareAndSet(false, true)) {
            log.warn().log("Gate is already open (attempt to release)");
            return false;
        }
        if (semaphore.hasQueuedThreads()) {
            semaphore.release();
            return true;
        }
        log.warn().log("Gate is released before the threads are queued");
        return false;
    }
}
