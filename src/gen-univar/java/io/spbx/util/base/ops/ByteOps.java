package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import io.spbx.util.func.ByteBinaryOperator;
import io.spbx.util.func.BytePredicate;
import io.spbx.util.func.ByteUnaryOperator;
import io.spbx.util.func.IntToByteFunction;
import io.spbx.util.func.BiByteIntToByteFunction;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * Utility operations for {@code byte}s.
 */
@Stateless
@Pure
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2024-12-02T15:53:08.666713400Z")
public class ByteOps {
    public static final byte ZERO = (byte) 0;
    public static final ByteBinaryOperator BYTE_ADD = (a, b) -> (byte) (a + b);
    public static final ByteBinaryOperator BYTE_MUL = (a, b) -> (byte) (a * b);
    public static final ByteBinaryOperator BYTE_AND = (a, b) -> (byte) (a & b);
    public static final ByteBinaryOperator BYTE_OR  = (a, b) -> (byte) (a | b);
    public static final ByteBinaryOperator BYTE_XOR = (a, b) -> (byte) (a ^ b);
    public static final ByteUnaryOperator  BYTE_NEG = a -> (byte) -a;
    public static final ByteUnaryOperator  BYTE_NOT = a -> (byte) ~a;
    public static final ByteUnaryOperator BYTE_TO_ASCII_LOWER = ByteOps::toAsciiLowerCase;
    public static final ByteUnaryOperator BYTE_TO_ASCII_UPPER = ByteOps::toAsciiUpperCase;
    public static final BytePredicate BYTE_IS_ASCII_LOWER_LETTER = ByteOps::isAsciiLowerCase;
    public static final BytePredicate BYTE_IS_ASCII_UPPER_LETTER = ByteOps::isAsciiUpperCase;

    public static boolean isAsciiLowerCase(byte val) {
        return 'a' <= val && val <= 'z';
    }

    public static boolean isAsciiUpperCase(byte val) {
        return 'A' <= val && val <= 'Z';
    }

    public static byte toAsciiLowerCase(byte val) {
        return isAsciiUpperCase(val) ? (byte) (val + 32) : val;  // 32 = 'a' - 'A'
    }

    public static byte toAsciiUpperCase(byte val) {
        return isAsciiLowerCase(val) ? (byte) (val - 32) : val;  // 32 = 'a' - 'A'
    }

    /* Range array */

    // Supports negative ranges
    public static byte[] range(byte end) {
        return range((byte) 0, end);
    }

    // Supports negative ranges
    public static byte[] range(byte start, byte end) {
        byte[] array = new byte[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    public static byte[] reverse(byte[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            byte tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    public static byte[] fill(byte[] array, byte val) {
        Arrays.fill(array, val);
        return array;
    }

    public static byte[] fill(int len, byte value) {
        return fill(new byte[len], value);
    }
    public static byte[] fill(int len, IntToByteFunction func) {
        return fill(new byte[len], func);
    }

    public static byte[] fill(byte[] array, IntToByteFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToByte(i);
        }
        return array;
    }

    /* Map array */

    public static byte[] map(byte[] array, ByteUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToByte(array[i]);
        }
        return array;
    }
    public static byte[] map(byte[] array, BiByteIntToByteFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToByte(array[i], i);
        }
        return array;
    }

    /* Array search */

    public static int indexOf(byte[] array, byte val) {
        return indexOf(array, i -> i == val);
    }

    public static int indexOf(byte[] array, BytePredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int indexOf(byte[] array, BytePredicate check, int from, int to, int def) {
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

    public static int lastIndexOf(byte[] array, byte val) {
        return lastIndexOf(array, i -> i == val);
    }

    public static int lastIndexOf(byte[] array, BytePredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @NegativeIndexingSupported
    public static int lastIndexOf(byte[] array, BytePredicate check, int from, int to, int def) {
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

    public static boolean contains(byte[] array, byte val) {
        return indexOf(array, val) >= 0;
    }

    public static boolean contains(byte[] array, BytePredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static byte[] append(byte[] array, byte val) {
        byte[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    public static byte[] prepend(byte val, byte[] array) {
        byte[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    public static byte[] slice(byte[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    public static byte[] slice(byte[] array, int from) {
        return slice(array, from, array.length);
    }

    public static byte[] ensureCapacity(byte[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    public static int[] coerceToIntArray(byte[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    public static long[] coerceToLongArray(byte[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    public static char[] coerceToCharArray(byte[] array) {
        char[] chars = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = (char) array[i];
        }
        return chars;
    }

    public static double[] coerceToDoubleArray(byte[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Java NIO buffers */

    public static byte[] getBytes(java.nio.ByteBuffer buffer, int len) {
        byte[] bytes = new byte[len];
        buffer.get(bytes, 0, len);
        return bytes;
    }

    // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
    public static byte[] remainingBytes(java.nio.ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, bytes.length);
        return bytes;
    }

    /* Positive/non-negative number selections */

    public static byte firstPositive(byte a, byte b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    public static byte firstPositive(byte a, byte b, byte c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    public static byte firstPositive(byte... nums) {
        for (byte num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    public static byte firstNonNegative(byte a, byte b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    public static byte firstNonNegative(byte a, byte b, byte c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    public static byte firstNonNegative(byte... nums) {
        for (byte num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    public static byte requirePositive(byte val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    public static byte requireNonNegative(byte val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
