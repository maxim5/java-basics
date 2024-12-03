package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;
import io.spbx.util.func.DoubleUnaryOperator;
import io.spbx.util.func.IntToDoubleFunction;
import io.spbx.util.func.BiDoubleIntToDoubleFunction;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code double}s.
 */
@Stateless
@Pure
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2024-12-02T15:53:08.666713400Z")
public class DoubleOps {
    public static final double ZERO = 0;

    public static final DoubleBinaryOperator DOUBLE_ADD = Double::sum;
    public static final DoubleBinaryOperator DOUBLE_MUL = (a, b) -> a * b;

    /* Range array */

    // Supports negative ranges
    public static double[] range(double end) {
        return range((double) 0, end);
    }

    // Supports negative ranges
    public static double[] range(double start, double end) {
        double[] array = new double[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    public static double[] reverse(double[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    public static double[] fill(double[] array, double val) {
        Arrays.fill(array, val);
        return array;
    }

    public static double[] fill(int len, double value) {
        return fill(new double[len], value);
    }
    public static double[] fill(int len, IntToDoubleFunction func) {
        return fill(new double[len], func);
    }

    public static double[] fill(double[] array, IntToDoubleFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToDouble(i);
        }
        return array;
    }

    /* Map array */

    public static double[] map(double[] array, DoubleUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToDouble(array[i]);
        }
        return array;
    }
    public static double[] map(double[] array, BiDoubleIntToDoubleFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToDouble(array[i], i);
        }
        return array;
    }

    /* Array search */

    public static int indexOf(double[] array, double val) {
        return indexOf(array, i -> i == val);
    }

    public static int indexOf(double[] array, DoublePredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int indexOf(double[] array, DoublePredicate check, int from, int to, int def) {
        assert RangeCheck.with(array.length, array).rangeCheck(from, to, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert RangeCheck.with(array.length, array).outOfRangeCheck(def);
        from = LowLevel.translateIndex(from, array.length);
        to = LowLevel.translateIndex(to, array.length);
        for (int i = from; i < to; ++i) {
            if (check.test(array[i])) {
                return i;
            }
        }
        return def;
    }

    public static int lastIndexOf(double[] array, double val) {
        return lastIndexOf(array, i -> i == val);
    }

    public static int lastIndexOf(double[] array, DoublePredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int lastIndexOf(double[] array, DoublePredicate check, int from, int to, int def) {
        assert RangeCheck.with(array.length, array).rangeCheck(from, to, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert RangeCheck.with(array.length, array).outOfRangeCheck(def);
        from = LowLevel.translateIndex(from, array.length);
        to = LowLevel.translateIndex(to, array.length);
        for (int i = to - 1; i >= from; --i) {
            if (check.test(array[i])) {
                return i;
            }
        }
        return def;
    }

    public static boolean contains(double[] array, double val) {
        return indexOf(array, val) >= 0;
    }

    public static boolean contains(double[] array, DoublePredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    public static double[] concat(double[] array1, double[] array2) {
        double[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static double[] append(double[] array, double val) {
        double[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    public static double[] prepend(double val, double[] array) {
        double[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    public static double[] slice(double[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    public static double[] slice(double[] array, int from) {
        return slice(array, from, array.length);
    }

    public static double[] ensureCapacity(double[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    public static int[] coerceToIntArray(double[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    public static long[] coerceToLongArray(double[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Double.BYTES;

    public static byte[] toBigEndianBytes(double[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Double.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asDoubleBuffer().put(array);
        return byteBuffer.array();
    }

    /* Java NIO buffers */

    public static double[] getDoubles(java.nio.DoubleBuffer buffer, int len) {
        double[] doubles = new double[len];
        buffer.get(doubles, 0, len);
        return doubles;
    }

    // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
    public static double[] remainingDoubles(java.nio.DoubleBuffer buffer) {
        double[] doubles = new double[buffer.remaining()];
        buffer.get(doubles, 0, doubles.length);
        return doubles;
    }

    /* Positive/non-negative number selections */

    public static double firstPositive(double a, double b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    public static double firstPositive(double a, double b, double c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    public static double firstPositive(double... nums) {
        for (double num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    public static double firstNonNegative(double a, double b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    public static double firstNonNegative(double a, double b, double c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    public static double firstNonNegative(double... nums) {
        for (double num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    public static double requirePositive(double val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    public static double requireNonNegative(double val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
