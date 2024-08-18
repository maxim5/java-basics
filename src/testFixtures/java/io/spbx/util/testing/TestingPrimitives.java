package io.spbx.util.testing;

import java.util.Random;

public class TestingPrimitives {
    public static int[] ints(int... values) {
        return values;
    }

    public static long[] longs(long... values) {
        return values;
    }

    public static double[] doubles(double... values) {
        return values;
    }

    public static byte[] bytes(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return bytes;
    }

    public static int[] randomIncreasingInts(int size, int bound) {
        return randomIncreasingInts(size, bound, 0);
    }

    public static int[] randomIncreasingInts(int size, int bound, int seed) {
        Random random = new Random(seed);
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = (i > 0 ? result[i-1] : 0) + random.nextInt(bound);
        }
        return result;
    }

    public static long[] randomIncreasingLongs(int size, long bound) {
        return randomIncreasingLongs(size, bound, 0);
    }

    public static long[] randomIncreasingLongs(int size, long bound, int seed) {
        Random random = new Random(seed);
        long[] result = new long[size];
        for (int i = 0; i < size; i++) {
            result[i] = (i > 0 ? result[i-1] : 0) + random.nextLong(bound);
        }
        return result;
    }
}
