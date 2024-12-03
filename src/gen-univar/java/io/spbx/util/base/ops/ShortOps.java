package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import io.spbx.util.func.ShortBinaryOperator;
import io.spbx.util.func.ShortPredicate;
import io.spbx.util.func.ShortUnaryOperator;
import io.spbx.util.func.IntToShortFunction;
import io.spbx.util.func.BiShortIntToShortFunction;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code short}s.
 */
@Stateless
@Pure
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2024-12-02T15:53:08.666713400Z")
public class ShortOps {
    public static final short ZERO = (short) 0;
    public static final ShortBinaryOperator SHORT_ADD = (a, b) -> (short) (a + b);
    public static final ShortBinaryOperator SHORT_MUL = (a, b) -> (short) (a * b);
    public static final ShortBinaryOperator SHORT_AND = (a, b) -> (short) (a & b);
    public static final ShortBinaryOperator SHORT_OR  = (a, b) -> (short) (a | b);
    public static final ShortBinaryOperator SHORT_XOR = (a, b) -> (short) (a ^ b);
    public static final ShortUnaryOperator  SHORT_NEG = a -> (short) -a;
    public static final ShortUnaryOperator  SHORT_NOT = a -> (short) ~a;

    /* Range array */

    // Supports negative ranges
    public static short[] range(short end) {
        return range((short) 0, end);
    }

    // Supports negative ranges
    public static short[] range(short start, short end) {
        short[] array = new short[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    public static short[] reverse(short[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            short tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    public static short[] fill(short[] array, short val) {
        Arrays.fill(array, val);
        return array;
    }

    public static short[] fill(int len, short value) {
        return fill(new short[len], value);
    }
    public static short[] fill(int len, IntToShortFunction func) {
        return fill(new short[len], func);
    }

    public static short[] fill(short[] array, IntToShortFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToShort(i);
        }
        return array;
    }

    /* Map array */

    public static short[] map(short[] array, ShortUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToShort(array[i]);
        }
        return array;
    }
    public static short[] map(short[] array, BiShortIntToShortFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToShort(array[i], i);
        }
        return array;
    }

    /* Array search */

    public static int indexOf(short[] array, short val) {
        return indexOf(array, i -> i == val);
    }

    public static int indexOf(short[] array, ShortPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int indexOf(short[] array, ShortPredicate check, int from, int to, int def) {
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

    public static int lastIndexOf(short[] array, short val) {
        return lastIndexOf(array, i -> i == val);
    }

    public static int lastIndexOf(short[] array, ShortPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int lastIndexOf(short[] array, ShortPredicate check, int from, int to, int def) {
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

    public static boolean contains(short[] array, short val) {
        return indexOf(array, val) >= 0;
    }

    public static boolean contains(short[] array, ShortPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    public static short[] concat(short[] array1, short[] array2) {
        short[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static short[] append(short[] array, short val) {
        short[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    public static short[] prepend(short val, short[] array) {
        short[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    public static short[] slice(short[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    public static short[] slice(short[] array, int from) {
        return slice(array, from, array.length);
    }

    public static short[] ensureCapacity(short[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    public static byte[] coerceToByteArray(short[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    public static int[] coerceToIntArray(short[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    public static long[] coerceToLongArray(short[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    public static double[] coerceToDoubleArray(short[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Short.BYTES;

    public static byte[] toBigEndianBytes(short[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Short.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asShortBuffer().put(array);
        return byteBuffer.array();
    }

    public static short[] fromBigEndianBytes(byte[] bytes) {
        assert bytes.length % BYTES == 0 : "Size mismatch on conversion to short[]: " + bytes.length;
        java.nio.ShortBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.BIG_ENDIAN).asShortBuffer();
        short[] array = new short[buffer.remaining()];
        buffer.get(array);
        return array;
    }

    public static short valueOfBigEndianBytes(byte[] bytes) {
        return valueOfBigEndianBytes(bytes, 0);
    }

    public static byte[] toBigEndianBytes(short value) {
        return new byte[] {(byte) (value >> 8), (byte) value};
    }

    public static short valueOfBigEndianBytes(byte[] bytes, int start) {
        assert start + BYTES <= bytes.length : "Array too small: %s < %s".formatted(bytes.length, start + BYTES);
        return valueOfBigEndianBytes(bytes[start], bytes[start + 1]);
    }

    public static short valueOfBigEndianBytes(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }

    /* Java NIO buffers */

    public static short[] getShorts(java.nio.ShortBuffer buffer, int len) {
        short[] shorts = new short[len];
        buffer.get(shorts, 0, len);
        return shorts;
    }

    // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
    public static short[] remainingShorts(java.nio.ShortBuffer buffer) {
        short[] shorts = new short[buffer.remaining()];
        buffer.get(shorts, 0, shorts.length);
        return shorts;
    }

    /* Positive/non-negative number selections */

    public static short firstPositive(short a, short b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    public static short firstPositive(short a, short b, short c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    public static short firstPositive(short... nums) {
        for (short num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    public static short firstNonNegative(short a, short b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    public static short firstNonNegative(short a, short b, short c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    public static short firstNonNegative(short... nums) {
        for (short num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    public static short requirePositive(short val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    public static short requireNonNegative(short val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
