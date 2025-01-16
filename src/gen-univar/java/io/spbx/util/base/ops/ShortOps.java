package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import io.spbx.util.func.ShortBinaryOperator;
import io.spbx.util.func.ShortPredicate;
import io.spbx.util.func.ShortSupplier;
import io.spbx.util.func.ShortUnaryOperator;
import io.spbx.util.func.IntToShortFunction;
import io.spbx.util.func.BiShortIntPredicate;
import io.spbx.util.func.BiShortIntToShortFunction;

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
 * Utility operations for {@code short}s.
 */
@Stateless
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2025-01-14T10:07:33.401105400Z")
public class ShortOps {
    public static final short ZERO = (short) 0;
    public static final short[] EMPTY_ARRAY = new short[0];
    public static final ShortBinaryOperator SHORT_ADD = (a, b) -> (short) (a + b);
    public static final ShortBinaryOperator SHORT_MUL = (a, b) -> (short) (a * b);
    public static final ShortBinaryOperator SHORT_AND = (a, b) -> (short) (a & b);
    public static final ShortBinaryOperator SHORT_OR  = (a, b) -> (short) (a | b);
    public static final ShortBinaryOperator SHORT_XOR = (a, b) -> (short) (a ^ b);
    public static final ShortUnaryOperator  SHORT_NEG = a -> (short) -a;
    public static final ShortUnaryOperator  SHORT_NOT = a -> (short) ~a;

    /* Range array */

    // Supports negative ranges
    @Pure
    public static short[] range(short end) {
        return range((short) 0, end);
    }

    // Supports negative ranges
    @Pure
    public static short[] range(short start, short end) {
        short[] array = new short[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    @NonPure
    public static short[] reverse(@InPlace short[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            short tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    @NonPure
    public static short[] fill(@InPlace short[] array, short val) {
        Arrays.fill(array, val);
        return array;
    }

    @Pure
    public static short[] fill(int len, short value) {
        return fill(new short[len], value);
    }
    @Pure
    public static short[] fill(int len, IntToShortFunction func) {
        return fill(new short[len], func);
    }

    @NonPure
    public static short[] fill(@InPlace short[] array, IntToShortFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToShort(i);
        }
        return array;
    }

    /* Map array */

    @Pure
    public static short[] map(short[] array, ShortUnaryOperator func) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToShort(array[i]);
        }
        return result;
    }
    @Pure
    public static short[] map(short[] array, BiShortIntToShortFunction func) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToShort(array[i], i);
        }
        return result;
    }

    @NonPure
    public static short[] mapInPlace(@InPlace short[] array, ShortUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToShort(array[i]);
        }
        return array;
    }
    @NonPure
    public static short[] mapInPlace(@InPlace short[] array, BiShortIntToShortFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToShort(array[i], i);
        }
        return array;
    }

    /* Filter array */

    @Pure
    public static short[] filter(short[] array, ShortPredicate predicate) {
        int n = array.length, j = 0;
        short[] result = new short[n];
        for (int i = 0; i < n; i++) {
            short val = array[i];
            if (predicate.test(val)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }
    public static short[] filter(short[] array, BiShortIntPredicate predicate) {
        int n = array.length, j = 0;
        short[] result = new short[n];
        for (int i = 0; i < n; i++) {
            short val = array[i];
            if (predicate.test(val, i)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }

    @NonPure
    public static int filterInPlace(@InPlace short[] array, ShortPredicate predicate) {
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
    public static int filterInPlace(@InPlace short[] array, BiShortIntPredicate predicate) {
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
    public static int indexOf(short[] array, short val) {
        return indexOf(array, i -> i == val);
    }

    @Pure
    public static int indexOf(short[] array, ShortPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int indexOf(short[] array, ShortPredicate check, @PyIndex int from, @PyIndex int to, int def) {
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
    public static int lastIndexOf(short[] array, short val) {
        return lastIndexOf(array, i -> i == val);
    }

    @Pure
    public static int lastIndexOf(short[] array, ShortPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int lastIndexOf(short[] array, ShortPredicate check, @PyIndex int from, @PyIndex int to, int def) {
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
    public static boolean contains(short[] array, short val) {
        return indexOf(array, val) >= 0;
    }

    @Pure
    public static boolean contains(short[] array, ShortPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    @Pure
    public static short[] concat(short[] array1, short[] array2) {
        short[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    @Pure
    public static short[] append(short[] array, short val) {
        short[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    @Pure
    public static short[] prepend(short val, short[] array) {
        short[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    @Pure
    public static short[] slice(short[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return from == to ? EMPTY_ARRAY : from == 0 && to == array.length ? array : Arrays.copyOfRange(array, from, to);
    }

    @Pure
    public static short[] slice(short[] array, int from) {
        return slice(array, from, array.length);
    }

    @Pure
    public static short[] realloc(short[] array, int len) {
        assert len >= 0 : "Invalid realloc length: " + len;
        return len == 0 ? EMPTY_ARRAY : len == array.length ? array : Arrays.copyOf(array, len);
    }

    @Pure
    public static short[] ensureCapacity(short[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    @Pure
    public static byte[] coerceToByteArray(short[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    @Pure
    public static int[] coerceToIntArray(short[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    @Pure
    public static long[] coerceToLongArray(short[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    @Pure
    public static double[] coerceToDoubleArray(short[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            doubles[i] = (double) array[i];
        }
        return doubles;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Short.BYTES;

    @Pure
    public static byte[] toBigEndianBytes(short[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Short.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asShortBuffer().put(array);
        return byteBuffer.array();
    }

    @Pure
    public static short[] fromBigEndianBytes(byte[] bytes) {
        assert bytes.length % BYTES == 0 : "Size mismatch on conversion to short[]: " + bytes.length;
        java.nio.ShortBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.BIG_ENDIAN).asShortBuffer();
        short[] array = new short[buffer.remaining()];
        buffer.get(array);
        return array;
    }

    @Pure
    public static short valueOfBigEndianBytes(byte[] bytes) {
        return valueOfBigEndianBytes(bytes, 0);
    }

    @Pure
    public static byte[] toBigEndianBytes(short value) {
        return new byte[] {(byte) (value >> 8), (byte) value};
    }

    @Pure
    public static short valueOfBigEndianBytes(byte[] bytes, int start) {
        assert start + BYTES <= bytes.length : "Array too small: %s < %s".formatted(bytes.length, start + BYTES);
        return valueOfBigEndianBytes(bytes[start], bytes[start + 1]);
    }

    @Pure
    public static short valueOfBigEndianBytes(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }

    /* Java NIO buffers */

    @Pure
    public static short[] getShorts(java.nio.ShortBuffer buffer, int len) {
        short[] shorts = new short[len];
        buffer.get(shorts, 0, len);
        return shorts;
    }

    @Pure
    public static short[] remainingShorts(java.nio.ShortBuffer buffer) {
        // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
        short[] shorts = new short[buffer.remaining()];
        buffer.get(shorts, 0, shorts.length);
        return shorts;
    }

    /* Positive/non-negative number selections */

    @Pure
    public static short firstPositive(short a, short b) {
        if (a > 0) return a;
        if (b > 0) return b;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d".formatted(a, b));
    }

    @Pure
    public static short firstPositive(short a, short b, short c) {
        if (a > 0) return a;
        if (b > 0) return b;
        if (c > 0) return c;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, b, c));
    }

    @Pure
    public static short firstPositive(short a, ShortSupplier b) {
        return (a > 0) ? a : firstPositive(a, b.getAsShort());
    }

    @Pure
    public static short firstPositive(ShortSupplier a, ShortSupplier b) {
        return firstPositive(a.getAsShort(), b);
    }

    @Pure
    public static short firstPositive(short a, ShortSupplier b, ShortSupplier c) {
        short bv, cv;
        if (a > 0) return a;
        if ((bv = b.getAsShort()) > 0) return bv;
        if ((cv = c.getAsShort()) > 0) return cv;
        throw new IllegalArgumentException("All numbers are non-positive: %d, %d, %d".formatted(a, bv, cv));
    }

    @Pure
    public static short firstPositive(ShortSupplier a, ShortSupplier b, ShortSupplier c) {
        return firstPositive(a.getAsShort(), b, c);
    }

    @Pure
    public static short firstPositive(short... nums) {
        for (short num : nums) {
            if (num > 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are non-positive: " + Arrays.toString(nums));
    }

    @Pure
    public static short firstNonNegative(short a, short b) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        throw new IllegalArgumentException("All numbers are negative: %d, %d".formatted(a, b));
    }

    @Pure
    public static short firstNonNegative(short a, short b, short c) {
        if (a >= 0) return a;
        if (b >= 0) return b;
        if (c >= 0) return c;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, b, c));
    }

    @Pure
    public static short firstNonNegative(short a, ShortSupplier b) {
        return (a >= 0) ? a : firstNonNegative(a, b.getAsShort());
    }

    @Pure
    public static short firstNonNegative(ShortSupplier a, ShortSupplier b) {
        return firstNonNegative(a.getAsShort(), b);
    }

    @Pure
    public static short firstNonNegative(short a, ShortSupplier b, ShortSupplier c) {
        short bv, cv;
        if (a >= 0) return a;
        if ((bv = b.getAsShort()) >= 0) return bv;
        if ((cv = c.getAsShort()) >= 0) return cv;
        throw new IllegalArgumentException("All numbers are negative: %d, %d, %d".formatted(a, bv, cv));
    }

    @Pure
    public static short firstNonNegative(ShortSupplier a, ShortSupplier b, ShortSupplier c) {
        return firstNonNegative(a.getAsShort(), b, c);
    }

    @Pure
    public static short firstNonNegative(short... nums) {
        for (short num : nums) {
            if (num >= 0)
                return num;
        }
        throw new IllegalArgumentException("All numbers are negative: " + Arrays.toString(nums));
    }

    /* Positive/non-negative assersions */

    @Pure
    public static short requirePositive(short val) {
        if (val <= 0)
            throw new IllegalArgumentException("Value must be positive: " + val);
        return val;
    }

    @Pure
    public static short requireNonNegative(short val) {
        if (val < 0)
            throw new IllegalArgumentException("Value must be non-negative: " + val);
        return val;
    }
}
