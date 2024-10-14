package io.spbx.util.time;

import io.spbx.util.func.IntReversible;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Providers a 32-bit conversion from {@link Instant} to {@code int} and back.
 * <p>
 * Encodes seconds in the unsigned 32 bits and truncates nanoseconds.
 * <p>
 * Min supported timestamp: <code>Thu 1970-01-01 00:00:00.000000000</code>
 * Max supported timestamp: <code>Sun 2106-02-07 06:28:15.000000000</code>
 */
public class SecondsTimestamp {
    /**
     * <code>Thu 1970-01-01 00:00:00.000000000</code>
     */
    public static final Instant MIN_TIMESTAMP = seconds32ToInstant(0);

    /**
     * <code>Sun 2106-02-07 06:28:15.000000000</code>
     */
    public static final Instant MAX_TIMESTAMP = seconds32ToInstant(-1);

    @Pure
    public static int instantToSeconds32(@NotNull Instant instant) {
        return (int) instant.getEpochSecond();
    }

    @Pure
    public static @NotNull Instant seconds32ToInstant(int timestamp) {
        long epochSeconds = Integer.toUnsignedLong(timestamp);
        return Instant.ofEpochSecond(epochSeconds);
    }

    public static final IntReversible<Instant> REVERSIBLE = new IntReversible<>() {
        @Override public int forwardToInt(@NotNull Instant instant) {
            return instantToSeconds32(instant);
        }
        @Override public @NotNull Instant backward(int timestamp) {
            return seconds32ToInstant(timestamp);
        }
    };
}
