package io.spbx.util.base;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.BasicExceptions.IllegalArgumentExceptions;
import io.spbx.util.base.BasicExceptions.IllegalStateExceptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.spbx.util.base.BasicParsing.parseLongSafe;

@Immutable
public final class DataSize implements Comparable<DataSize> {
    public static final long KB = Unit.Kilobyte.toBytes();
    public static final long MB = Unit.Megabyte.toBytes();
    public static final long GB = Unit.Gigabyte.toBytes();
    public static final long TB = Unit.Terabyte.toBytes();
    public static final long PB = Unit.Petabyte.toBytes();
    public static final long EB = Unit.Exabyte.toBytes();

    private final Int128 bytes;

    private DataSize(@NotNull Int128 bytes) {
        this.bytes = bytes;
    }

    public static @NotNull DataSize of(long num, @NotNull Unit unit) {
        return new DataSize(Int128.from(num).shiftLeft(unit.scale()));
    }

    public static @NotNull DataSize ofBytes(long num) { return DataSize.of(num, Unit.Byte); }
    public static @NotNull DataSize ofKilobytes(long num) { return DataSize.of(num, Unit.Kilobyte); }
    public static @NotNull DataSize ofMegabytes(long num) { return DataSize.of(num, Unit.Megabyte); }
    public static @NotNull DataSize ofGigabytes(long num) { return DataSize.of(num, Unit.Gigabyte); }
    public static @NotNull DataSize ofTerabytes(long num) { return DataSize.of(num, Unit.Terabyte); }
    public static @NotNull DataSize ofPetabytes(long num) { return DataSize.of(num, Unit.Petabyte); }
    public static @NotNull DataSize ofExabytes(long num) { return DataSize.of(num, Unit.Exabyte); }

    public static @NotNull DataSize of(double num, @NotNull Unit unit) { return DataSize.of((long) num, unit); }
    public static @NotNull DataSize ofBytes(double num) { return DataSize.of((long) num, Unit.Byte); }
    public static @NotNull DataSize ofKilobytes(double num) { return DataSize.of((long) num, Unit.Kilobyte); }
    public static @NotNull DataSize ofMegabytes(double num) { return DataSize.of((long) num, Unit.Megabyte); }
    public static @NotNull DataSize ofGigabytes(double num) { return DataSize.of((long) num, Unit.Gigabyte); }
    public static @NotNull DataSize ofTerabytes(double num) { return DataSize.of((long) num, Unit.Terabyte); }
    public static @NotNull DataSize ofPetabytes(double num) { return DataSize.of((long) num, Unit.Petabyte); }
    public static @NotNull DataSize ofExabytes(double num) { return DataSize.of((long) num, Unit.Exabyte); }

    public @NotNull Int128 toUnit(@NotNull Unit unit) {
        return bytes.shiftRight(unit.scale());
    }

    public long toBytes() { return toBytes128().longValueExact(); }
    public long toKilobytes() { return toKilobytes128().longValueExact(); }
    public long toMegabytes() { return toMegabytes128().longValueExact(); }
    public long toGigabytes() { return toGigabytes128().longValueExact(); }
    public long toTerabytes() { return toTerabytes128().longValueExact(); }
    public long toPetabytes() { return toPetabytes128().longValueExact(); }
    public long toExabytes() { return toExabytes128().longValueExact(); }

    public @NotNull Int128 toBytes128() { return bytes; }
    public @NotNull Int128 toKilobytes128() { return toUnit(Unit.Kilobyte); }
    public @NotNull Int128 toMegabytes128() { return toUnit(Unit.Megabyte); }
    public @NotNull Int128 toGigabytes128() { return toUnit(Unit.Gigabyte); }
    public @NotNull Int128 toTerabytes128() { return toUnit(Unit.Terabyte); }
    public @NotNull Int128 toPetabytes128() { return toUnit(Unit.Petabyte); }
    public @NotNull Int128 toExabytes128() { return toUnit(Unit.Exabyte); }

    @Override
    public int compareTo(@NotNull DataSize other) {
        return Int128.compare(this.bytes, other.bytes);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof DataSize dataSize && Objects.equals(this.bytes, dataSize.bytes);
    }

    @Override
    public int hashCode() {
        return bytes.hashCode();
    }

    public @NotNull String toString(@NotNull Unit unit) {
        if (bytes.numberOfTrailingZeros() >= unit.scale()) {
            return "%s%s".formatted(this.toUnit(unit), unit.suffix());
        }

        double amount = bytes.doubleValue() / unit.toDoubleBytes();
        long round = (long) amount;
        if (round > 0 && isClose(amount, round)) {
            return "%s%s".formatted(round, unit.suffix());
        }

        if (Math.abs(amount) < 2 || Math.abs(amount - round) < 1e-3) {
            return "%.3f%s".formatted(amount, unit.suffix());
        }
        return "%.2f%s".formatted(amount, unit.suffix());
    }

    public @NotNull String toStringInBytes() { return toString(Unit.Byte); }
    public @NotNull String toStringInKilobytes() { return toString(Unit.Kilobyte); }
    public @NotNull String toStringInMegabytes() { return toString(Unit.Megabyte); }
    public @NotNull String toStringInGigabytes() { return toString(Unit.Gigabyte); }
    public @NotNull String toStringInTerabytes() { return toString(Unit.Terabyte); }
    public @NotNull String toStringInPetabytes() { return toString(Unit.Petabyte); }
    public @NotNull String toStringInExabytes() { return toString(Unit.Exabyte); }

    @Override
    public String toString() {
        int trailingZeros = bytes.numberOfTrailingZeros();
        int log2 = 128 - bytes.abs().numberOfLeadingZeros();
        Unit unit = pickBestReadableUnit(trailingZeros, log2);
        return toString(unit);
    }

    private static @NotNull Unit pickBestReadableUnit(int trailingZeros, int log2) {
        if (trailingZeros < 10 && log2 < 15) {
            return Unit.Byte;
        }
        if (trailingZeros < 20 && log2 < 25) {
            return Unit.Kilobyte;
        }
        if (trailingZeros < 30 && log2 < 35) {
            return Unit.Megabyte;
        }
        if (trailingZeros < 40 && log2 < 45) {
            return Unit.Gigabyte;
        }
        return Stream.of(Unit.values())
            .filter(unit -> unit.scale() <= log2)
            .max(Comparator.comparingInt(Unit::scale))
            .orElse(Unit.Byte);
    }

    public static @NotNull DataSize parse(@NotNull CharSequence str) {
        return parse(str, Unit.Byte);
    }

    public static @NotNull DataSize parse(@NotNull CharSequence str, @NotNull Unit defaultUnit) {
        return BasicNulls.firstNonNull(List.of(
            () -> parseInt(str, defaultUnit),
            () -> parseFloat(str, defaultUnit),
            () -> IllegalArgumentExceptions.fail("Failed to parse the size: `%s`", str)
        ));
    }

    private static final Pattern INT_SIZE = Pattern.compile("\\s*([+-]?[0-9]+)\\s*([a-zA-Z]*)\\s*");
    private static final Pattern FLOAT_SIZE = Pattern.compile("\\s*([+-]?[0-9.]+(e[+-]?[0-9]+)?)\\s*([a-zA-Z]*)\\s*");

    private static @Nullable DataSize parseInt(@NotNull CharSequence str, @NotNull Unit defaultUnit) {
        Matcher matcher = INT_SIZE.matcher(str);
        if (matcher.matches()) {
            long amount = parseLongSafe(matcher.group(1), Long.MIN_VALUE);
            Unit unit = findUnit(matcher.group(2), defaultUnit);
            return amount == Long.MIN_VALUE ? null : DataSize.of(amount, unit);
        }
        return null;
    }

    private static @Nullable DataSize parseFloat(@NotNull CharSequence str, @NotNull Unit defaultUnit) {
        Matcher matcher = FLOAT_SIZE.matcher(str);
        if (matcher.matches()) {
            double amount = BasicParsing.parseDoubleSafe(matcher.group(1), Double.NEGATIVE_INFINITY);
            Unit unit = findUnit(matcher.group(3), defaultUnit);
            return amount == Double.NEGATIVE_INFINITY ? null : ofDoubleAmount(amount, unit);
        }
        return null;
    }

    private static @NotNull DataSize ofDoubleAmount(double amount, @NotNull Unit unit) {
        long round = (long) amount;
        if (isClose(amount, round)) {
            return DataSize.of(round, unit);
        }

        double totalInDouble = amount * unit.toDoubleBytes();
        if (totalInDouble >= Long.MIN_VALUE && totalInDouble <= Long.MAX_VALUE) {
            return DataSize.of((long) totalInDouble, Unit.Byte);
        }

        // Assume `amount` has not more than 20 meaningful bits (~6 decimal digits)
        return new DataSize(Int128.from(amount * (1L << 40)).shiftLeft(unit.scale() - 40));
    }

    private static final double EPSILON = 1e-7;

    private static boolean isClose(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    @VisibleForTesting
    static @NotNull Unit findUnit(@NotNull String suffix, @NotNull Unit def) {
        return suffix.isEmpty() ?
            def :
            IllegalArgumentExceptions.assureNonNull(Unit.UNITS.get(suffix.toLowerCase()), "Suffix not found: %s", suffix);
    }

    // From https://en.wikipedia.org/wiki/Byte#Multiple-byte_units
    // All values are treated as binary (1024^n instead of 1000^n).
    public enum Unit {
        Byte(0, "b"),
        Kilobyte(10, "Kb", "Kib", "K"),
        Megabyte(20, "Mb", "Mib", "M"),
        Gigabyte(30, "Gb", "Gib", "G"),
        Terabyte(40, "Tb", "Tib", "T"),
        Petabyte(50, "Pb", "Pib", "P"),
        Exabyte(60, "Eb", "Eib"),
        Zettabyte(70, "Zb", "Zib"),
        Yottabyte(80, "Yb", "Yib"),
        Ronnabyte(90, "Rb", "Rib"),
        Quettabyte(100, "Qb", "Qib");

        private final int scale;
        private final List<String> suffixes;

        private static final Map<String, Unit> UNITS = Stream.of(values())
            .flatMap(unit -> unit.suffixes().stream().map(suffix -> Pair.of(suffix.toLowerCase(), unit)))
            .collect(Collectors.toMap(Pair::first, Pair::second));

        Unit(int scale, @NotNull String @NotNull ... suffixes) {
            this.scale = scale;
            this.suffixes = List.of(suffixes);
        }

        public int scale() {
            return scale;
        }

        public @NotNull List<String> suffixes() {
            return suffixes;
        }

        public @NotNull String suffix() {
            return suffixes.getFirst();
        }

        public long toBytes() {
            return scale <= 60 ? 1L << scale : IllegalStateExceptions.fail("The value is too large for long: " + this);
        }

        public double toDoubleBytes() {
            return scale <= 60 ? 1L << scale : (1L << (scale - 60)) * 1152921504606846976.0 /* = 2^60 */;
        }

        public @NotNull DataSize toDataSize(long num) {
            return DataSize.of(num, this);
        }
    }
}
