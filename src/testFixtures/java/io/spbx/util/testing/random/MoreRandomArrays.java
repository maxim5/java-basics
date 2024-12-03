package io.spbx.util.testing.random;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Random;
import java.util.random.RandomGenerator;

@ThreadSafe
public class MoreRandomArrays {
    private final RandomGenerator random;

    MoreRandomArrays(@NotNull RandomGenerator random) {
        this.random = random;
    }

    public static @NotNull MoreRandomArrays of() {
        return MoreRandomArrays.of(0);
    }

    public static @NotNull MoreRandomArrays of(long seed) {
        return MoreRandomArrays.of(new Random(seed));
    }

    public static @NotNull MoreRandomArrays of(@NotNull RandomGenerator random) {
        return new MoreRandomArrays(random);
    }

    public static @NotNull MoreRandomArrays ofRandomSeed() {
        return MoreRandomArrays.of(new Random());
    }

    public int[] nextIncreasingInts(int len, int bound) {
        return randomIncreasingInts(random, len, bound);
    }

    public long[] nextIncreasingLongs(int len, long bound) {
        return randomIncreasingLongs(random, len, bound);
    }

    public static int[] randomIncreasingInts(@NotNull RandomGenerator random, int len, int bound) {
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = (i > 0 ? result[i - 1] : 0) + random.nextInt(bound);
        }
        return result;
    }

    public static long[] randomIncreasingLongs(@NotNull RandomGenerator random, int len, long bound) {
        long[] result = new long[len];
        for (int i = 0; i < len; i++) {
            result[i] = (i > 0 ? result[i - 1] : 0) + random.nextLong(bound);
        }
        return result;
    }
}
