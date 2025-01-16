package io.spbx.util.base.ops;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import io.spbx.util.func.CharBinaryOperator;
import io.spbx.util.func.CharPredicate;
import io.spbx.util.func.CharSupplier;
import io.spbx.util.func.CharUnaryOperator;
import io.spbx.util.func.IntToCharFunction;
import io.spbx.util.func.BiCharIntPredicate;
import io.spbx.util.func.BiCharIntToCharFunction;

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
 * Utility operations for {@code char}s.
 */
@Stateless
@CheckReturnValue
@Generated(value = "$Type$Ops.java", date = "2025-01-14T10:07:33.401105400Z")
public class CharOps {
    public static final char ZERO = (char) 0;
    public static final char[] EMPTY_ARRAY = new char[0];

    public static final CharUnaryOperator CHAR_TO_LOWER = Character::toLowerCase;
    public static final CharUnaryOperator CHAR_TO_UPPER = Character::toUpperCase;
    public static final CharUnaryOperator CHAR_TO_ASCII_LOWER = CharOps::toAsciiLowerCase;
    public static final CharUnaryOperator CHAR_TO_ASCII_UPPER = CharOps::toAsciiUpperCase;
    public static final CharPredicate CHAR_IS_ASCII_LOWER_LETTER = CharOps::isAsciiLowerCase;
    public static final CharPredicate CHAR_IS_ASCII_UPPER_LETTER = CharOps::isAsciiUpperCase;

    @Pure
    public static boolean isAsciiLowerCase(char val) {
        return 'a' <= val && val <= 'z';
    }

    @Pure
    public static boolean isAsciiUpperCase(char val) {
        return 'A' <= val && val <= 'Z';
    }

    @Pure
    public static char toAsciiLowerCase(char val) {
        return isAsciiUpperCase(val) ? (char) (val + 32) : val;  // 32 = 'a' - 'A'
    }

    @Pure
    public static char toAsciiUpperCase(char val) {
        return isAsciiLowerCase(val) ? (char) (val - 32) : val;  // 32 = 'a' - 'A'
    }

    /* Range array */

    // Supports negative ranges
    @Pure
    public static char[] range(char end) {
        return range((char) 0, end);
    }

    // Supports negative ranges
    @Pure
    public static char[] range(char start, char end) {
        char[] array = new char[(int) Math.abs(end - start)];
        for (int i = 0, step = start <= end ? 1 : -1; i < array.length; i++, start += step) {
            array[i] = start;
        }
        return array;
    }

    /* Reverse array */

    @NonPure
    public static char[] reverse(@InPlace char[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            char tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    /* Fill array */

    @NonPure
    public static char[] fill(@InPlace char[] array, char val) {
        Arrays.fill(array, val);
        return array;
    }

    @Pure
    public static char[] fill(int len, char value) {
        return fill(new char[len], value);
    }
    @Pure
    public static char[] fill(int len, IntToCharFunction func) {
        return fill(new char[len], func);
    }

    @NonPure
    public static char[] fill(@InPlace char[] array, IntToCharFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToChar(i);
        }
        return array;
    }

    /* Map array */

    @Pure
    public static char[] map(char[] array, CharUnaryOperator func) {
        char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToChar(array[i]);
        }
        return result;
    }
    @Pure
    public static char[] map(char[] array, BiCharIntToCharFunction func) {
        char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = func.applyToChar(array[i], i);
        }
        return result;
    }

    @NonPure
    public static char[] mapInPlace(@InPlace char[] array, CharUnaryOperator func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToChar(array[i]);
        }
        return array;
    }
    @NonPure
    public static char[] mapInPlace(@InPlace char[] array, BiCharIntToCharFunction func) {
        for (int i = 0; i < array.length; i++) {
            array[i] = func.applyToChar(array[i], i);
        }
        return array;
    }

    /* Filter array */

    @Pure
    public static char[] filter(char[] array, CharPredicate predicate) {
        int n = array.length, j = 0;
        char[] result = new char[n];
        for (int i = 0; i < n; i++) {
            char val = array[i];
            if (predicate.test(val)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }
    public static char[] filter(char[] array, BiCharIntPredicate predicate) {
        int n = array.length, j = 0;
        char[] result = new char[n];
        for (int i = 0; i < n; i++) {
            char val = array[i];
            if (predicate.test(val, i)) {
                result[j++] = val;
            }
        }
        return realloc(result, j);
    }

    @NonPure
    public static int filterInPlace(@InPlace char[] array, CharPredicate predicate) {
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
    public static int filterInPlace(@InPlace char[] array, BiCharIntPredicate predicate) {
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
    public static int indexOf(char[] array, char val) {
        return indexOf(array, i -> i == val);
    }

    @Pure
    public static int indexOf(char[] array, CharPredicate check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int indexOf(char[] array, CharPredicate check, @PyIndex int from, @PyIndex int to, int def) {
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
    public static int lastIndexOf(char[] array, char val) {
        return lastIndexOf(array, i -> i == val);
    }

    @Pure
    public static int lastIndexOf(char[] array, CharPredicate check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static int lastIndexOf(char[] array, CharPredicate check, @PyIndex int from, @PyIndex int to, int def) {
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
    public static boolean contains(char[] array, char val) {
        return indexOf(array, val) >= 0;
    }

    @Pure
    public static boolean contains(char[] array, CharPredicate check) {
        return indexOf(array, check) >= 0;
    }

    /* Array concatenation */

    @Pure
    public static char[] concat(char[] array1, char[] array2) {
        char[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    @Pure
    public static char[] append(char[] array, char val) {
        char[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = val;
        return result;
    }

    @Pure
    public static char[] prepend(char val, char[] array) {
        char[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = val;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    /* Array manipulations */

    @Pure
    public static char[] slice(char[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return from == to ? EMPTY_ARRAY : from == 0 && to == array.length ? array : Arrays.copyOfRange(array, from, to);
    }

    @Pure
    public static char[] slice(char[] array, int from) {
        return slice(array, from, array.length);
    }

    @Pure
    public static char[] realloc(char[] array, int len) {
        assert len >= 0 : "Invalid realloc length: " + len;
        return len == 0 ? EMPTY_ARRAY : len == array.length ? array : Arrays.copyOf(array, len);
    }

    @Pure
    public static char[] ensureCapacity(char[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }

    /* Array coercion */

    @Pure
    public static byte[] coerceToByteArray(char[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }

    @Pure
    public static int[] coerceToIntArray(char[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = (int) array[i];
        }
        return ints;
    }

    @Pure
    public static long[] coerceToLongArray(char[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = (long) array[i];
        }
        return longs;
    }

    /* Array bit-level conversions */

    public static final int BYTES = Character.BYTES;

    @Pure
    public static byte[] toBigEndianBytes(char[] array) {
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(array.length * Character.BYTES).order(java.nio.ByteOrder.BIG_ENDIAN);
        byteBuffer.asCharBuffer().put(array);
        return byteBuffer.array();
    }

    @Pure
    public static char[] fromBigEndianBytes(byte[] bytes) {
        assert bytes.length % BYTES == 0 : "Size mismatch on conversion to char[]: " + bytes.length;
        java.nio.CharBuffer buffer = java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.BIG_ENDIAN).asCharBuffer();
        char[] array = new char[buffer.remaining()];
        buffer.get(array);
        return array;
    }

    @Pure
    public static char valueOfBigEndianBytes(byte[] bytes) {
        return valueOfBigEndianBytes(bytes, 0);
    }

    @Pure
    public static byte[] toBigEndianBytes(char value) {
        return new byte[] {(byte) (value >> 8), (byte) value};
    }

    @Pure
    public static char valueOfBigEndianBytes(byte[] bytes, int start) {
        assert start + BYTES <= bytes.length : "Array too small: %s < %s".formatted(bytes.length, start + BYTES);
        return valueOfBigEndianBytes(bytes[start], bytes[start + 1]);
    }

    @Pure
    public static char valueOfBigEndianBytes(byte b1, byte b2) {
        return (char) ((b1 << 8) | (b2 & 0xFF));
    }

    /* Java NIO buffers */

    @Pure
    public static char[] getChars(java.nio.CharBuffer buffer, int len) {
        char[] chars = new char[len];
        buffer.get(chars, 0, len);
        return chars;
    }

    @Pure
    public static char[] remainingChars(java.nio.CharBuffer buffer) {
        // https://stackoverflow.com/questions/679298/gets-byte-array-from-a-bytebuffer-in-java
        char[] chars = new char[buffer.remaining()];
        buffer.get(chars, 0, chars.length);
        return chars;
    }
}
