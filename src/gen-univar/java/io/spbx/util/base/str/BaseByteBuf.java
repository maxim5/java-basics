package io.spbx.util.base.str;

import io.spbx.util.base.annotate.AllowPythonIndexing;
import io.spbx.util.base.annotate.PyIndex;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static io.spbx.util.base.error.RangeCheck.OPEN_END_RANGE;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;
import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;

/**
 * A base class for the {@code byte} array buffer.
 * <p>
 * {@link BaseByteBuf} supports python-style negative indexing, e.g. {@code buf.at(-2)} is equivalent to
 * {@code buf.at(buf.length()-2)}.
 */
@AllowPythonIndexing
@Generated(value = "Base$Type$Buf.java", date = "2025-01-14T10:07:33.422108600Z")
public abstract class BaseByteBuf<B extends BaseByteBuf> extends BaseBuf implements Comparable<B>, Serializable {
    protected final byte[] bytes;
    protected /* final */ int start;
    protected /* final */ int end;

    protected BaseByteBuf(byte @NotNull[] bytes, int start, int end) {
        assert 0 <= start : "Start index %d out of bounds".formatted(start);
        assert start <= end : "Start index can't be larger than end index: %d > %d".formatted(start, end);
        assert end <= bytes.length : "End index %d out of bounds [%d, %d]".formatted(end, 0, bytes.length);
        this.bytes = bytes;
        this.start = start;
        this.end = end;
    }

    /* Position pointers */

    public final int start() {
        return start;
    }

    public final int end() {
        return end;
    }

    @Override
    public final int length() {
        return end - start;
    }

    @Override
    public final boolean isEmpty() {
        return start == end;
    }

    @Override
    public final boolean isNotEmpty() {
        return start < end;
    }

    /* Bytes access */

    @AllowPythonIndexing
    public byte byteAt(@PyIndex int index) {
        assert rangeCheck(index, BEFORE_TRANSLATION | OPEN_END_RANGE);
        return bytes[start + translateIndex(index)];
    }

    @AllowPythonIndexing
    public int at(@PyIndex int index) {
        index = translateIndex(index);
        return index >= 0 && index < length() ? bytes[start + index] : -1;
    }

    public <T extends IntConsumer> @NotNull T forEach(@NotNull T consumer) {
        for (int i = start; i < end; ++i) {
            consumer.accept(bytes[i]);
        }
        return consumer;
    }

    public <T extends IndexedByteConsumer> @NotNull T forEachIndexed(@NotNull T consumer) {
        for (int index = 0, i = start; i < end; ++index, ++i) {
            consumer.accept(index, bytes[i]);
        }
        return consumer;
    }

    // FIX: Primitive-candidate
    public interface IndexedByteConsumer {
        void accept(int index, byte val);
    }

    public @NotNull Iterable<ByteCursor> toIterable() {
        return this::iterator;
    }

    public @NotNull Iterator<ByteCursor> iterator() {
        return new Iterator<>() {
            private final ByteCursor cursor = new ByteCursor();
            private final int len = length();
            private int index = 0;

            @Override public boolean hasNext() {
                return index < len;
            }

            @Override public @NotNull ByteCursor next() {
                cursor.index = index;
                cursor.val = bytes[start + index];
                index++;
                return cursor;
            }
        };
    }

    public static class ByteCursor {
        public int index;
        public byte val;

        public int index() {
            return index;
        }
        public byte val() {
            return val;
        }
    }

    public @NotNull ByteBuffer asNioBuffer() {
        return asRawBuffer().asReadOnlyBuffer();
    }

    protected @NotNull ByteBuffer asRawBuffer() {
        return ByteBuffer.wrap(bytes, start, end - start);  // note: writable!
    }

    /* Starts-with and ends-with */

    public boolean startsWith(@NotNull B prefix) {
        return length() >= prefix.length() &&
               Arrays.equals(bytes, start, start + prefix.length(), prefix.bytes, prefix.start, prefix.end);
    }

    public boolean endsWith(@NotNull B suffix) {
        return length() >= suffix.length() &&
               Arrays.equals(bytes, end - suffix.length(), end, suffix.bytes, suffix.start, suffix.end);
    }

    public boolean startsWith(byte[] prefix) {
        return length() >= prefix.length && Arrays.equals(bytes, start, start + prefix.length, prefix, 0, prefix.length);
    }

    public boolean endsWith(byte[] suffix) {
        return length() >= suffix.length && Arrays.equals(bytes, end - suffix.length, end, suffix, 0, suffix.length);
    }

    public boolean startsWith(byte val) {
        return isNotEmpty() && bytes[start] == val;
    }

    public boolean endsWith(byte val) {
        return isNotEmpty() && bytes[end - 1] == val;
    }

    /* Index of: `IntPredicate` */

    public int indexOf(@NotNull IntPredicate check) {
        return this.indexOf(check, 0, -1);
    }

    @AllowPythonIndexing
    public int indexOf(@NotNull IntPredicate check, @PyIndex int from) {
        return this.indexOf(check, from, -1);
    }

    @AllowPythonIndexing
    public int indexOf(@NotNull IntPredicate check, @PyIndex int from, int def) {
        assert rangeCheck(from, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert outOfRangeCheck(def);
        for (int i = start + translateIndex(from); i < end; ++i) {
            if (check.test(bytes[i])) {
                return i - start;
            }
        }
        return def;
    }

    /* Index of: byte */

    public int indexOf(byte val) {
        return this.indexOf(val, 0, -1);
    }

    @AllowPythonIndexing
    public int indexOf(byte val, @PyIndex int from) {
        return this.indexOf(val, from, -1);
    }

    @AllowPythonIndexing
    public int indexOf(byte val, @PyIndex int from, int def) {
        return this.indexOf(cur -> cur == val, from, def);
    }

    /* Index of: `BaseByteBuf` */

    public int indexOf(@NotNull B array) {
        return this.indexOf(array, 0, -1);
    }

    @AllowPythonIndexing
    public int indexOf(@NotNull B array, @PyIndex int from) {
        return this.indexOf(array, from, -1);
    }

    @AllowPythonIndexing
    public int indexOf(@NotNull B array, @PyIndex int from, int def) {
        assert rangeCheck(from, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert outOfRangeCheck(def);
        int length = array.length();
        if (length() < length) {
            return def;
        }
        for (int i = start + translateIndex(from), max = end - length + 1; i < max; ++i) {
            if (Arrays.equals(bytes, i, i + length, array.bytes, array.start, array.end)) {
                return i - start;
            }
        }
        return def;
    }

    /* Index of: `byte[]` */

    public int indexOf(byte[] array) {
        return this.indexOf(array, 0, -1);
    }

    @AllowPythonIndexing
    public int indexOf(byte[] array, @PyIndex int from) {
        return this.indexOf(array, from, -1);
    }

    @AllowPythonIndexing
    public int indexOf(byte[] array, @PyIndex int from, int def) {
        return this.indexOf(_wrap(array, 0, array.length), from, def);
    }

    /* Last index of: `IntPredicate` */

    public int lastIndexOf(@NotNull IntPredicate check) {
        return this.lastIndexOf(check, length() - 1, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(@NotNull IntPredicate check, @PyIndex int from) {
        return this.lastIndexOf(check, from, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(@NotNull IntPredicate check, @PyIndex int from, int def) {
        assert rangeCheck(from, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert outOfRangeCheck(def);
        for (int i = Math.min(start + translateIndex(from), end - 1); i >= start; --i) {
            if (check.test(bytes[i])) {
                return i - start;
            }
        }
        return def;
    }

    /* Last index of: byte */

    public int lastIndexOf(byte val) {
        return this.lastIndexOf(val, length() - 1, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(byte val, @PyIndex int from) {
        return this.lastIndexOf(val, from, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(byte val, @PyIndex int from, int def) {
        return this.lastIndexOf(cur -> cur == val, from, def);
    }

    /* Last index of: `BaseByteBuf` */

    public int lastIndexOf(@NotNull B array) {
        return this.lastIndexOf(array, length() - array.length(), -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(@NotNull B array, @PyIndex int from) {
        return this.lastIndexOf(array, from, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(@NotNull B array, @PyIndex int from, int def) {
        assert rangeCheck(from, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert outOfRangeCheck(def);
        int length = array.length();
        if (length() < length) {
            return def;
        }
        for (int i = Math.min(start + translateIndex(from), end - length); i >= start; --i) {
            if (Arrays.equals(bytes, i, i + length, array.bytes, array.start, array.end)) {
                return i - start;
            }
        }
        return def;
    }

    /* Last index of: `byte[]` */

    public int lastIndexOf(byte[] array) {
        return this.lastIndexOf(array, length() - 1, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(byte[] array, @PyIndex int from) {
        return this.lastIndexOf(array, from, -1);
    }

    @AllowPythonIndexing
    public int lastIndexOf(byte[] array, @PyIndex int from, int def) {
        return this.lastIndexOf(_wrap(array, 0, array.length), from, def);
    }

    /* Contains */

    public boolean contains(byte val) {
        return indexOf(val) >= 0;
    }

    public boolean contains(@NotNull B array) {
        return indexOf(array) >= 0;
    }

    public boolean contains(byte[] val) {
        return indexOf(val) >= 0;
    }

    /* Split */

    public @NotNull Iterator<B> split(byte val) {
        return split(start -> indexOf(val, start), 1);
    }

    public @NotNull Iterator<B> split(@NotNull B array) {
        assert array.isNotEmpty() : "Separator is empty";
        return split(start -> indexOf(array, start), array.length());
    }

    public @NotNull Iterator<B> split(byte[] val) {
        assert val.length > 0 : "Separator is empty";
        return split(start -> indexOf(val, start), val.length);
    }

    public void split(byte val, @NotNull Consumer<B> callback) {
        split(start -> indexOf(val, start), callback, 1);
    }

    public void split(@NotNull B array, @NotNull Consumer<B> callback) {
        assert array.isNotEmpty() : "Separator is empty";
        split(start -> indexOf(array, start), callback, array.length());
    }

    public void split(byte[] val, @NotNull Consumer<B> callback) {
        assert val.length > 0 : "Separator is empty";
        split(start -> indexOf(val, start), callback, val.length);
    }

    public @NotNull Iterator<B> split(@NotNull IntPredicate predicate) {
        return split(start -> indexOf(predicate, start), 1);
    }

    public void split(@NotNull IntPredicate predicate, @NotNull Consumer<B> callback) {
        split(start -> indexOf(predicate, start), callback, 1);
    }

    protected @NotNull Iterator<B> split(@NotNull IntUnaryOperator indexOf, int separatorLength) {
        return new Iterator<>() {
            private final int len = length();
            private int index = 0;
            @Override public boolean hasNext() {
                return index <= len;
            }
            @Override public B next() {
                assert index <= len : "No more elements to iterate";
                int next = indexOf.applyAsInt(index);
                next = next < 0 ? len : next;
                B buf = _wrap(bytes, start + index, start + next);
                index = next + separatorLength;
                return buf;
            }
        };
    }

    protected void split(@NotNull IntUnaryOperator indexOf, @NotNull Consumer<B> callback, int separatorLength) {
        int start = 0;
        while (true) {
            int index = indexOf.applyAsInt(start);
            if (index == -1) {
                callback.accept(_wrap(bytes, this.start + start, this.end));
                return;
            } else {
                callback.accept(_wrap(bytes, this.start + start, this.start + index));
                start = index + separatorLength;
            }
        }
    }

    /* Trim */

    public @NotNull B trimStart(@NotNull IntPredicate check) {
        int i = start;
        while (i < end && check.test(bytes[i])) {
            ++i;
        }
        return i == start ? _this() : _wrap(bytes, i, end);
    }

    public @NotNull B trimEnd(@NotNull IntPredicate check) {
        int i = end - 1;
        while (i >= start && check.test(bytes[i])) {
            --i;
        }
        ++i;
        return i == end ? _this() : _wrap(bytes, start, i);
    }

    public @NotNull B trim(@NotNull IntPredicate check) {
        return (B) trimStart(check).trimEnd(check);
    }

    public @NotNull B trim(byte val) {
        return trim(value -> value == val);
    }

    /* Common prefix */

    // Returns the length of the common prefix
    public int commonPrefix(@NotNull B array) {
        int index = Arrays.mismatch(bytes, start, end, array.bytes, array.start, array.end);
        return (index >= 0) ? index : length();
    }

    // Returns the length of the common prefix
    public int commonPrefix(byte[] array) {
        int index = Arrays.mismatch(bytes, start, end, array, 0, array.length);
        return (index >= 0) ? index : length();
    }

    public boolean isPrefixOf(@NotNull B array) {
        return commonPrefix(array) == length();
    }

    public boolean isPrefixOf(byte[] array) {
        return commonPrefix(array) == length();
    }

    /* Common suffix */

    // Returns the length of the common suffix
    public int commonSuffix(@NotNull B array) {
        int i = 1;
        int limit = Math.min(length(), array.length());
        while (i <= limit && bytes[end - i] == array.bytes[array.end - i]) {
            i++;
        }
        return i - 1;
    }

    // Returns the length of the common suffix
    public int commonSuffix(byte[] array) {
        int i = 1;
        int limit = Math.min(length(), array.length);
        while (i <= limit && bytes[end - i] == array[array.length - i]) {
            i++;
        }
        return i - 1;
    }

    public boolean isSuffixOf(@NotNull B array) {
        return commonSuffix(array) == length();
    }

    public boolean isSuffixOf(byte[] array) {
        return commonSuffix(array) == length();
    }

    /* Remove prefix */

    public @NotNull B removePrefix(@NotNull B prefix) {
        int len = commonPrefix(prefix);
        return len < prefix.length() ? _this() : _wrap(bytes, start + len, end);
    }

    public @NotNull B removePrefix(byte val) {
        return startsWith(val) ? _wrap(bytes, start + 1, end) : _this();
    }

    public @NotNull B removePrefix(byte[] prefix) {
        return startsWith(prefix) ? _wrap(bytes, start + prefix.length, end) : _this();
    }

    /* Remove suffix */

    public @NotNull B removeSuffix(@NotNull B suffix) {
        int len = commonSuffix(suffix);
        return len < suffix.length() ? _this() : _wrap(bytes, start, end - len);
    }

    public @NotNull B removeSuffix(byte val) {
        return endsWith(val) ? _wrap(bytes, start, end - 1) : _this();
    }

    public @NotNull B removeSuffix(byte[] prefix) {
        return endsWith(prefix) ? _wrap(bytes, start, end - prefix.length) : _this();
    }

    /* Comparison */

    @Override
    public int compareTo(@NotNull B that) {
        return Arrays.compare(bytes, start, end, that.bytes, that.start, that.end);
    }

    public int compareTo(@NotNull byte[] array) {
        return Arrays.compare(bytes, start, end, array, 0, array.length);
    }

    /* Equality check */

    public boolean contentEquals(B that) {
        return Arrays.equals(bytes, start, end, that.bytes, that.start, that.end);
    }

    public boolean contentEquals(byte val) {
        return length() == 1 && at(0) == val;
    }

    public boolean contentEquals(byte[] that) {
        return Arrays.equals(bytes, start, end, that, 0, that.length);
    }

    /* Hash code */

    // Based on the shift-add-xor class of string hashing functions
    // cf. Ramakrishna and Zobel,
    //     "Performance in Practice of String Hashing Functions"
    //
    // Values left=5, right=2 work well for ASCII inputs.

    protected static final int HASH_SEED = 31;
    protected static final int HASH_LEFT = 5;
    protected static final int HASH_RIGHT = 2;

    public static int hashCode(byte[] bytes) {
        return hashCode(bytes, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static int hashCode(byte[] bytes, int start, int end) {
        return hashCode(bytes, start, end, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static int hashCode(byte[] bytes, int start, int end, @NotNull ByteFunc func) {
        return hashCode(bytes, start, end, func, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static <T> int hashCode(@NotNull T instance, int len, @NotNull ByteAtFunction<T> byteAt) {
        return hashCode(instance, len, byteAt, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    // FIX: Primitive-candidate
    public interface ByteFunc {
        byte apply(byte val);
    }

    // FIX: Primitive-candidate
    public interface ByteAtFunction<T> {
        int byteAt(@NotNull T instance, int index);
    }

    protected static int hashCode(byte[] bytes, int seed, int l, int r) {
        int hash = seed;
        for (byte val : bytes) {
            hash = hash ^ ((hash << l) + (hash >> r) + val);
        }
        return hash;
    }

    protected static int hashCode(byte[] bytes, int start, int end, int seed, int l, int r) {
        int hash = seed;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + bytes[i]);
        }
        return hash;
    }

    protected static int hashCode(byte[] bytes, int start, int end, @NotNull ByteFunc func, int seed, int l, int r) {
        int hash = seed;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + func.apply(bytes[i]));
        }
        return hash;
    }

    protected static <T> int hashCode(@NotNull T instance, int len, @NotNull ByteAtFunction<T> byteAtFunc,
                                      int seed, int l, int r) {
        int hash = seed;
        for (int i = 0; i < len; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + byteAtFunc.byteAt(instance, i));
        }
        return hash;
    }

    /* Helper */

    protected abstract @NotNull B _this();

    @AllowPythonIndexing
    protected abstract @NotNull B _wrap(byte @NotNull[] bytes, @PyIndex int start, @PyIndex int end);
}
