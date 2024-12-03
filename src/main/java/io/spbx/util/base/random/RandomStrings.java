package io.spbx.util.base.random;

import io.spbx.util.base.annotate.NonPure;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.lang.IntLength;
import io.spbx.util.collect.stream.BasicStreams;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Random;
import java.util.random.RandomGenerator;

@ThreadSafe
@NonPure
public class RandomStrings {
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

    @Pure public static @NotNull RandomStrings of() {
        return RandomStrings.of(0);
    }

    @Pure public static @NotNull RandomStrings of(long seed) {
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
    public static class Chars implements IntLength {
        private final char[] chars;

        Chars(char @NotNull [] chars) {
            this.chars = chars;
        }

        @Override
        public int length() {
            return this.chars.length;
        }

        public static @NotNull Chars of(@NotNull String str) {
            return new Chars(str.toCharArray());
        }

        public static @NotNull Chars of(@NotNull String @NotNull ... strings) {
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

        public static @NotNull Chars of(@NotNull Chars @NotNull ... chars) {
            int capacity = BasicStreams.streamOf(chars).mapToInt(Chars::length).sum();
            StringBuilder builder = new StringBuilder(capacity);
            for (Chars ch : chars) {
                builder.append(ch.chars);
            }
            return Chars.of(builder.toString());
        }

        @Override
        public String toString() {
            return String.valueOf(chars);
        }
    }
}
