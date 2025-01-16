package io.spbx.util.random;

import io.spbx.util.base.annotate.NonPure;
import io.spbx.util.base.annotate.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Random;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

@ThreadSafe
@NonPure
public class RandomArrays {
    private final RandomGenerator random;

    RandomArrays(@NotNull RandomGenerator random) {
        this.random = random;
    }

    @Pure public static @NotNull RandomArrays of() {
        return RandomArrays.of(0);
    }

    @Pure public static @NotNull RandomArrays of(long seed) {
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
        for (int i = 0; i < len; i++) {
            array[i] = random.getAsInt();
        }
        return array;
    }

    public static long[] randomLongs(@NotNull LongSupplier random, int len) {
        long[] array = new long[len];
        for (int i = 0; i < len; i++) {
            array[i] = random.getAsLong();
        }
        return array;
    }

    public static <T> T @NotNull[] randomArray(@NotNull Supplier<T> random, T @NotNull[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = random.get();
        }
        return array;
    }

    public static <T> T randomItem(@NotNull RandomGenerator random, @Nullable T @NotNull [] array) {
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
