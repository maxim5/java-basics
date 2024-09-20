package io.spbx.util.buf;

import io.spbx.util.annotate.NegativeIndexingSupported;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Consumer;

/**
 * A base class for the {@code char} array buffer.
 * <p>
 * {@link BaseCharBuf} supports python-style negative indexing, e.g. {@code buf.at(-2)} is equivalent to
 * {@code buf.at(buf.length()-2)}.
 */
@NegativeIndexingSupported
@Generated(value = "Base$Type$Buf.java", date = "2024-09-20T11:06:19.295010037Z")
public abstract class BaseCharBuf<B extends BaseCharBuf> extends BaseBuf implements Comparable<B>, Serializable {
    protected final char[] chars;
    protected /* final */ int start;
    protected /* final */ int end;

    protected BaseCharBuf(char @NotNull[] chars, int start, int end) {
        assert 0 <= start : "Start index %d out of bounds".formatted(start);
        assert start <= end : "Start index can't be larger than end index: %d > %d".formatted(start, end);
        assert end <= chars.length : "End index %d out of bounds [%d, %d]".formatted(end, 0, chars.length);
        this.chars = chars;
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

    /* Chars access */

    @NegativeIndexingSupported
    public char charAt(int index) {
        assert rangeCheck(index, BEFORE_TRANSLATION | OPEN_END_RANGE);
        return chars[start + translateIndex(index)];
    }

    @NegativeIndexingSupported
    public int at(int index) {
        index = translateIndex(index);
        return index >= 0 && index < length() ? chars[start + index] : -1;
    }

    public <T extends IntConsumer> @NotNull T forEach(@NotNull T consumer) {
        for (int i = start; i < end; ++i) {
            consumer.accept(chars[i]);
        }
        return consumer;
    }

    public <T extends IndexedCharConsumer> @NotNull T forEachIndexed(@NotNull T consumer) {
        for (int index = 0, i = start; i < end; ++index, ++i) {
            consumer.accept(index, chars[i]);
        }
        return consumer;
    }

    // FIX: Primitive-candidate
    public interface IndexedCharConsumer {
        void accept(int index, char val);
    }

    public @NotNull Iterable<CharCursor> toIterable() {
        return this::iterator;
    }

    public @NotNull Iterator<CharCursor> iterator() {
        return new Iterator<>() {
            private final CharCursor cursor = new CharCursor();
            private final int len = length();
            private int index = 0;

            @Override public boolean hasNext() {
                return index < len;
            }

            @Override public @NotNull CharCursor next() {
                cursor.index = index;
                cursor.val = chars[start + index];
                index++;
                return cursor;
            }
        };
    }

    public static class CharCursor {
        public int index;
        public char val;

        public int index() {
            return index;
        }
        public char val() {
            return val;
        }
    }

    public @NotNull CharBuffer asNioBuffer() {
        return asRawBuffer().asReadOnlyBuffer();
    }

    protected @NotNull CharBuffer asRawBuffer() {
        return CharBuffer.wrap(chars, start, end - start);  // note: writable!
    }

    /* Starts-with and ends-with */

    public boolean startsWith(@NotNull B prefix) {
        return length() >= prefix.length() &&
               Arrays.equals(chars, start, start + prefix.length(), prefix.chars, prefix.start, prefix.end);
    }

    public boolean endsWith(@NotNull B suffix) {
        return length() >= suffix.length() &&
               Arrays.equals(chars, end - suffix.length(), end, suffix.chars, suffix.start, suffix.end);
    }

    public boolean startsWith(char[] prefix) {
        return length() >= prefix.length && Arrays.equals(chars, start, start + prefix.length, prefix, 0, prefix.length);
    }

    public boolean endsWith(char[] suffix) {
        return length() >= suffix.length && Arrays.equals(chars, end - suffix.length, end, suffix, 0, suffix.length);
    }

    public boolean startsWith(char val) {
        return isNotEmpty() && chars[start] == val;
    }

    public boolean endsWith(char val) {
        return isNotEmpty() && chars[end - 1] == val;
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
            if (check.test(chars[i])) {
                return i - start;
            }
        }
        return def;
    }

    /* Index of: char */

    public int indexOf(char val) {
        return this.indexOf(val, 0, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(char val, int from) {
        return this.indexOf(val, from, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(char val, int from, int def) {
        return this.indexOf(cur -> cur == val, from, def);
    }

    /* Index of: `BaseCharBuf` */

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
            if (Arrays.equals(chars, i, i + length, array.chars, array.start, array.end)) {
                return i - start;
            }
        }
        return def;
    }

    /* Index of: `char[]` */

    public int indexOf(char[] array) {
        return this.indexOf(array, 0, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(char[] array, int from) {
        return this.indexOf(array, from, -1);
    }

    @NegativeIndexingSupported
    public int indexOf(char[] array, int from, int def) {
        return this.indexOf(_wrap(array, 0, array.length), from, def);
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
            if (check.test(chars[i])) {
                return i - start;
            }
        }
        return def;
    }

    /* Last index of: char */

    public int lastIndexOf(char val) {
        return this.lastIndexOf(val, length() - 1, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(char val, int from) {
        return this.lastIndexOf(val, from, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(char val, int from, int def) {
        return this.lastIndexOf(cur -> cur == val, from, def);
    }

    /* Last index of: `BaseCharBuf` */

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
            if (Arrays.equals(chars, i, i + length, array.chars, array.start, array.end)) {
                return i - start;
            }
        }
        return def;
    }

    /* Last index of: `char[]` */

    public int lastIndexOf(char[] array) {
        return this.lastIndexOf(array, length() - 1, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(char[] array, int from) {
        return this.lastIndexOf(array, from, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(char[] array, int from, int def) {
        return this.lastIndexOf(_wrap(array, 0, array.length), from, def);
    }

    /* Contains */

    public boolean contains(char val) {
        return indexOf(val) >= 0;
    }

    public boolean contains(@NotNull B array) {
        return indexOf(array) >= 0;
    }

    public boolean contains(char[] val) {
        return indexOf(val) >= 0;
    }

    /* Split */

    public @NotNull Iterator<B> split(char val) {
        return split(start -> indexOf(val, start), 1);
    }

    public @NotNull Iterator<B> split(@NotNull B array) {
        assert array.isNotEmpty() : "Separator is empty";
        return split(start -> indexOf(array, start), array.length());
    }

    public @NotNull Iterator<B> split(char[] val) {
        assert val.length > 0 : "Separator is empty";
        return split(start -> indexOf(val, start), val.length);
    }

    public void split(char val, @NotNull Consumer<B> callback) {
        split(start -> indexOf(val, start), callback, 1);
    }

    public void split(@NotNull B array, @NotNull Consumer<B> callback) {
        assert array.isNotEmpty() : "Separator is empty";
        split(start -> indexOf(array, start), callback, array.length());
    }

    public void split(char[] val, @NotNull Consumer<B> callback) {
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
                B buf = _wrap(chars, start + index, start + next);
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
                callback.accept(_wrap(chars, this.start + start, this.end));
                return;
            } else {
                callback.accept(_wrap(chars, this.start + start, this.start + index));
                start = index + separatorLength;
            }
        }
    }

    /* Trim */

    public @NotNull B trimStart(@NotNull IntPredicate check) {
        int i = start;
        while (i < end && check.test(chars[i])) {
            ++i;
        }
        return i == start ? _this() : _wrap(chars, i, end);
    }

    public @NotNull B trimEnd(@NotNull IntPredicate check) {
        int i = end - 1;
        while (i >= start && check.test(chars[i])) {
            --i;
        }
        ++i;
        return i == end ? _this() : _wrap(chars, start, i);
    }

    public @NotNull B trim(@NotNull IntPredicate check) {
        return (B) trimStart(check).trimEnd(check);
    }

    public @NotNull B trim(char val) {
        return trim(value -> value == val);
    }

    /* Common prefix */

    // Returns the length of the common prefix
    public int commonPrefix(@NotNull B array) {
        int index = Arrays.mismatch(chars, start, end, array.chars, array.start, array.end);
        return (index >= 0) ? index : length();
    }

    // Returns the length of the common prefix
    public int commonPrefix(char[] array) {
        int index = Arrays.mismatch(chars, start, end, array, 0, array.length);
        return (index >= 0) ? index : length();
    }

    public boolean isPrefixOf(@NotNull B array) {
        return commonPrefix(array) == length();
    }

    public boolean isPrefixOf(char[] array) {
        return commonPrefix(array) == length();
    }

    /* Common suffix */

    // Returns the length of the common suffix
    public int commonSuffix(@NotNull B array) {
        int i = 1;
        int limit = Math.min(length(), array.length());
        while (i <= limit && chars[end - i] == array.chars[array.end - i]) {
            i++;
        }
        return i - 1;
    }

    // Returns the length of the common suffix
    public int commonSuffix(char[] array) {
        int i = 1;
        int limit = Math.min(length(), array.length);
        while (i <= limit && chars[end - i] == array[array.length - i]) {
            i++;
        }
        return i - 1;
    }

    public boolean isSuffixOf(@NotNull B array) {
        return commonSuffix(array) == length();
    }

    public boolean isSuffixOf(char[] array) {
        return commonSuffix(array) == length();
    }

    /* Remove prefix */

    public @NotNull B removePrefix(@NotNull B prefix) {
        int len = commonPrefix(prefix);
        return len < prefix.length() ? _this() : _wrap(chars, start + len, end);
    }

    public @NotNull B removePrefix(char val) {
        return startsWith(val) ? _wrap(chars, start + 1, end) : _this();
    }

    public @NotNull B removePrefix(char[] prefix) {
        return startsWith(prefix) ? _wrap(chars, start + prefix.length, end) : _this();
    }

    /* Remove suffix */

    public @NotNull B removeSuffix(@NotNull B suffix) {
        int len = commonSuffix(suffix);
        return len < suffix.length() ? _this() : _wrap(chars, start, end - len);
    }

    public @NotNull B removeSuffix(char val) {
        return endsWith(val) ? _wrap(chars, start, end - 1) : _this();
    }

    public @NotNull B removeSuffix(char[] prefix) {
        return endsWith(prefix) ? _wrap(chars, start, end - prefix.length) : _this();
    }

    /* Comparison */

    @Override
    public int compareTo(@NotNull B that) {
        return Arrays.compare(chars, start, end, that.chars, that.start, that.end);
    }

    public int compareTo(@NotNull char[] array) {
        return Arrays.compare(chars, start, end, array, 0, array.length);
    }

    /* Equality check */

    public boolean contentEquals(B that) {
        return Arrays.equals(chars, start, end, that.chars, that.start, that.end);
    }

    public boolean contentEquals(char val) {
        return length() == 1 && at(0) == val;
    }

    public boolean contentEquals(char[] that) {
        return Arrays.equals(chars, start, end, that, 0, that.length);
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

    public static int hashCode(char[] chars) {
        return hashCode(chars, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static int hashCode(char[] chars, int start, int end) {
        return hashCode(chars, start, end, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static int hashCode(char[] chars, int start, int end, @NotNull CharFunc func) {
        return hashCode(chars, start, end, func, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static <T> int hashCode(@NotNull T instance, int len, @NotNull CharAtFunction<T> charAt) {
        return hashCode(instance, len, charAt, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    // FIX: Primitive-candidate
    public interface CharFunc {
        char apply(char val);
    }

    // FIX: Primitive-candidate
    public interface CharAtFunction<T> {
        int charAt(@NotNull T instance, int index);
    }

    protected static int hashCode(char[] chars, int seed, int l, int r) {
        int hash = seed;
        for (char val : chars) {
            hash = hash ^ ((hash << l) + (hash >> r) + val);
        }
        return hash;
    }

    protected static int hashCode(char[] chars, int start, int end, int seed, int l, int r) {
        int hash = seed;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + chars[i]);
        }
        return hash;
    }

    protected static int hashCode(char[] chars, int start, int end, @NotNull CharFunc func, int seed, int l, int r) {
        int hash = seed;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + func.apply(chars[i]));
        }
        return hash;
    }

    protected static <T> int hashCode(@NotNull T instance, int len, @NotNull CharAtFunction<T> charAtFunc,
                                      int seed, int l, int r) {
        int hash = seed;
        for (int i = 0; i < len; ++i) {
            hash = hash ^ ((hash << l) + (hash >> r) + charAtFunc.charAt(instance, i));
        }
        return hash;
    }

    /* Helper */

    protected abstract @NotNull B _this();

    protected abstract @NotNull B _wrap(char @NotNull[] chars, int start, int end);
}
