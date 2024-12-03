package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import io.spbx.util.func.IntUnaryOperator;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code int}s.
 */
@Stateless
@Pure
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2024-12-02T15:53:08.666713400Z")
public class IntOps {
    public static final int ZERO = 0;

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

    public static boolean isAsciiLowerCase(int val) {
        return 'a' <= val && val <= 'z';
    }

    public static boolean isAsciiUpperCase(int val) {
        return 'A' <= val && val <= 'Z';
    }

    public static int toAsciiLowerCase(int val) {
        return isAsciiUpperCase(val) ? (int) (val + 32) : val;  // 32 = 'a' - 'A'
    }

    public static int toAsciiUpperCase(int val) {
        return isAsciiLowerCase(val) ? (int) (val - 32) : val;  // 32 = 'a' - 'A'
    }

    /* Range array */

    // Supports negative ranges
    public static int[] range(int end) {
        return range((int) 0, end);
    }

    // Supports negative ranges
    public static int[] range(int start, int end) {
        int[] array = new int[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    public static int[] reverse(int[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    public static int[] fill(int[] array, int val) {
        Arrays.fill(array, val);
        return array;
    }

    public static int[] fill(int len, int value) {
        return fill(new int[len], value);
    }

    public static int[] fill(int len, IntUnaryOperator func) {
        return fill(new int[len], func);
    }

    public static int[] fill(int[] array, IntUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToInt(i);
        }
        return array;
    }

    /* Map array */

    public static int[] map(int[] array, IntUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToInt(array[i]);
        }
        return array;
    }

    public static int[] map(int[] array, IntBinaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyAsInt(array[i], i);
        }
        return array;
    }

    /* Array search */

    public static int indexOf(int[] array, int val) {
        return indexOf(array, i -> i == val);
    }

    public static int indexOf(int[] array, IntPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int indexOf(int[] array, IntPredicate check, int from, int to, int def) {
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

    public static int lastIndexOf(int[] array, int val) {
        return lastIndexOf(array, i -> i == val);
    }

    public static int lastIndexOf(int[] array, IntPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int lastIndexOf(int[] array, IntPredicate check, int from, int to, int def) {
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

    public static boolean contains(int[] array, int val) {
        return indexOf(array, val) >= 0;
    }

    public static boolean contains(int[] array, IntPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    public static int[] concat(int[] array1, int[] array2) {
        int[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static int[] append(int[] array, int val) {
        int[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    public static int[] prepend(int val, int[] array) {
        int[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    public static int[] slice(int[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    public static int[] slice(int[] array, int from) {
        return slice(array, from, array.length);
    }

    public static int[] ensureCapacity(int[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    public static byte[] coerceToByteArray(int[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    public static long[] coerceToLongArray(int[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    public static char[] coerceToCharArray(int[] array) {
        char[] chars = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = (char) array[i];
        }
        return chars;
    }

    public static double[] coerceToDoubleArray(int[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Integer.BYTES;

    public static byte[] toBigEndianBytes(int[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Integer.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asIntBuffer().put(array);
        return byteBuffer.array();
    }

    public static int[] fromBigEndianBytes(byte[] bytes) {
        assert bytes.length % BYTES == 0 : "Size mismatch on conversion to int[]: " + bytes.length;
        java.nio.IntBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] array = new int[buffer.remaining()];
        buffer.get(array);
        return array;
    }

    public static int valueOfBigEndianBytes(byte[] bytes) {
        return valueOfBigEndianBytes(bytes, 0);
    }

    public static byte[] toBigEndianBytes(int value) {
        return new byte[] {
            (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value
        };
    }

    public static byte[] toBigEndianBytes(int value, byte[] target) {
        return toBigEndianBytes(value, target, 0);
    }

    public static byte[] toBigEndianBytes(int value, byte[] target, int start) {
        assert start + BYTES <= target.length : "Array too small: %s < %s".formatted(target.length, start + BYTES);
        target[start]     = (byte) (value >> 24);
        target[start + 1] = (byte) (value >> 16);
        target[start + 2] = (byte) (value >> 8);
        target[start + 3] = (byte) value;
        return target;
    }

    public static int valueOfBigEndianBytes(byte[] bytes, int start) {
        assert start + BYTES <= bytes.length : "Array too small: %s < %s".formatted(bytes.length, start + BYTES);
        return valueOfBigEndianBytes(bytes[start], bytes[start + 1], bytes[start + 2], bytes[start + 3]);
    }

    public static int valueOfBigEndianBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    /* Java NIO buffers */

    public static int[] getInts(java.nio.IntBuffer buffer, int len) {
        int[] ints = new int[len];
        buffer.get(ints, 0, len);
        return ints;
    }

    // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
    public static int[] remainingInts(java.nio.IntBuffer buffer) {
        int[] ints = new int[buffer.remaining()];
        buffer.get(ints, 0, ints.length);
        return ints;
    }

    /* Math ops */

    /**
     * Overflow-safe form of {@code (a + b) >> 1}.
     */
    public static int avg(int a, int b) {
        return (a & b) + ((a ^ b) >> 1);
    }

    /* Positive/non-negative number selections */

    public static int firstPositive(int a, int b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    public static int firstPositive(int a, int b, int c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    public static int firstPositive(int... nums) {
        for (int num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    public static int firstNonNegative(int a, int b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    public static int firstNonNegative(int a, int b, int c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    public static int firstNonNegative(int... nums) {
        for (int num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    public static int requirePositive(int val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    public static int requireNonNegative(int val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
