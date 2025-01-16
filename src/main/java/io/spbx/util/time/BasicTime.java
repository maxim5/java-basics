package io.spbx.util.time;

import com.google.common.collect.ImmutableMap;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.str.BasicParsing;
import io.spbx.util.base.str.Regex;
import io.spbx.util.collect.map.MapBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Stateless
@Pure
@CheckReturnValue
public class BasicTime {
    private static final Pattern INT_DURATION_PATTERN = Pattern.compile("\\s*([+-]?[0-9]+)\\s*([a-zA-Z]*)\\s*");
    private static final ImmutableMap<String, ChronoUnit> CHRONO_UNITS = MapBuilder.<String, ChronoUnit>builder()
        .putMultiKeys("ns", "nano", "nanos", ChronoUnit.NANOS)
        .putMultiKeys("micro", "micros", ChronoUnit.MICROS)
        .putMultiKeys("ms", "milli", "millis", ChronoUnit.MILLIS)
        .putMultiKeys("s", "sec", "second", "seconds", ChronoUnit.SECONDS)
        .putMultiKeys("min", "minute", "minutes", ChronoUnit.MINUTES)
        .putMultiKeys("h", "hour", "hours", ChronoUnit.HOURS)
        .putMultiKeys("d", "day", "days", ChronoUnit.DAYS)
        .toGuavaImmutableMap();

    public static @Nullable Duration parseHumanDuration(@NotNull String str) {
        return Regex.on(str).matchOrNull(INT_DURATION_PATTERN, matcher -> {
            Long amount = BasicParsing.parseLongOrNull(matcher.group(1));
            ChronoUnit unit = CHRONO_UNITS.get(matcher.group(2).toLowerCase());
            return amount == null || unit == null ? null : Duration.of(amount, unit);
        });
    }

    private static final Pattern HUMAN_LOCAL_DATE_PATTERN = Pattern.compile("([0-9]+)[/-]([0-9]+)[/-]([0-9]+)");
    private static final int DEFAULT_CENTURY = 2000;
    public enum AmbiguousChoice {
        DAY_FIRST,
        MONTH_FIRST,
        NULL,
    }

    // Inspired by
    // https://stackoverflow.com/questions/7048828/how-can-i-parse-multiple-unknown-date-formats-in-python
    public static @Nullable LocalDate parseHumanLocalDate(@NotNull String str) {
        return parseHumanLocalDate(str, AmbiguousChoice.DAY_FIRST);
    }

    public static @Nullable LocalDate parseHumanLocalDate(@NotNull String str, @NotNull AmbiguousChoice choice) {
        return Regex.on(str).matchOrNull(HUMAN_LOCAL_DATE_PATTERN, matcher -> {
            Integer first = BasicParsing.parseIntegerOrNull(matcher.group(1));
            Integer second = BasicParsing.parseIntegerOrNull(matcher.group(2));
            Integer third = BasicParsing.parseIntegerOrNull(matcher.group(3));
            if (first == null || second == null || third == null) {
                return null;
            }
            int year = third < 100 ? third + DEFAULT_CENTURY : third;
            if (first > 12) {
                return LocalDate.of(year, second, first);
            }
            if (second > 12) {
                return LocalDate.of(year, first, second);
            }
            return switch (choice) {
                case DAY_FIRST -> LocalDate.of(year, second, first);
                case MONTH_FIRST -> LocalDate.of(year, first, second);
                case NULL -> null;
            };
        });
    }

    public static @NotNull String toHMS(long amount, @NotNull TimeUnit unit) {
        return toHMS(unit.toSeconds(amount));
    }

    public static @NotNull String toHMS(long seconds) {
        assert seconds >= 0 && seconds < 86400 : "Seconds are out of day bounds: " + seconds;
        long hrs = TimeUnit.SECONDS.toHours(seconds);
        long min = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        long sec = TimeUnit.SECONDS.toSeconds(seconds) % 60;
        return "%02d:%02d:%02d".formatted(hrs, min, sec);
    }

    public static long toUnit(@NotNull Duration duration, @NotNull TimeUnit unit) {
        return switch (unit) {
            case NANOSECONDS -> duration.toNanos();
            case MICROSECONDS -> duration.toNanos() / 1000;
            case MILLISECONDS -> duration.toMillis();
            case SECONDS -> duration.toSeconds();
            case MINUTES -> duration.toMinutes();
            case HOURS -> duration.toHours();
            case DAYS -> duration.toDays();
        };
    }

    public static long toUnit(@NotNull TimeUnit durationUnit, long duration, @NotNull TimeUnit unit) {
        return switch (unit) {
            case NANOSECONDS -> durationUnit.toNanos(duration);
            case MICROSECONDS -> durationUnit.toMicros(duration);
            case MILLISECONDS -> durationUnit.toMillis(duration);
            case SECONDS -> durationUnit.toSeconds(duration);
            case MINUTES -> durationUnit.toMinutes(duration);
            case HOURS -> durationUnit.toHours(duration);
            case DAYS -> durationUnit.toDays(duration);
        };
    }

    public static long toUnit(@NotNull Duration duration, @NotNull ChronoUnit unit) {
        return toUnit(duration, TimeUnit.of(unit));
    }

    public static long toUnit(@NotNull ChronoUnit durationUnit, long duration, @NotNull ChronoUnit unit) {
        return toUnit(TimeUnit.of(durationUnit), duration, TimeUnit.of(unit));
    }
}
