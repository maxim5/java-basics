package io.spbx.util.time;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.func.LongReversible;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Providers a 64-bit conversion from {@link Instant} to {@code long} and back.
 * <p>
 * Encodes seconds in 34 bits (hence allows a maximum of <code>2^34 - 1</code> epoch seconds)
 * and exact nanoseconds in the remaining 30 bits.
 * <p>
 * Min supported timestamp: <code>Thu 1970-01-01 00:00:00.000000000</code>
 * Max supported timestamp: <code>Wed 2514-05-30 01:53:04.073741823</code>
 */
@Stateless
@CheckReturnValue
public class NanoTimestamp {
    /**
     * <code>Thu 1970-01-01 00:00:00.000000000</code>
     */
    public static final Instant MIN_TIMESTAMP = nanos64ToInstant(0);

    /**
     * <code>Wed 2514-05-30 01:53:04.073741823</code>
     */
    public static final Instant MAX_TIMESTAMP = nanos64ToInstant(-1);

    @Pure public static long instantToNanos64(@NotNull Instant instant) {
        long epochSecond = instant.getEpochSecond();
        int nano = instant.getNano();
        return (epochSecond << 30) + nano;
    }

    @Pure public static @NotNull Instant nanos64ToInstant(long timestamp) {
        long epochSeconds = timestamp >>> 30;
        int nanos = (int) (timestamp & 0x3fff_ffffL);  // 0b0011_1111_1111_1111_1111_1111_1111_1111L, 30 bits
        return Instant.ofEpochSecond(epochSeconds, nanos);
    }

    public static final LongReversible<Instant> REVERSIBLE = new LongReversible<>() {
        @Override public long forwardToLong(@NotNull Instant instant) {
            return instantToNanos64(instant);
        }
        @Override public @NotNull Instant backward(long timestamp) {
            return nanos64ToInstant(timestamp);
        }
    };
}
