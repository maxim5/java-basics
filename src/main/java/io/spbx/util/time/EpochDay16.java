package io.spbx.util.time;

import com.google.common.collect.Range;
import io.spbx.util.func.Reversible;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * Provides 16-bit conversions from {@link LocalDate} to a {@code short} integer and back.
 * <p>
 * Supported date ranges for 16-bit conversion are the following.
 *
 * <ul>
 *     <li> {@link EpochDay16#DEFAULT}: <code>1970-01-01 ... 2149-06-06</code>. </li>
 *     <li> {@link EpochDay16#MILLENNIAL}: <code>2000-01-01 ... 2179-06-06</code>. </li>
 *     <li> {@link EpochDay16#XX_CENTURY}: <code>1900-01-01 ... 2079-06-06</code>. </li>
 * </ul>
 */
public enum EpochDay16 {
    /** <code>1970-01-01 ... 2149-06-06</code> */
    DEFAULT(Epoch.DEFAULT),
    /** <code>2000-01-01 ... 2179-06-06</code> */
    MILLENNIAL(Epoch.MILLENNIAL),
    /** <code>1900-01-01 ... 2079-06-06</code> */
    XX_CENTURY(Epoch.XX_CENTURY);

    private final long epochStartInDays;

    EpochDay16(@NotNull Epoch epoch) {
        this.epochStartInDays = epoch.timestamp().toLocalDateTime().toLocalDate().toEpochDay();
    }

    public @NotNull LocalDate minDate() {
        return epochDay16ToLocalDate((short) 0);
    }

    public @NotNull LocalDate maxDate() {
        return epochDay16ToLocalDate((short) -1);
    }

    public @NotNull Range<LocalDate> range() {
        return Range.closed(minDate(), maxDate());
    }

    public @NotNull Reversible<LocalDate, Short> reversible() {
        return new Reversible<>() {
            @Override public @NotNull Short forward(@NotNull LocalDate date) {
                return localDateToEpochDay16(date);
            }
            @Override public @NotNull LocalDate backward(@NotNull Short epochDay) {
                return epochDay16ToLocalDate(epochDay);
            }
        };
    }

    @Pure
    public short localDateToEpochDay16(@NotNull LocalDate localDate) {
        return (short) (localDate.toEpochDay() - epochStartInDays);
    }

    @Pure
    public @NotNull LocalDate epochDay16ToLocalDate(short epochDay) {
        return LocalDate.ofEpochDay(epochStartInDays + Short.toUnsignedInt(epochDay));
    }
}
