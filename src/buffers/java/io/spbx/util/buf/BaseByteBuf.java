package io.spbx.util.buf;

import io.spbx.util.annotate.NegativeIndexingSupported;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Consumer;

/**
 * A base class for the <code>byte</code> array buffer.
 * <p>
 * {@link BaseByteBuf} supports python-style negative indexing, e.g. {@code buf.at(-2)} is equivalent to
 * {@code buf.at(buf.length()-2)}.
 */
@NegativeIndexingSupported
@Generated(value = "Base$Type$Buf.java", date = "2024-08-25T16:08:05.817822126Z")
public abstract class BaseByteBuf<B extends BaseByteBuf> extends BaseBuf implements Serializable {
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

    public boolean isEmpty() {
        return start == end;
    }

    public boolean isNotEmpty() {
        return start < end;
    }

    /* Bytes access */

    @NegativeIndexingSupported
    public byte byteAt(int index) {
        assert rangeCheck(index, BEFORE_TRANSLATION | OPEN_END_RANGE);
        return bytes[start + translateIndex(index)];
    }

    @NegativeIndexingSupported
    public int at(int index) {
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

    @NegativeIndexingSupported
    public int indexOf(@NotNull IntPredicate check, int from) {
        return this.indexOf(check, from, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(@NotNull IntPredicate check, int from, int def) {
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

    @NegativeIndexingSupported
    public int indexOf(byte val, int from) {
        return this.indexOf(val, from, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(byte val, int from, int def) {
        return this.indexOf(cur -> cur == val, from, def);
    }

    /* Index of: `BaseByteBuf` */

    public int indexOf(@NotNull B array) {
        return this.indexOf(array, 0, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(@NotNull B array, int from) {
        return this.indexOf(array, from, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(@NotNull B array, int from, int def) {
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

    /* Last index of: `IntPredicate` */

    public int lastIndexOf(@NotNull IntPredicate check) {
        return this.lastIndexOf(check, length() - 1, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(@NotNull IntPredicate check, int from) {
        return this.lastIndexOf(check, from, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(@NotNull IntPredicate check, int from, int def) {
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

    @NegativeIndexingSupported
    public int lastIndexOf(byte val, int from) {
        return this.lastIndexOf(val, from, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(byte val, int from, int def) {
        return this.lastIndexOf(cur -> cur == val, from, def);
    }

    /* Last index of: `BaseByteBuf` */

    public int lastIndexOf(@NotNull B array) {
        return this.lastIndexOf(array, length() - array.length(), -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(@NotNull B array, int from) {
        return this.lastIndexOf(array, from, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(@NotNull B array, int from, int def) {
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

    /* Contains */

    public boolean contains(byte val) {
        return indexOf(val) >= 0;
    }

    public boolean contains(@NotNull B array) {
        return indexOf(array) >= 0;
    }

    /* Split */

    public void split(byte val, @NotNull Consumer<B> callback) {
        int start = 0;
        while (true) {
            int end = indexOf(val, start);
            if (end == -1) {
                callback.accept(_wrap(bytes, this.start + start, this.end));
                return;
            } else {
                callback.accept(_wrap(bytes, this.start + start, this.start + end));
                start = end + 1;
            }
        }
    }

    public @NotNull List<B> split(byte val) {
        List<B> list = new ArrayList<>();
        split(val, list::add);
        return list;
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

    public boolean isPrefixOf(@NotNull B array) {
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

    public boolean isSuffixOf(@NotNull B array) {
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

    /* Remove suffix */

    public @NotNull B removeSuffix(@NotNull B suffix) {
        int len = commonSuffix(suffix);
        return len < suffix.length() ? _this() : _wrap(bytes, start, end - len);
    }

    public @NotNull B removeSuffix(byte val) {
        return endsWith(val) ? _wrap(bytes, start, end - 1) : _this();
    }

    /* Equality check */

    public boolean contentEquals(B that) {
        return Arrays.equals(bytes, start, end, that.bytes, that.start, that.end);
    }

    public boolean contentEquals(byte val) {
        return length() == 1 && at(0) == val;
    }

    /* Hash code */

    protected int hashCode(int seed, int l, int r) {
        int hash = seed;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + bytes[i]);
        }
        return hash;
    }

    protected int hashCode(int seed, int l, int r, @NotNull ByteFunc func) {
        int hash = seed;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + func.apply(bytes[i]));
        }
        return hash;
    }

    // FIX: Primitive-candidate
    protected interface ByteFunc {
        byte apply(byte val);
    }

    /* Helper */

    protected abstract @NotNull B _this();

    protected abstract @NotNull B _wrap(byte @NotNull[] bytes, int start, int end);
}
