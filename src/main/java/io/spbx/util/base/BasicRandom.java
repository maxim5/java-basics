package io.spbx.util.base;

import io.spbx.util.collect.BasicStreams;
import io.spbx.util.time.BasicTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.random.RandomGenerator;

public class BasicRandom {
    @ThreadSafe
    public static class RandomArrays {
        private final RandomGenerator random;

        RandomArrays(@NotNull RandomGenerator random) {
            this.random = random;
        }

        public static @NotNull RandomArrays of() {
            return RandomArrays.of(0);
        }

        public static @NotNull RandomArrays of(long seed) {
            return RandomArrays.of(new Random(seed));
        }

        public static @NotNull RandomArrays of(@NotNull RandomGenerator random) {
            return new RandomArrays(random);
        }

        public static @NotNull RandomArrays ofRandomSeed() {
            return RandomArrays.of(new Random());
        }

        public int[] nextInts(int len) {
            return randomInts(random::nextInt, len);
        }

        public long[] nextLongs(int len) {
            return randomLongs(random::nextLong, len);
        }

        public static int[] randomInts(@NotNull IntSupplier random, int len) {
            int[] array = new int[len];
            for (int i = 0; i < array.length; i++) {
                array[i] = random.getAsInt();
            }
            return array;
        }

        public static long[] randomLongs(@NotNull LongSupplier random, int len) {
            long[] array = new long[len];
            for (int i = 0; i < array.length; i++) {
                array[i] = random.getAsLong();
            }
            return array;
        }

        public static <T> T randomItem(@NotNull RandomGenerator random, @Nullable T @NotNull[] array) {
            return array[random.nextInt(array.length)];
        }

        public static int randomItem(@NotNull RandomGenerator random, int[] array) {
            return array[random.nextInt(array.length)];
        }

        public static long randomItem(@NotNull RandomGenerator random, long[] array) {
            return array[random.nextInt(array.length)];
        }

        public static byte randomItem(@NotNull RandomGenerator random, byte[] array) {
            return array[random.nextInt(array.length)];
        }

        public static short randomItem(@NotNull RandomGenerator random, short[] array) {
            return array[random.nextInt(array.length)];
        }

        public static char randomItem(@NotNull RandomGenerator random, char[] array) {
            return array[random.nextInt(array.length)];
        }
    }

    @ThreadSafe
    public static class RandomStrings {
        public static final Chars DIGITS = Chars.of("0123456789");
        public static final Chars ASCII_LOWER_CASE = Chars.of("abcdefghijklmnopqrstuvwxyz");
        public static final Chars ASCII_UPPER_CASE = Chars.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        public static final Chars ASCII_LETTERS = Chars.of(ASCII_LOWER_CASE, ASCII_UPPER_CASE);
        public static final Chars ASCII_DIGITS_LOWER_CASE = Chars.of(DIGITS, ASCII_LOWER_CASE);
        public static final Chars ASCII_DIGITS_UPPER_CASE = Chars.of(DIGITS, ASCII_UPPER_CASE);
        public static final Chars ASCII_DIGITS_LETTERS = Chars.of(DIGITS, ASCII_LOWER_CASE, ASCII_UPPER_CASE);

        private final RandomGenerator random;

        RandomStrings(@NotNull RandomGenerator random) {
            this.random = random;
        }

        public static @NotNull RandomStrings of() {
            return RandomStrings.of(0);
        }

        public static @NotNull RandomStrings of(long seed) {
            return RandomStrings.of(new Random(seed));
        }

        public static @NotNull RandomStrings of(@NotNull RandomGenerator random) {
            return new RandomStrings(random);
        }

        public static @NotNull RandomStrings ofRandomSeed() {
            return RandomStrings.of(new Random());
        }

        public @NotNull String next(@NotNull Chars chars, int len) {
            return randomString(random, chars, len);
        }

        public @NotNull String next(int len) {
            return randomString(random, ASCII_LOWER_CASE, len);
        }

        public static @NotNull String randomString(@NotNull RandomGenerator random, @NotNull Chars chars, int len) {
            StringBuilder builder = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                builder.append(RandomArrays.randomItem(random, chars.chars));
            }
            return builder.toString();
        }

        @Immutable
        public static class Chars {
            private final char[] chars;

            Chars(char @NotNull[] chars) {
                this.chars = chars;
            }

            public int length() {
                return this.chars.length;
            }

            public static @NotNull Chars of(@NotNull String str) {
                return new Chars(str.toCharArray());
            }

            public static @NotNull Chars of(@NotNull String @NotNull... strings) {
                int capacity = BasicStreams.streamOf(strings).mapToInt(String::length).sum();
                StringBuilder builder = new StringBuilder(capacity);
                for (String str : strings) {
                    builder.append(str);
                }
                return Chars.of(builder.toString());
            }

            public static @NotNull Chars of(@NotNull Chars chars) {
                return new Chars(chars.chars);
            }

            public static @NotNull Chars of(@NotNull Chars @NotNull... chars) {
                int capacity = BasicStreams.streamOf(chars).mapToInt(Chars::length).sum();
                StringBuilder builder = new StringBuilder(capacity);
                for (Chars ch : chars) {
                    builder.append(ch.chars);
                }
                return Chars.of(builder.toString());
            }

            @Override public String toString() {
                return String.valueOf(chars);
            }
        }
    }

    /**
     * @see Thread#sleep
     * @see java.util.concurrent.locks.LockSupport#parkNanos(long)
     */
    @ThreadSafe
    public static class RandomTime {
        private final RandomGenerator random;

        RandomTime(@NotNull RandomGenerator random) {
            this.random = random;
        }

        public static @NotNull RandomTime of() {
            return RandomTime.of(0);
        }

        public static @NotNull RandomTime of(long seed) {
            return RandomTime.of(new Random(seed));
        }

        public static @NotNull RandomTime of(@NotNull RandomGenerator random) {
            return new RandomTime(random);
        }

        public static @NotNull RandomTime ofRandomSeed() {
            return RandomTime.of(new Random());
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
}
