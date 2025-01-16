package io.spbx.util.collect.iter;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class Progress {
    private static final Logger log = Logger.forEnclosingClass();

    private final long total;
    private final AtomicLong count = new AtomicLong();
    private final AtomicLong startedMillis = new AtomicLong(System.currentTimeMillis());
    private final AtomicLong atMostEveryMillis = new AtomicLong();
    private final AtomicLong lastReportMillis = new AtomicLong();
    private final AtomicReference<String> name = new AtomicReference<>("");

    Progress(long total) {
        this.total = total;
    }

    public static @NotNull Progress of(long total) {
        return new Progress(total);
    }

    public static @NotNull Progress ofUnknownTotal() {
        return new Progress(-1);
    }

    @CanIgnoreReturnValue
    public @NotNull Progress resetTimer() {
        startedMillis.set(System.currentTimeMillis());
        return this;
    }

    @CanIgnoreReturnValue
    public @NotNull Progress atMostEvery(long millis) {
        atMostEveryMillis.set(millis);
        return this;
    }

    @CanIgnoreReturnValue
    public @NotNull Progress atMostEvery(long amount, @NotNull TimeUnit timeUnit) {
        return atMostEvery(timeUnit.toMillis(amount));
    }

    @CanIgnoreReturnValue
    public @NotNull Progress name(@NotNull String name) {
        this.name.set(name.isEmpty() ? name : name + " ");
        return this;
    }

    @CanIgnoreReturnValue
    public long step(long step) {
        assert step > 0 : "Invalid step: " + step;
        long taken = count.addAndGet(step);
        if (canReportNow()) {
            long millis = System.currentTimeMillis() - startedMillis.get();
            reportProgress(name.get(), total, taken, millis);
        }
        return taken;
    }

    private boolean canReportNow() {
        if (atMostEveryMillis.get() <= 0) {
            return true;
        }
        long now = System.currentTimeMillis();
        long timestamp = now - atMostEveryMillis.get();
        long lastReported = lastReportMillis.get();
        if (lastReported < timestamp) {
            return lastReportMillis.compareAndSet(lastReported, now);
        }
        return false;
    }

    private static void reportProgress(@NotNull String name, long total, long taken, long millis) {
        long elapsed = millis / 1000;
        double speed = millis == 0 ? 0.0 : 1000.0 * taken / millis;
        if (total > 0) {
            double percent = 100.0 * taken / total;
            log.info().log("Progress %s%.1f%% (%s/%s): %.2f it/sec after %s sec",
                           name, percent, taken, total, speed, elapsed);
        } else {
            log.info().log("Progress %s%s/?: %.2f it/sec after %s sec",
                           name, taken, speed, elapsed);
        }
    }

    public static <T> @NotNull Iterator<T> progressBar(@NotNull Iterator<T> iterator) {
        return progressBar(iterator, -1);
    }

    public static <T> @NotNull Iterator<T> progressBar(@NotNull Iterator<T> iterator, long total) {
        return new Iterator<>() {
            final Progress progress = Progress.of(total);
            @Override public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override public T next() {
                progress.step(1);
                return iterator.next();
            }
            @Override public void remove() {
                iterator.remove();
            }
        };
    }

    public static <T> @NotNull Iterable<T> progressBar(@NotNull Iterable<T> iterable) {
        return () -> progressBar(iterable.iterator(), BasicIterables.estimateSize(iterable, -1));
    }
}
