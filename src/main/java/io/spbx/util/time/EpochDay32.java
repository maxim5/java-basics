package io.spbx.util.time;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.func.IntReversible;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * Provides a 16-bit and 32-bit conversions from {@link LocalDate} to {@code int} and back.
 * <p>
 * Supported date range for 32-bit conversion: <code>-5877641-06-23 ... +5881580-07-11</code>.
 */
@Stateless
@CheckReturnValue
public class EpochDay32 {
    @Pure public static int localDateToEpochDay32(@NotNull LocalDate localDate) {
        return (int) localDate.toEpochDay();
    }

    @Pure public static @NotNull LocalDate epochDay32ToLocalDate(int epochDay) {
        return LocalDate.ofEpochDay(epochDay);
    }

    public static final IntReversible<LocalDate> REVERSIBLE = new IntReversible<>() {
        @Override public int forwardToInt(@NotNull LocalDate date) {
            return localDateToEpochDay32(date);
        }
        @Override public @NotNull LocalDate backward(int epochDay) {
            return epochDay32ToLocalDate(epochDay);
        }
    };
}
