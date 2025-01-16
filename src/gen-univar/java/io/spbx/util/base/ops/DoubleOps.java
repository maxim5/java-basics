package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import io.spbx.util.func.DoubleUnaryOperator;
import io.spbx.util.func.IntToDoubleFunction;
import io.spbx.util.func.BiDoubleIntPredicate;
import io.spbx.util.func.BiDoubleIntToDoubleFunction;

import io.spbx.util.base.annotate.AllowPythonIndexing;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.InPlace;
import io.spbx.util.base.annotate.NonPure;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.PyIndex;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code double}s.
 */
@Stateless
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2025-01-14T10:07:33.401105400Z")
public class DoubleOps {
    public static final double ZERO = 0;
    public static final double[] EMPTY_ARRAY = new double[0];

    public static final DoubleBinaryOperator DOUBLE_ADD = Double::sum;
    public static final DoubleBinaryOperator DOUBLE_MUL = (a, b) -> a * b;

    /* Range array */

    // Supports negative ranges
    @Pure
    public static double[] range(double end) {
        return range((double) 0, end);
    }

    // Supports negative ranges
    @Pure
    public static double[] range(double start, double end) {
        double[] array = new double[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    @NonPure
    public static double[] reverse(@InPlace double[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    @NonPure
    public static double[] fill(@InPlace double[] array, double val) {
        Arrays.fill(array, val);
        return array;
    }

    @Pure
    public static double[] fill(int len, double value) {
        return fill(new double[len], value);
    }
    @Pure
    public static double[] fill(int len, IntToDoubleFunction func) {
        return fill(new double[len], func);
    }

    @NonPure
    public static double[] fill(@InPlace double[] array, IntToDoubleFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToDouble(i);
        }
        return array;
    }

    /* Map array */

    @Pure
    public static double[] map(double[] array, DoubleUnaryOperator func) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToDouble(array[i]);
        }
        return result;
    }
    @Pure
    public static double[] map(double[] array, BiDoubleIntToDoubleFunction func) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToDouble(array[i], i);
        }
        return result;
    }

    @NonPure
    public static double[] mapInPlace(@InPlace double[] array, DoubleUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToDouble(array[i]);
        }
        return array;
    }
    @NonPure
    public static double[] mapInPlace(@InPlace double[] array, BiDoubleIntToDoubleFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToDouble(array[i], i);
        }
        return array;
    }

    /* Filter array */

    @Pure
    public static double[] filter(double[] array, DoublePredicate predicate) {
        int n = array.length, j = 0;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            double val = array[i];
            if (predicate.test(val)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }
    public static double[] filter(double[] array, BiDoubleIntPredicate predicate) {
        int n = array.length, j = 0;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            double val = array[i];
            if (predicate.test(val, i)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }

    @NonPure
    public static int filterInPlace(@InPlace double[] array, DoublePredicate predicate) {
        int n = array.length, j = 0;
        for (int i = 0; i < n; i++) {
            if (predicate.test(array[i])) {
                array[i - j] = array[i];
            } else {
                j++;
            }
        }
        return n - j;
    }
    @NonPure
    public static int filterInPlace(@InPlace double[] array, BiDoubleIntPredicate predicate) {
        int n = array.length, j = 0;
        for (int i = 0; i < n; i++) {
            if (predicate.test(array[i], i)) {
                array[i - j] = array[i];
            } else {
                j++;
            }
        }
        return n - j;
    }

    /* Array search */

    @Pure
    public static int indexOf(double[] array, double val) {
        return indexOf(array, i -> i == val);
    }

    @Pure
    public static int indexOf(double[] array, DoublePredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int indexOf(double[] array, DoublePredicate check, @PyIndex int from, @PyIndex int to, int def) {
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

    @Pure
    public static int lastIndexOf(double[] array, double val) {
        return lastIndexOf(array, i -> i == val);
    }

    @Pure
    public static int lastIndexOf(double[] array, DoublePredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int lastIndexOf(double[] array, DoublePredicate check, @PyIndex int from, @PyIndex int to, int def) {
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

    @Pure
    public static boolean contains(double[] array, double val) {
        return indexOf(array, val) >= 0;
    }

    @Pure
    public static boolean contains(double[] array, DoublePredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    @Pure
    public static double[] concat(double[] array1, double[] array2) {
        double[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    @Pure
    public static double[] append(double[] array, double val) {
        double[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    @Pure
    public static double[] prepend(double val, double[] array) {
        double[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    @Pure
    public static double[] slice(double[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return from == to ? EMPTY_ARRAY : from == 0 && to == array.length ? array : Arrays.copyOfRange(array, from, to);
    }

    @Pure
    public static double[] slice(double[] array, int from) {
        return slice(array, from, array.length);
    }

    @Pure
    public static double[] realloc(double[] array, int len) {
        assert len >= 0 : "Invalid realloc length: " + len;
        return len == 0 ? EMPTY_ARRAY : len == array.length ? array : Arrays.copyOf(array, len);
    }

    @Pure
    public static double[] ensureCapacity(double[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    @Pure
    public static int[] coerceToIntArray(double[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    @Pure
    public static long[] coerceToLongArray(double[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Double.BYTES;

    @Pure
    public static byte[] toBigEndianBytes(double[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Double.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asDoubleBuffer().put(array);
        return byteBuffer.array();
    }

    /* Java NIO buffers */

    @Pure
    public static double[] getDoubles(java.nio.DoubleBuffer buffer, int len) {
        double[] doubles = new double[len];
        buffer.get(doubles, 0, len);
        return doubles;
    }

    @Pure
    public static double[] remainingDoubles(java.nio.DoubleBuffer buffer) {
        // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
        double[] doubles = new double[buffer.remaining()];
        buffer.get(doubles, 0, doubles.length);
        return doubles;
    }

    /* Positive/non-negative number selections */

    @Pure
    public static double firstPositive(double a, double b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    @Pure
    public static double firstPositive(double a, double b, double c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    @Pure
    public static double firstPositive(double a, DoubleSupplier b) {
        return (a > 0) ? a : firstPositive(a, b.getAsDouble());
    }

    @Pure
    public static double firstPositive(DoubleSupplier a, DoubleSupplier b) {
        return firstPositive(a.getAsDouble(), b);
    }

    @Pure
    public static double firstPositive(double a, DoubleSupplier b, DoubleSupplier c) {
        double bv, cv;
        if (a > 0) return a;
        if ((bv = b.getAsDouble()) > 0) return bv;
        if ((cv = c.getAsDouble()) > 0) return cv;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, bv, cv));
    }

    @Pure
    public static double firstPositive(DoubleSupplier a, DoubleSupplier b, DoubleSupplier c) {
        return firstPositive(a.getAsDouble(), b, c);
    }

    @Pure
    public static double firstPositive(double... nums) {
        for (double num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    @Pure
    public static double firstNonNegative(double a, double b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    @Pure
    public static double firstNonNegative(double a, double b, double c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    @Pure
    public static double firstNonNegative(double a, DoubleSupplier b) {
        return (a >= 0) ? a : firstNonNegative(a, b.getAsDouble());
    }

    @Pure
    public static double firstNonNegative(DoubleSupplier a, DoubleSupplier b) {
        return firstNonNegative(a.getAsDouble(), b);
    }

    @Pure
    public static double firstNonNegative(double a, DoubleSupplier b, DoubleSupplier c) {
        double bv, cv;
        if (a >= 0) return a;
        if ((bv = b.getAsDouble()) >= 0) return bv;
        if ((cv = c.getAsDouble()) >= 0) return cv;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, bv, cv));
    }

    @Pure
    public static double firstNonNegative(DoubleSupplier a, DoubleSupplier b, DoubleSupplier c) {
        return firstNonNegative(a.getAsDouble(), b, c);
    }

    @Pure
    public static double firstNonNegative(double... nums) {
        for (double num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    @Pure
    public static double requirePositive(double val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    @Pure
    public static double requireNonNegative(double val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
