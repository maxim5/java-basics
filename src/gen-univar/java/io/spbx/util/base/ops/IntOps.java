package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import io.spbx.util.func.IntUnaryOperator;
import io.spbx.util.func.BiIntPredicate;

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
 * Utility operations for {@code int}s.
 */
@Stateless
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2025-01-14T10:07:33.401105400Z")
public class IntOps {
    public static final int ZERO = 0;
    public static final int[] EMPTY_ARRAY = new int[0];

    public static final IntBinaryOperator INT_ADD = Integer::sum;
    public static final IntBinaryOperator INT_MUL = (a, b) -> a * b;
    public static final IntBinaryOperator INT_AND = (a, b) -> a & b;
    public static final IntBinaryOperator INT_OR  = (a, b) -> a | b;
    public static final IntBinaryOperator INT_XOR = (a, b) -> a ^ b;
    public static final IntUnaryOperator  INT_NEG = a -> -a;
    public static final IntUnaryOperator  INT_NOT = a -> ~a;
    public static final IntUnaryOperator INT_TO_ASCII_LOWER = IntOps::toAsciiLowerCase;
    public static final IntUnaryOperator INT_TO_ASCII_UPPER = IntOps::toAsciiUpperCase;
    public static final IntPredicate INT_IS_ASCII_LOWER_LETTER = IntOps::isAsciiLowerCase;
    public static final IntPredicate INT_IS_ASCII_UPPER_LETTER = IntOps::isAsciiUpperCase;

    @Pure
    public static boolean isAsciiLowerCase(int val) {
        return 'a' <= val && val <= 'z';
    }

    @Pure
    public static boolean isAsciiUpperCase(int val) {
        return 'A' <= val && val <= 'Z';
    }

    @Pure
    public static int toAsciiLowerCase(int val) {
        return isAsciiUpperCase(val) ? (int) (val + 32) : val;  // 32 = 'a' - 'A'
    }

    @Pure
    public static int toAsciiUpperCase(int val) {
        return isAsciiLowerCase(val) ? (int) (val - 32) : val;  // 32 = 'a' - 'A'
    }

    /* Range array */

    // Supports negative ranges
    @Pure
    public static int[] range(int end) {
        return range((int) 0, end);
    }

    // Supports negative ranges
    @Pure
    public static int[] range(int start, int end) {
        int[] array = new int[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    @NonPure
    public static int[] reverse(@InPlace int[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    @NonPure
    public static int[] fill(@InPlace int[] array, int val) {
        Arrays.fill(array, val);
        return array;
    }

    @Pure
    public static int[] fill(int len, int value) {
        return fill(new int[len], value);
    }

    @Pure
    public static int[] fill(int len, IntUnaryOperator func) {
        return fill(new int[len], func);
    }

    @NonPure
    public static int[] fill(@InPlace int[] array, IntUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToInt(i);
        }
        return array;
    }

    /* Map array */

    @Pure
    public static int[] map(int[] array, IntUnaryOperator func) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToInt(array[i]);
        }
        return result;
    }

    @Pure
    public static int[] map(int[] array, IntBinaryOperator func) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyAsInt(array[i], i);
        }
        return result;
    }

    @NonPure
    public static int[] mapInPlace(@InPlace int[] array, IntUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToInt(array[i]);
        }
        return array;
    }

    @NonPure
    public static int[] mapInPlace(@InPlace int[] array, IntBinaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyAsInt(array[i], i);
        }
        return array;
    }

    /* Filter array */

    @Pure
    public static int[] filter(int[] array, IntPredicate predicate) {
        int n = array.length, j = 0;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            int val = array[i];
            if (predicate.test(val)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }

    @Pure
    public static int[] filter(int[] array, BiIntPredicate predicate) {
        int n = array.length, j = 0;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            int val = array[i];
            if (predicate.test(val, i)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }

    @NonPure
    public static int filterInPlace(@InPlace int[] array, IntPredicate predicate) {
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
    public static int filterInPlace(@InPlace int[] array, BiIntPredicate predicate) {
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
    public static int indexOf(int[] array, int val) {
        return indexOf(array, i -> i == val);
    }

    @Pure
    public static int indexOf(int[] array, IntPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int indexOf(int[] array, IntPredicate check, @PyIndex int from, @PyIndex int to, int def) {
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
    public static int lastIndexOf(int[] array, int val) {
        return lastIndexOf(array, i -> i == val);
    }

    @Pure
    public static int lastIndexOf(int[] array, IntPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int lastIndexOf(int[] array, IntPredicate check, @PyIndex int from, @PyIndex int to, int def) {
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
    public static boolean contains(int[] array, int val) {
        return indexOf(array, val) >= 0;
    }

    @Pure
    public static boolean contains(int[] array, IntPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    @Pure
    public static int[] concat(int[] array1, int[] array2) {
        int[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    @Pure
    public static int[] append(int[] array, int val) {
        int[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    @Pure
    public static int[] prepend(int val, int[] array) {
        int[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    @Pure
    public static int[] slice(int[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return from == to ? EMPTY_ARRAY : from == 0 && to == array.length ? array : Arrays.copyOfRange(array, from, to);
    }

    @Pure
    public static int[] slice(int[] array, int from) {
        return slice(array, from, array.length);
    }

    @Pure
    public static int[] realloc(int[] array, int len) {
        assert len >= 0 : "Invalid realloc length: " + len;
        return len == 0 ? EMPTY_ARRAY : len == array.length ? array : Arrays.copyOf(array, len);
    }

    @Pure
    public static int[] ensureCapacity(int[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    @Pure
    public static byte[] coerceToByteArray(int[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    @Pure
    public static long[] coerceToLongArray(int[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    @Pure
    public static char[] coerceToCharArray(int[] array) {
        char[] chars = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = (char) array[i];
        }
        return chars;
    }

    @Pure
    public static double[] coerceToDoubleArray(int[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Integer.BYTES;

    @Pure
    public static byte[] toBigEndianBytes(int[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Integer.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asIntBuffer().put(array);
        return byteBuffer.array();
    }

    @Pure
    public static int[] fromBigEndianBytes(byte[] bytes) {
        assert bytes.length % BYTES == 0 : "Size mismatch on conversion to int[]: " + bytes.length;
        java.nio.IntBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] array = new int[buffer.remaining()];
        buffer.get(array);
        return array;
    }

    @Pure
    public static int valueOfBigEndianBytes(byte[] bytes) {
        return valueOfBigEndianBytes(bytes, 0);
    }

    @Pure
    public static byte[] toBigEndianBytes(int value) {
        return new byte[] {
            (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value
        };
    }

    @Pure
    public static byte[] toBigEndianBytes(int value, byte[] target) {
        return toBigEndianBytes(value, target, 0);
    }

    @Pure
    public static byte[] toBigEndianBytes(int value, byte[] target, int start) {
        assert start + BYTES <= target.length : "Array too small: %s < %s".formatted(target.length, start + BYTES);
        target[start]     = (byte) (value >> 24);
        target[start + 1] = (byte) (value >> 16);
        target[start + 2] = (byte) (value >> 8);
        target[start + 3] = (byte) value;
        return target;
    }

    @Pure
    public static int valueOfBigEndianBytes(byte[] bytes, int start) {
        assert start + BYTES <= bytes.length : "Array too small: %s < %s".formatted(bytes.length, start + BYTES);
        return valueOfBigEndianBytes(bytes[start], bytes[start + 1], bytes[start + 2], bytes[start + 3]);
    }

    @Pure
    public static int valueOfBigEndianBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    /* Java NIO buffers */

    @Pure
    public static int[] getInts(java.nio.IntBuffer buffer, int len) {
        int[] ints = new int[len];
        buffer.get(ints, 0, len);
        return ints;
    }

    @Pure
    public static int[] remainingInts(java.nio.IntBuffer buffer) {
        // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
        int[] ints = new int[buffer.remaining()];
        buffer.get(ints, 0, ints.length);
        return ints;
    }

    /* Math ops */

    /**
     * Overflow-safe form of {@code (a + b) >> 1}.
     */
    @Pure
    public static int avg(int a, int b) {
        return (a & b) + ((a ^ b) >> 1);
    }

    /* Positive/non-negative number selections */

    @Pure
    public static int firstPositive(int a, int b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    @Pure
    public static int firstPositive(int a, int b, int c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    @Pure
    public static int firstPositive(int a, IntSupplier b) {
        return (a > 0) ? a : firstPositive(a, b.getAsInt());
    }

    @Pure
    public static int firstPositive(IntSupplier a, IntSupplier b) {
        return firstPositive(a.getAsInt(), b);
    }

    @Pure
    public static int firstPositive(int a, IntSupplier b, IntSupplier c) {
        int bv, cv;
        if (a > 0) return a;
        if ((bv = b.getAsInt()) > 0) return bv;
        if ((cv = c.getAsInt()) > 0) return cv;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, bv, cv));
    }

    @Pure
    public static int firstPositive(IntSupplier a, IntSupplier b, IntSupplier c) {
        return firstPositive(a.getAsInt(), b, c);
    }

    @Pure
    public static int firstPositive(int... nums) {
        for (int num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    @Pure
    public static int firstNonNegative(int a, int b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    @Pure
    public static int firstNonNegative(int a, int b, int c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    @Pure
    public static int firstNonNegative(int a, IntSupplier b) {
        return (a >= 0) ? a : firstNonNegative(a, b.getAsInt());
    }

    @Pure
    public static int firstNonNegative(IntSupplier a, IntSupplier b) {
        return firstNonNegative(a.getAsInt(), b);
    }

    @Pure
    public static int firstNonNegative(int a, IntSupplier b, IntSupplier c) {
        int bv, cv;
        if (a >= 0) return a;
        if ((bv = b.getAsInt()) >= 0) return bv;
        if ((cv = c.getAsInt()) >= 0) return cv;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, bv, cv));
    }

    @Pure
    public static int firstNonNegative(IntSupplier a, IntSupplier b, IntSupplier c) {
        return firstNonNegative(a.getAsInt(), b, c);
    }

    @Pure
    public static int firstNonNegative(int... nums) {
        for (int num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    @Pure
    public static int requirePositive(int val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    @Pure
    public static int requireNonNegative(int val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
