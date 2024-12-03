package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import io.spbx.util.func.FloatBinaryOperator;
import io.spbx.util.func.FloatPredicate;
import io.spbx.util.func.FloatUnaryOperator;
import io.spbx.util.func.IntToFloatFunction;
import io.spbx.util.func.BiFloatIntToFloatFunction;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code float}s.
 */
@Stateless
@Pure
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2024-12-02T15:53:08.666713400Z")
public class FloatOps {
    public static final float ZERO = 0;

    public static final FloatBinaryOperator FLOAT_ADD = Float::sum;
    public static final FloatBinaryOperator FLOAT_MUL = (a, b) -> a * b;

    /* Range array */

    // Supports negative ranges
    public static float[] range(float end) {
        return range((float) 0, end);
    }

    // Supports negative ranges
    public static float[] range(float start, float end) {
        float[] array = new float[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    public static float[] reverse(float[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            float tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    public static float[] fill(float[] array, float val) {
        Arrays.fill(array, val);
        return array;
    }

    public static float[] fill(int len, float value) {
        return fill(new float[len], value);
    }
    public static float[] fill(int len, IntToFloatFunction func) {
        return fill(new float[len], func);
    }

    public static float[] fill(float[] array, IntToFloatFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToFloat(i);
        }
        return array;
    }

    /* Map array */

    public static float[] map(float[] array, FloatUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToFloat(array[i]);
        }
        return array;
    }
    public static float[] map(float[] array, BiFloatIntToFloatFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToFloat(array[i], i);
        }
        return array;
    }

    /* Array search */

    public static int indexOf(float[] array, float val) {
        return indexOf(array, i -> i == val);
    }

    public static int indexOf(float[] array, FloatPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int indexOf(float[] array, FloatPredicate check, int from, int to, int def) {
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

    public static int lastIndexOf(float[] array, float val) {
        return lastIndexOf(array, i -> i == val);
    }

    public static int lastIndexOf(float[] array, FloatPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int lastIndexOf(float[] array, FloatPredicate check, int from, int to, int def) {
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

    public static boolean contains(float[] array, float val) {
        return indexOf(array, val) >= 0;
    }

    public static boolean contains(float[] array, FloatPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    public static float[] concat(float[] array1, float[] array2) {
        float[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static float[] append(float[] array, float val) {
        float[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    public static float[] prepend(float val, float[] array) {
        float[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    public static float[] slice(float[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    public static float[] slice(float[] array, int from) {
        return slice(array, from, array.length);
    }

    public static float[] ensureCapacity(float[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    public static int[] coerceToIntArray(float[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    public static long[] coerceToLongArray(float[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    public static double[] coerceToDoubleArray(float[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Float.BYTES;

    public static byte[] toBigEndianBytes(float[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Float.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asFloatBuffer().put(array);
        return byteBuffer.array();
    }

    /* Java NIO buffers */

    public static float[] getFloats(java.nio.FloatBuffer buffer, int len) {
        float[] floats = new float[len];
        buffer.get(floats, 0, len);
        return floats;
    }

    // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
    public static float[] remainingFloats(java.nio.FloatBuffer buffer) {
        float[] floats = new float[buffer.remaining()];
        buffer.get(floats, 0, floats.length);
        return floats;
    }

    /* Positive/non-negative number selections */

    public static float firstPositive(float a, float b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    public static float firstPositive(float a, float b, float c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    public static float firstPositive(float... nums) {
        for (float num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    public static float firstNonNegative(float a, float b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    public static float firstNonNegative(float a, float b, float c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    public static float firstNonNegative(float... nums) {
        for (float num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    public static float requirePositive(float val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    public static float requireNonNegative(float val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
