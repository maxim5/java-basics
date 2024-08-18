package io.spbx.util.time;

import com.google.common.collect.Range;
import io.spbx.util.func.Reversible;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;

/**
 * Provides 8-bit conversions from an integer human year to a {@code byte} integer.
 * <p>
 * Each value covers a span of 255 years, e.g.
 * {@link #DEFAULT_SIGNED} supports years in <code>1842 ... 2097</code> range and
 * {@link #MILLENNIAL_UNSIGNED} supports years in <code>2000 ... 2255</code> range.
 */
public enum EpochYear8 {
    /** <code>1842 ... 2097</code> */
    DEFAULT_SIGNED(Epoch.DEFAULT, true),
    /** <code>1970 ... 2225</code> */
    DEFAULT_UNSIGNED(Epoch.DEFAULT, false),
    /** <code>1872 ... 2127</code> */
    MILLENNIAL_SIGNED(Epoch.MILLENNIAL, true),
    /** <code>2000 ... 2255</code> */
    MILLENNIAL_UNSIGNED(Epoch.MILLENNIAL, false),
    /** <code>1772 ... 2027</code> */
    XX_CENTURY_SIGNED(Epoch.XX_CENTURY, true),
    /** <code>1900 ... 2155</code> */
    XX_CENTURY_UNSIGNED(Epoch.XX_CENTURY, false);

    private final int epochStartYear;
    private final boolean isSigned;

    EpochYear8(@NotNull Epoch epoch, boolean isSigned) {
        this.epochStartYear = epoch.timestamp().toLocalDateTime().getYear();
        this.isSigned = isSigned;
    }

    public int minYear() {
        return epochYear8ToHumanYear(isSigned ? Byte.MIN_VALUE : (byte) 0);
    }

    public int maxYear() {
        return epochYear8ToHumanYear(isSigned ? Byte.MAX_VALUE : (byte) -1);
    }

    public @NotNull Range<Integer> range() {
        return Range.closed(minYear(), maxYear());
    }

    public @NotNull Reversible<Integer, Byte> reversible() {
        return new Reversible<>() {
            @Override public @NotNull Byte forward(@NotNull Integer humanYear) {
                return humanYearToEpochYear8(humanYear);
            }
            @Override public @NotNull Integer backward(@NotNull Byte epochYear) {
                return epochYear8ToHumanYear(epochYear);
            }
        };
    }

    @Pure
    public byte humanYearToEpochYear8(int humanYear) {
        return (byte) (humanYear - epochStartYear);
    }

    @Pure
    public int epochYear8ToHumanYear(byte epochYear) {
        return epochStartYear + (isSigned ? (int) epochYear : Byte.toUnsignedInt(epochYear));
    }
}
