package io.spbx.util.base.random;

import io.spbx.util.base.annotate.NonPure;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.time.BasicTime;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

/**
 * @see Thread#sleep
 * @see java.util.concurrent.locks.LockSupport#parkNanos(long)
 */
@ThreadSafe
@NonPure
public class RandomTime {
    private final RandomGenerator random;

    RandomTime(@NotNull RandomGenerator random) {
        this.random = random;
    }

    @Pure public static @NotNull RandomTime of() {
        return RandomTime.of(0);
    }

    @Pure public static @NotNull RandomTime of(long seed) {
        return RandomTime.of(new Random(seed));
    }

    public static @NotNull RandomTime of(@NotNull RandomGenerator random) {
        return new RandomTime(random);
    }

    public static @NotNull RandomTime ofRandomSeed() {
        return RandomTime.of(new Random());
    }

    public @NotNull LocalTime nextLocalTime(@NotNull LocalTime from, @NotNull LocalTime to) {
        return LocalTime.ofNanoOfDay(random.nextLong(from.toNanoOfDay(), to.toNanoOfDay()));
    }

    public @NotNull LocalTime nextLocalTime() {
        return LocalTime.ofNanoOfDay(random.nextLong(LocalTime.MAX.toNanoOfDay()));
    }

    public long nextSeconds(@NotNull Duration max) {
        return this.nextTimeAmount(max, TimeUnit.SECONDS);
    }

    public long nextSeconds(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount) {
        return this.nextTimeAmount(maxDurationUnit, maxDurationAmount, TimeUnit.SECONDS);
    }

    public long nextMillis(@NotNull Duration max) {
        return this.nextTimeAmount(max, TimeUnit.MILLISECONDS);
    }

    public long nextMillis(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount) {
        return this.nextTimeAmount(maxDurationUnit, maxDurationAmount, TimeUnit.MILLISECONDS);
    }

    public long nextNanos(@NotNull Duration max) {
        return this.nextTimeAmount(max, TimeUnit.NANOSECONDS);
    }

    public long nextNanos(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount) {
        return this.nextTimeAmount(maxDurationUnit, maxDurationAmount, TimeUnit.NANOSECONDS);
    }

    public long nextTimeAmount(@NotNull Duration max, @NotNull TimeUnit unit) {
        return random.nextLong(BasicTime.toUnit(max, unit));
    }

    public long nextTimeAmount(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount, @NotNull TimeUnit unit) {
        return random.nextLong(BasicTime.toUnit(maxDurationUnit, maxDurationAmount, unit));
    }

    public @NotNull Duration nextTimeAmount(@NotNull Duration max) {
        return Duration.of(this.nextTimeAmount(max, TimeUnit.NANOSECONDS), ChronoUnit.NANOS);
    }

    public @NotNull Duration nextTimeAmount(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount) {
        return Duration.of(this.nextTimeAmount(maxDurationUnit, maxDurationAmount, TimeUnit.NANOSECONDS), ChronoUnit.NANOS);
    }
}
