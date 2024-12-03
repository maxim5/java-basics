package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.function.LongBinaryOperator;
import java.util.function.LongPredicate;
import io.spbx.util.func.LongUnaryOperator;
import io.spbx.util.func.IntToLongFunction;
import io.spbx.util.func.BiLongIntToLongFunction;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code long}s.
 */
@Stateless
@Pure
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2024-12-02T15:53:08.666713400Z")
public class LongOps {
    public static final long ZERO = 0;

    public static final LongBinaryOperator LONG_ADD = Long::sum;
    public static final LongBinaryOperator LONG_MUL = (a, b) -> a * b;
    public static final LongBinaryOperator LONG_AND = (a, b) -> a & b;
    public static final LongBinaryOperator LONG_OR  = (a, b) -> a | b;
    public static final LongBinaryOperator LONG_XOR = (a, b) -> a ^ b;
    public static final LongUnaryOperator  LONG_NEG = a -> -a;
    public static final LongUnaryOperator  LONG_NOT = a -> ~a;

    /* Range array */

    // Supports negative ranges
    public static long[] range(long end) {
        return range((long) 0, end);
    }

    // Supports negative ranges
    public static long[] range(long start, long end) {
        long[] array = new long[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    public static long[] reverse(long[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            long tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    public static long[] fill(long[] array, long val) {
        Arrays.fill(array, val);
        return array;
    }

    public static long[] fill(int len, long value) {
        return fill(new long[len], value);
    }
    public static long[] fill(int len, IntToLongFunction func) {
        return fill(new long[len], func);
    }

    public static long[] fill(long[] array, IntToLongFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToLong(i);
        }
        return array;
    }

    /* Map array */

    public static long[] map(long[] array, LongUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToLong(array[i]);
        }
        return array;
    }
    public static long[] map(long[] array, BiLongIntToLongFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToLong(array[i], i);
        }
        return array;
    }

    /* Array search */

    public static int indexOf(long[] array, long val) {
        return indexOf(array, i -> i == val);
    }

    public static int indexOf(long[] array, LongPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int indexOf(long[] array, LongPredicate check, int from, int to, int def) {
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

    public static int lastIndexOf(long[] array, long val) {
        return lastIndexOf(array, i -> i == val);
    }

    public static int lastIndexOf(long[] array, LongPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int lastIndexOf(long[] array, LongPredicate check, int from, int to, int def) {
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

    public static boolean contains(long[] array, long val) {
        return indexOf(array, val) >= 0;
    }

    public static boolean contains(long[] array, LongPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    public static long[] concat(long[] array1, long[] array2) {
        long[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static long[] append(long[] array, long val) {
        long[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    public static long[] prepend(long val, long[] array) {
        long[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    public static long[] slice(long[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    public static long[] slice(long[] array, int from) {
        return slice(array, from, array.length);
    }

    public static long[] ensureCapacity(long[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    public static byte[] coerceToByteArray(long[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    public static int[] coerceToIntArray(long[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    public static double[] coerceToDoubleArray(long[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Long.BYTES;

    public static byte[] toBigEndianBytes(long[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Long.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asLongBuffer().put(array);
        return byteBuffer.array();
    }

    public static long[] fromBigEndianBytes(byte[] bytes) {
        assert bytes.length % BYTES == 0 : "Size mismatch on conversion to long[]: " + bytes.length;
        java.nio.LongBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.BIG_ENDIAN).asLongBuffer();
        long[] array = new long[buffer.remaining()];
        buffer.get(array);
        return array;
    }

    public static long valueOfBigEndianBytes(byte[] bytes) {
        return valueOfBigEndianBytes(bytes, 0);
    }

    public static byte[] toBigEndianBytes(long value) {
        byte[] result = new byte[BYTES];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xffL);
            value >>= 8;
        }
        return result;
    }

    public static byte[] toBigEndianBytes(long value, byte[] target) {
        return toBigEndianBytes(value, target, 0);
    }

    public static byte[] toBigEndianBytes(long value, byte[] target, int start) {
        assert start + BYTES <= target.length : "Array too small: %s < %s".formatted(target.length, start + BYTES);
        target[start]     = (byte) (value >> 56);
        target[start + 1] = (byte) (value >> 48);
        target[start + 2] = (byte) (value >> 40);
        target[start + 3] = (byte) (value >> 32);
        target[start + 4] = (byte) (value >> 24);
        target[start + 5] = (byte) (value >> 16);
        target[start + 6] = (byte) (value >> 8);
        target[start + 7] = (byte) value;
        return target;
    }

    public static long valueOfBigEndianBytes(byte[] bytes, int start) {
        assert start + BYTES <= bytes.length : "Array too small: %s < %s".formatted(bytes.length, start + BYTES);
        return valueOfBigEndianBytes(bytes[start], bytes[start + 1], bytes[start + 2], bytes[start + 3],
                                     bytes[start + 4], bytes[start + 5], bytes[start + 6], bytes[start + 7]);
    }

    public static long valueOfBigEndianBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return (b1 & 0xFFL) << 56
               | (b2 & 0xFFL) << 48
               | (b3 & 0xFFL) << 40
               | (b4 & 0xFFL) << 32
               | (b5 & 0xFFL) << 24
               | (b6 & 0xFFL) << 16
               | (b7 & 0xFFL) << 8
               | (b8 & 0xFFL);
    }

    /* Java NIO buffers */

    public static long[] getLongs(java.nio.LongBuffer buffer, int len) {
        long[] longs = new long[len];
        buffer.get(longs, 0, len);
        return longs;
    }

    // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
    public static long[] remainingLongs(java.nio.LongBuffer buffer) {
        long[] longs = new long[buffer.remaining()];
        buffer.get(longs, 0, longs.length);
        return longs;
    }

    /* Math ops */

    /**
     * Overflow-safe form of {@code (a + b) >> 1}.
     */
    public static long avg(long a, long b) {
        return (a & b) + ((a ^ b) >> 1);
    }

    /* Positive/non-negative number selections */

    public static long firstPositive(long a, long b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    public static long firstPositive(long a, long b, long c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    public static long firstPositive(long... nums) {
        for (long num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    public static long firstNonNegative(long a, long b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    public static long firstNonNegative(long a, long b, long c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    public static long firstNonNegative(long... nums) {
        for (long num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    public static long requirePositive(long val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    public static long requireNonNegative(long val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
