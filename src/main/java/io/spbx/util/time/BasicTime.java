package io.spbx.util.time;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class BasicTime {
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
