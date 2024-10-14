package io.spbx.util.func;

class TestingFunctions {
    public static boolean isPositive(int a) {
        return a > 0;
    }

    public static boolean isPositive(long a) {
        return a > 0;
    }

    public static int negate(int a) {
        return -a;
    }

    public static long negate(long a) {
        return -a;
    }

    public static boolean and(Boolean x, Boolean y) {
        return x & y;
    }
}
