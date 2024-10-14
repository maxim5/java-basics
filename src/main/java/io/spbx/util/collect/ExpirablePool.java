package io.spbx.util.collect;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.BasicNulls;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.concurrent.ThreadSafe;
import java.time.Instant;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@ThreadSafe
public class ExpirablePool<T> {
    private static final Logger log = Logger.forEnclosingClass();

    private final Map<T, ObjectData> working;
    private final Map<T, ObjectData> expired;
    private Iterator<Map.Entry<T, ObjectData>> rotation;

    public ExpirablePool(@NotNull Iterable<T> objects) {
        this.working = Streamer.of(objects).zipRight(ObjectData::newCleanData).toOrderedMap();
        this.expired = BasicMaps.newMutableMap(BasicIterables.sizeOf(objects, 16));
        this.rotation = this.working.entrySet().iterator();
    }

    public static <T> @NotNull ExpirablePool<T> of(@NotNull Iterable<T> objects) {
        return new ExpirablePool<>(objects);
    }

    public static @SafeVarargs <T> @NotNull ExpirablePool<T> of(@NotNull T @NotNull ... objects) {
        return new ExpirablePool<>(ImmutableList.copyOf(objects));
    }

    public synchronized int total() {
        return working.size() + expired.size();
    }

    public synchronized int working() {
        return working.size();
    }

    public synchronized int taken() {
        return (int) working.values().stream().filter(ObjectData::isTaken).count();
    }

    public synchronized int available() {
        return (int) working.values().stream().filter(ObjectData::isFree).count();
    }

    public synchronized boolean hasWorking() {
        return !working.isEmpty();
    }

    public synchronized boolean hasAvailable() {
        return working.values().stream().anyMatch(ObjectData::isFree);
    }

    public synchronized @NotNull List<T> allWorking() {
        return ImmutableList.copyOf(working.keySet());
    }

    @VisibleForTesting
    synchronized @NotNull List<T> allAvailable() {
        return working.entrySet().stream().filter(entry -> entry.getValue().isFree()).map(Map.Entry::getKey).toList();
    }

    public synchronized @NotNull T nextAvailable() {
        IllegalStateExceptions.assure(hasWorking(), "No more working objects in the pool:", this);
        try {
            while (rotation.hasNext()) {
                Map.Entry<T, ObjectData> entry = rotation.next();
                if (entry.getValue().isFree()) {
                    entry.getValue().take();
                    return entry.getKey();
                }
            }
            IllegalStateExceptions.assure(hasAvailable(), "All working objects in the pool are taken:", this);
        } catch (NoSuchElementException | ConcurrentModificationException ignore) {}
        rotation = working.entrySet().iterator();
        return nextAvailable();
    }

    public void returnBackSuccess(@NotNull T object, @NotNull ExpirationInfo info) {
        returnBack(object, Status.WORKING, info);
    }

    public void returnBackSuccess(@NotNull T object) {
        returnBack(object, Status.WORKING, NO_INFO);
    }

    public void returnBackExpired(@NotNull T object, @NotNull ExpirationInfo info) {
        returnBack(object, Status.EXPIRED, info);
    }

    public void returnBackExpired(@NotNull T object) {
        returnBack(object, Status.EXPIRED, NO_INFO);
    }

    public synchronized void returnBack(@NotNull T object, @NotNull Status status, @NotNull ExpirationInfo info) {
        ObjectData data = working.get(object);
        if (data == null) {
            log.warn().log("Object expired concurrently: %s", object);
            return;
        }

        data.free();
        data.updateFrom(status, info);
        if (status == Status.EXPIRED) {
            working.remove(object);
            ObjectData existing = expired.put(object, data);
            assert existing == null : "Object data collision for object: " + object;
        }
    }

    public synchronized @NotNull ObjectStats getStats(@NotNull T object) {
        return BasicNulls.firstNonNull(working.get(object), () -> expired.get(object)).toStats();
    }

    static class ObjectData {
        private Status status = Status.WORKING;
        private ExpirationInfo info = NO_INFO;
        private int callsInThisSession = 0;
        private long lastCallMillis = 0;
        private boolean taken = false;

        public static @NotNull ObjectData newCleanData() {
            return new ObjectData();
        }

        public boolean isWorking() {
            return status == Status.WORKING;
        }

        public boolean isTaken() {
            return taken;
        }

        public boolean isFree() {
            return !taken;
        }

        public void take() {
            assert isFree() : "Already taken: " + this;
            assert isWorking() : "Object expired: " + this;
            taken = true;
        }

        public void free() {
            assert isTaken() : "Already free: " + this;
            taken = false;
        }

        public void updateFrom(@NotNull Status status, @NotNull ExpirationInfo info) {
            this.status = status;
            this.info = info;
            this.callsInThisSession++;
            this.lastCallMillis = System.currentTimeMillis();
        }

        public @NotNull ObjectStats toStats() {
            return new ObjectStats(status, info, callsInThisSession, lastCallMillis, taken);
        }

        @Override
        public String toString() {
            return "ObjectData{status=%s, info=%s, callsInThisSession=%d, lastCallMillis=%d, taken=%s}"
                .formatted(status, info, callsInThisSession, lastCallMillis, taken);
        }
    }

    public record ObjectStats(@NotNull Status status,
                              @NotNull ExpirationInfo expirationInfo,
                              int callsInThisSession,
                              long lastCallMillis,
                              boolean taken) {
    }

    public enum Status {
        WORKING,
        EXPIRED,
    }

    public static final ExpirationInfo NO_INFO = new ExpirationInfo(-1, null, null);

    public static @NotNull ExpirationInfo callsLeftKnown(int callsLeft) {
        return new ExpirationInfo(callsLeft, null, null);
    }

    public static @NotNull ExpirationInfo expirationKnown(@NotNull Instant expiration) {
        return new ExpirationInfo(-1, expiration, null);
    }

    public static @NotNull ExpirationInfo refreshKnown(@NotNull Instant refresh) {
        return new ExpirationInfo(-1, null, refresh);
    }

    public record ExpirationInfo(int callsLeft, @Nullable Instant expiration, @Nullable Instant refresh) {
    }
}
