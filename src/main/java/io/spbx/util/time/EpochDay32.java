package io.spbx.util.time;

import io.spbx.util.func.Reversible;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * Provides a 16-bit and 32-bit conversions from {@link LocalDate} to {@code int} and back.
 * <p>
 * Supported date range for 32-bit conversion: <code>-5877641-06-23 ... +5881580-07-11</code>.
 */
public class EpochDay32 {
    @Pure
    public static int localDateToEpochDay32(@NotNull LocalDate localDate) {
        return (int) localDate.toEpochDay();
    }

    @Pure
    public static @NotNull LocalDate epochDay32ToLocalDate(int epochDay) {
        return LocalDate.ofEpochDay(epochDay);
    }

    public static final Reversible<LocalDate, Integer> REVERSIBLE = new Reversible<>() {
        @Override public @NotNull Integer forward(@NotNull LocalDate date) {
            return localDateToEpochDay32(date);
        }
        @Override public @NotNull LocalDate backward(@NotNull Integer epochDay) {
            return epochDay32ToLocalDate(epochDay);
        }
    };
}
