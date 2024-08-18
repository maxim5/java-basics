package io.spbx.util.time;

import io.spbx.util.func.Reversible;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Providers a 32-bit conversion from {@link LocalTime} to {@code int} and back.
 * <p>
 * Encodes millis within a day in 32 bits (a maximum of <code>86`400`000</code>).
 * In other words, truncates the {@link LocalTime} to {@link java.util.concurrent.TimeUnit#MILLISECONDS}.
 */
public class MillisDaytime {
    @Pure
    public static int localDateTimeToMillis32(@NotNull LocalDateTime localDateTime) {
        return localTimeToMillis32(localDateTime.toLocalTime());
    }

    @Pure
    public static int localTimeToMillis32(@NotNull LocalTime localTime) {
        long nanoOfDay = localTime.toNanoOfDay();
        return (int) (nanoOfDay / 1_000_000);  // Max value: 86_400_000 (millis in a day)
    }

    @Pure
    public static @NotNull LocalTime millis32ToLocalTime(int millis) {
        long nanoOfDay = millis * 1_000_000L;
        return LocalTime.ofNanoOfDay(nanoOfDay);
    }

    public static final Reversible<LocalTime, Integer> REVERSIBLE = new Reversible<>() {
        @Override public @NotNull Integer forward(@NotNull LocalTime localTime) {
            return localTimeToMillis32(localTime);
        }
        @Override public @NotNull LocalTime backward(@NotNull Integer millis) {
            return millis32ToLocalTime(millis);
        }
    };
}
