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
 * Encodes seconds in 44 bits (hence allows a maximum of <code>2^44 - 1</code> epoch seconds)
 * and microseconds in the remaining 20 bits.
 * In other words, truncates the {@link Instant} to {@link java.util.concurrent.TimeUnit#MICROSECONDS}.
 * <p>
 * Min supported timestamp: <code>-276768-11-27 19:09:52.000000</code>
 * Max supported timestamp: <code>+280707-02-04 04:50:08.048575</code>
 */
@Stateless
@CheckReturnValue
public class MicroTimestamp {
    @Pure public static long instantToMicros64(@NotNull Instant instant) {
        long epochSecond = instant.getEpochSecond();
        int nano = instant.getNano();
        return (epochSecond << 20) + (nano / 1000);
    }

    @Pure public static @NotNull Instant micros64ToInstant(long timestamp) {
        long epochSeconds = timestamp >> 20;
        int micros = (int) (timestamp & 0x000f_ffffL);  // 0b1111_1111_1111_1111_1111L, 20 bits
        return Instant.ofEpochSecond(epochSeconds, micros * 1000);
    }

    public static final LongReversible<Instant> REVERSIBLE = new LongReversible<>() {
        @Override public long forwardToLong(@NotNull Instant instant) {
            return instantToMicros64(instant);
        }
        @Override public @NotNull Instant backward(long timestamp) {
            return micros64ToInstant(timestamp);
        }
    };
}
