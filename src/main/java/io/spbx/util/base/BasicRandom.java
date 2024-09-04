package io.spbx.util.base;

import io.spbx.util.time.BasicTime;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BasicRandom {
    /**
     * @see Thread#sleep
     * @see java.util.concurrent.locks.LockSupport#parkNanos(long)
     */
    public static class TimeRandom extends Random {
        public TimeRandom() {
            super();
        }

        public TimeRandom(long seed) {
            super(seed);
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
            return this.nextLong(BasicTime.toUnit(max, unit));
        }

        public long nextTimeAmount(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount, @NotNull TimeUnit unit) {
            return this.nextLong(BasicTime.toUnit(maxDurationUnit, maxDurationAmount, unit));
        }

        public @NotNull Duration nextTimeAmount(@NotNull Duration max) {
            return Duration.of(this.nextTimeAmount(max, TimeUnit.NANOSECONDS), ChronoUnit.NANOS);
        }

        public @NotNull Duration nextTimeAmount(@NotNull TimeUnit maxDurationUnit, int maxDurationAmount) {
            return Duration.of(this.nextTimeAmount(maxDurationUnit, maxDurationAmount, TimeUnit.NANOSECONDS), ChronoUnit.NANOS);
        }
    }
}
