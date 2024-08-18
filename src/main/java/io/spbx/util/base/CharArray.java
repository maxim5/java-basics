package io.spbx.util.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * A String-like wrapper around <code>char[]</code> array, providing efficient slicing, searching,
 * for-each iteration and other operations.
 * <p>
 * {@link CharArray} provides immutable API but does not fully guarantee immutability because
 * it doesn't exclusively own the underlying char array. For example, {@link CharArray#wrap(char[])}
 * does not copy the native <code>char[]</code> for performance reasons, hence can be modified concurrently
 * outside the {@link CharArray} instance.
 * But {@link CharArray} itself treats both the native array and indices as immutable.
 *
 * @see MutableCharArray
 */
// FIX[minor]: unify negative indexes (ctor, indexOf)
public class CharArray implements CharSequence, Comparable<CharArray>, Serializable {
    public static final CharArray EMPTY = CharArray.wrap(new char[0]);

    protected final char[] chars;
    protected int start;
    protected int end;

    protected CharArray(char @NotNull[] chars, int start, int end) {
        assert 0 <= start : "CharArray start=%d can't be negative".formatted(start);
        assert start <= end : "CharArray start=%d is greater than end=%d".formatted(start, end);
        assert end <= chars.length : "CharArray end=%d is greater than array.length=%d".formatted(end, chars.length);
        this.chars = chars;
        this.start = start;
        this.end = end;
    }

    /* Construction */

    public static @NotNull CharArray wrap(char @NotNull[] chars) {
        return CharArray.wrap(chars, 0, chars.length);
    }

    public static @NotNull CharArray wrap(char @NotNull[] chars, int start, int end) {
        return new CharArray(chars, start, end);
    }

    public static @NotNull CharArray copyOf(char @NotNull[] chars) {
        return CharArray.wrap(Arrays.copyOf(chars, chars.length));
    }

    public static @NotNull CharArray copyOf(char @NotNull[] chars, int start, int end) {
        return CharArray.wrap(Arrays.copyOfRange(chars, start, end));
    }

    public static @NotNull CharArray of(@NotNull String s) {
        return CharArray.of(s, 0, s.length());
    }

    public static @NotNull CharArray of(@NotNull CharSequence s) {
        return s instanceof CharArray array ? CharArray.of(array) : CharArray.of(s.toString());
    }

    public static @NotNull CharArray of(@NotNull CharArray array) {
        return CharArray.wrap(array.chars, array.start, array.end);
    }

    public static @NotNull CharArray of(@NotNull String s, int start, int end) {
        return CharArray.wrap(s.toCharArray(), start, end);
    }

    public static @NotNull CharArray of(@NotNull CharSequence s, int start, int end) {
        return CharArray.of(s.toString(), start, end);
    }

    public static @NotNull CharArray of(char ch) {
        return CharArray.wrap(new char[] { ch });
    }

    public static @NotNull CharArray of(@NotNull CharBuffer buffer) {
        return CharArray.wrap(buffer.isReadOnly() ? buffer.toString().toCharArray() : buffer.array(),
                              buffer.isReadOnly() ? 0 : buffer.position(),
                              buffer.isReadOnly() ? buffer.length() : buffer.position() + buffer.length());
    }

    public static @NotNull CharArray asCharArray(@NotNull CharSequence s) {
        return s instanceof CharArray array ? array : CharArray.of(s);
    }

    /* Position pointers */

    @VisibleForTesting
    int start() {
        return start;
    }

    @VisibleForTesting
    int end() {
        return end;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public boolean isEmpty() {
        return start == end;
    }

    public boolean isNotEmpty() {
        return start < end;
    }

    /* Chars access */

    @Override
    public char charAt(int index) {
        assert index >= 0 : "Index can't be negative: %d".formatted(index);
        assert index < length() : "Index is out of bounds: %d".formatted(index);
        return chars[start + index];
    }

    public int at(int index) {
        index = index >= 0 ? index : index + length();  // allows negative from the end (-1 is `length()-1`)
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
        void accept(int index, char ch);
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
                cursor.ch = chars[start + index];
                index++;
                return cursor;
            }
        };
    }

    public static class CharCursor {
        public int index;
        public char ch;

        public int index() {
            return index;
        }
        public char ch() {
            return ch;
        }
    }

    @Override
    public @NotNull IntStream chars() {
        return asRawBuffer().chars();
    }

    @Override
    public @NotNull IntStream codePoints() {
        return asRawBuffer().codePoints();
    }

    /* Substring */

    public @NotNull CharArray substringFrom(int start) {
        return substring(start, length());
    }

    public @NotNull CharArray substringUntil(int end) {
        return substring(0, end);
    }

    public @NotNull CharArray substring(int start, int end) {
        start = start >= 0 ? start : start + length();  // allows negative from the end (-1 is `length()-1`)
        end = end >= 0 ? end : end + length();          // allows negative from the end (-1 is `length()-1`)
        assert start >= 0 && start <= length() : "Start index is out of range: %d in `%s`".formatted(start, this);
        assert end >= 0 && end <= length() : "End index is out of range: %d in `%s`".formatted(end, this);
        assert start <= end : "Start index can't be larger than end index: %d >= %d".formatted(start, end);
        return CharArray.wrap(chars, this.start + start, this.start + end);
    }

    @Override
    public @NotNull CharSequence subSequence(int start, int end) {
        return substring(start, end);
    }

    /* Starts-with and ends-with */

    public boolean startsWith(@NotNull CharArray prefix) {
        return length() >= prefix.length() &&
               Arrays.equals(chars, start, start + prefix.length(), prefix.chars, prefix.start, prefix.end);
    }

    public boolean endsWith(@NotNull CharArray suffix) {
        return length() >= suffix.length() &&
               Arrays.equals(chars, end - suffix.length(), end, suffix.chars, suffix.start, suffix.end);
    }

    public boolean startsWith(char ch) {
        return isNotEmpty() && chars[start] == ch;
    }

    public boolean endsWith(char ch) {
        return isNotEmpty() && chars[end - 1] == ch;
    }

    public boolean startsWith(@NotNull CharSequence prefix) {
        int length = prefix.length();
        if (length() < length) {
            return false;
        }
        int i = 0;
        while (i < length && chars[i + start] == prefix.charAt(i)) {
            i++;
        }
        return i == length;
    }

    public boolean endsWith(@NotNull CharSequence prefix) {
        int length = prefix.length();
        if (length() < length) {
            return false;
        }
        int i = 1;
        while (i <= length && chars[end - i] == prefix.charAt(length - i)) {
            i++;
        }
        return i > length;
    }

    /* Conversions */

    public @NotNull CharBuffer asNioBuffer() {
        return asRawBuffer().asReadOnlyBuffer();
    }

    protected @NotNull CharBuffer asRawBuffer() {
        return CharBuffer.wrap(chars, start, end - start);  // note: writable!
    }

    public @NotNull MutableCharArray mutable() {
        return mutableCopy();
    }

    public @NotNull MutableCharArray mutableCopy() {
        return MutableCharArray.wrap(chars, start, end);
    }

    public @NotNull CharArray immutable() {
        return this;
    }

    public @NotNull CharArray immutableCopy() {
        return CharArray.of(this);
    }

    /* Index of: char */

    public int indexOf(char ch) {
        return this.indexOf(ch, 0, -1);
    }

    public int indexOf(char ch, int from) {
        return this.indexOf(ch, from, -1);
    }

    public int indexOf(char ch, int from, int def) {
        return this.indexOf(cur -> cur == ch, from, def);
    }

    public int indexOfAny(char ch1, char ch2) {
        return this.indexOfAny(ch1, ch2, 0, -1);
    }

    public int indexOfAny(char ch1, char ch2, int from) {
        return this.indexOfAny(ch1, ch2, from, -1);
    }

    public int indexOfAny(char ch1, char ch2, int from, int def) {
        return this.indexOf(cur -> cur == ch1 || cur == ch2, from, def);
    }

    /* Index of: `CharArray` */

    public int indexOf(@NotNull CharArray array) {
        return this.indexOf(array, 0, -1);
    }

    public int indexOf(@NotNull CharArray array, int from) {
        return this.indexOf(array, from, -1);
    }

    public int indexOf(@NotNull CharArray array, int from, int def) {
        assert from >= 0 && from <= length() : "From index is out of array bounds: %d".formatted(from);
        assert def < 0 || def >= length() : "Default index can't be within array bounds: %d".formatted(def);
        int length = array.length();
        if (length() < length) {
            return def;
        }
        for (int i = start + from, max = end - length + 1; i < max; ++i) {
            if (Arrays.equals(chars, i, i + length, array.chars, array.start, array.end)) {
                return i - start;
            }
        }
        return def;
    }

    /* Index of: `CharSequence` */

    public int indexOf(@NotNull CharSequence str) {
        return this.indexOf(CharArray.of(str));
    }

    public int indexOf(@NotNull CharSequence str, int from) {
        return this.indexOf(CharArray.of(str), from);
    }

    public int indexOf(@NotNull CharSequence str, int from, int def) {
        return this.indexOf(CharArray.of(str), from, def);
    }

    /* Index of: regex `Pattern` */

    public @Nullable Matcher indexOf(@NotNull Pattern pattern) {
        return this.indexOf(pattern, 0);
    }

    public @Nullable Matcher indexOf(@NotNull Pattern pattern, int from) {
        Matcher matcher = pattern.matcher(this);
        return matcher.find(from) ? matcher : null;
    }

    /* Index of: `IntPredicate` */

    public int indexOf(@NotNull IntPredicate check) {
        return this.indexOf(check, 0, -1);
    }

    public int indexOf(@NotNull IntPredicate check, int from) {
        return this.indexOf(check, from, -1);
    }

    public int indexOf(@NotNull IntPredicate check, int from, int def) {
        assert from >= 0 && from <= length() : "From index is out of array bounds: %d".formatted(from);
        assert def < 0 || def >= length() : "Default index can't be within array bounds: %d".formatted(def);
        for (int i = start + from; i < end; ++i) {
            if (check.test(chars[i])) {
                return i - start;
            }
        }
        return def;
    }

    /* Last index of: char */

    public int lastIndexOf(char ch) {
        return this.lastIndexOf(ch, length() - 1, -1);
    }

    public int lastIndexOf(char ch, int from) {
        return this.lastIndexOf(ch, from, -1);
    }

    public int lastIndexOf(char ch, int from, int def) {
        return this.lastIndexOf(cur -> cur == ch, from, def);
    }

    public int lastIndexOfAny(char ch1, char ch2) {
        return this.lastIndexOfAny(ch1, ch2, length() - 1, -1);
    }

    public int lastIndexOfAny(char ch1, char ch2, int from) {
        return this.lastIndexOfAny(ch1, ch2, from, -1);
    }

    public int lastIndexOfAny(char ch1, char ch2, int from, int def) {
        return this.lastIndexOf(cur -> cur == ch1 || cur == ch2, from, def);
    }

    /* Last index of: `CharArray` */

    public int lastIndexOf(@NotNull CharArray array) {
        return this.lastIndexOf(array, length() - array.length(), -1);
    }

    public int lastIndexOf(@NotNull CharArray array, int from) {
        return this.lastIndexOf(array, from, -1);
    }

    public int lastIndexOf(@NotNull CharArray array, int from, int def) {
        assert from >= 0 && from <= length() : "From index is out of array bounds: %d".formatted(from);
        assert def < 0 || def >= length() : "Default index can't be within array bounds: %d".formatted(def);
        int length = array.length();
        if (length() < length) {
            return def;
        }
        for (int i = Math.min(start + from, end - length); i >= start; --i) {
            if (Arrays.equals(chars, i, i + length, array.chars, array.start, array.end)) {
                return i - start;
            }
        }
        return def;
    }

    /* Last index of: `CharSequence` */

    public int lastIndexOf(@NotNull CharSequence str) {
        return this.lastIndexOf(CharArray.of(str));
    }

    public int lastIndexOf(@NotNull CharSequence str, int from) {
        return this.lastIndexOf(CharArray.of(str), from, -1);
    }

    public int lastIndexOf(@NotNull CharSequence str, int from, int def) {
        return this.lastIndexOf(CharArray.of(str), from, def);
    }

    /* Last index of: `IntPredicate` */

    public int lastIndexOf(@NotNull IntPredicate check) {
        return this.lastIndexOf(check, length() - 1, -1);
    }

    public int lastIndexOf(@NotNull IntPredicate check, int from) {
        return this.lastIndexOf(check, from, -1);
    }

    public int lastIndexOf(@NotNull IntPredicate check, int from, int def) {
        assert from >= 0 && from <= length() : "From index is out of array bounds: %d".formatted(from);
        assert def < 0 || def >= length() : "Default index can't be within array bounds: %d".formatted(def);
        for (int i = Math.min(start + from, end - 1); i >= start; --i) {
            if (check.test(chars[i])) {
                return i - start;
            }
        }
        return def;
    }

    /* Contains */

    public boolean contains(char ch) {
        return indexOf(ch) >= 0;
    }

    public boolean contains(@NotNull CharArray array) {
        return indexOf(array) >= 0;
    }

    public boolean contains(@NotNull CharSequence str) {
        return indexOf(str) >= 0;
    }

    public boolean containsAny(char ch1, char ch2) {
        return indexOfAny(ch1, ch2) >= 0;
    }

    /* Split */

    public void split(char ch, @NotNull Consumer<CharArray> callback) {
        int start = 0;
        while (true) {
            int end = indexOf(ch, start);
            if (end == -1) {
                callback.accept(substringFrom(start));
                return;
            } else {
                callback.accept(substring(start, end));
                start = end + 1;
            }
        }
    }

    public @NotNull List<CharArray> split(char ch) {
        List<CharArray> list = new ArrayList<>();
        split(ch, list::add);
        return list;
    }

    /* Trim */

    public @NotNull CharArray trimStart(@NotNull IntPredicate check) {
        int i = start;
        while (i < end && check.test(chars[i])) {
            ++i;
        }
        return i == start ? this : CharArray.wrap(chars, i, end);
    }

    public @NotNull CharArray trimEnd(@NotNull IntPredicate check) {
        int i = end - 1;
        while (i >= start && check.test(chars[i])) {
            --i;
        }
        ++i;
        return i == end ? this : CharArray.wrap(chars, start, i);
    }

    public @NotNull CharArray trim(@NotNull IntPredicate check) {
        return trimStart(check).trimEnd(check);
    }

    public @NotNull CharArray trim(char ch) {
        return trim(value -> value == ch);
    }

    public @NotNull CharArray trim() {
        return trim(Character::isWhitespace);
    }

    /* Common prefix */

    // Returns the length of the common prefix
    public int commonPrefix(@NotNull CharArray array) {
        int index = Arrays.mismatch(chars, start, end, array.chars, array.start, array.end);
        return (index >= 0) ? index : length();
    }

    public int commonPrefix(@NotNull CharSequence str) {
        return commonPrefix(CharArray.of(str));
    }

    public boolean isPrefixOf(@NotNull CharArray array) {
        return commonPrefix(array) == length();
    }

    public boolean isPrefixOf(@NotNull CharSequence str) {
        return isPrefixOf(CharArray.of(str));
    }

    /* Common suffix */

    // Returns the length of the common suffix
    public int commonSuffix(@NotNull CharArray array) {
        int i = 1;
        int limit = Math.min(length(), array.length());
        while (i <= limit && chars[end - i] == array.chars[array.end - i]) {
            i++;
        }
        return i - 1;
    }

    public int commonSuffix(@NotNull CharSequence str) {
        return commonSuffix(CharArray.of(str));
    }

    public boolean isSuffixOf(@NotNull CharArray array) {
        return commonSuffix(array) == length();
    }

    public boolean isSuffixOf(@NotNull CharSequence str) {
        return isSuffixOf(CharArray.of(str));
    }

    /* Join */

    public static @NotNull CharArray join(@NotNull CharArray lhs, @NotNull CharArray rhs) {
        if (lhs.chars == rhs.chars && lhs.end == rhs.start) {
            return CharArray.wrap(lhs.chars, lhs.start, rhs.end);
        }
        return CharArray.of(new StringBuilder(lhs.length() + rhs.length()).append(lhs).append(rhs));
    }

    /* Remove prefix */

    public @NotNull CharArray cutPrefix(@NotNull CharArray prefix) {
        int len = commonPrefix(prefix);
        return len < prefix.length() ? this : substringFrom(len);
    }

    public @NotNull CharArray cutPrefix(@NotNull CharSequence prefix) {
        return cutPrefix(CharArray.of(prefix));
    }

    public @NotNull CharArray cutPrefix(char ch) {
        return startsWith(ch) ? substringFrom(1) : this;
    }

    /* Remove suffix */

    public @NotNull CharArray cutSuffix(@NotNull CharArray suffix) {
        int len = commonSuffix(suffix);
        return len < suffix.length() ? this : substringUntil(length() - len);
    }

    public @NotNull CharArray cutSuffix(@NotNull CharSequence suffix) {
        return cutSuffix(CharArray.of(suffix));
    }

    public @NotNull CharArray cutSuffix(char ch) {
        return endsWith(ch) ? substringUntil(length() - 1) : this;
    }

    /* Comparison */

    @Override
    public int compareTo(@NotNull CharArray that) {
        return Arrays.compare(chars, start, end, that.chars, that.start, that.end);
    }

    public int compareTo(@NotNull CharSequence str) {
        return CharSequence.compare(this, str);
    }

    /* To string */

    @Override
    public @NotNull String toString() {
        return new String(chars, start, end - start);
    }

    /* Equality check */

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof CharArray that && Arrays.equals(chars, start, end, that.chars, that.start, that.end);
    }

    public boolean contentEquals(char ch) {
        return length() == 1 && at(0) == ch;
    }

    public boolean contentEquals(@NotNull String str) {
        return str.contentEquals(this);
    }

    public boolean contentEquals(@NotNull CharSequence str) {
        if (length() != str.length()) {
            return false;
        }
        for (int i = 0, len = length(); i < len; ++i) {
            if (charAt(i) != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean contentEqualsIgnoreCase(@NotNull CharSequence str) {
        if (length() != str.length()) {
            return false;
        }
        for (int i = 0, len = length(); i < len; ++i) {
            if (Character.toLowerCase(charAt(i)) != Character.toLowerCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* Hash code */

    @Override
    public int hashCode() {
        int hash = 31;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << 5) + (hash >> 2) + chars[i]);
        }
        return hash;
    }

    public int hashCodeIgnoreCase() {
        int hash = 31;
        for (int i = start; i < end; ++i) {
            hash = hash ^ ((hash << 5) + (hash >> 2) + Character.toLowerCase(chars[i]));
        }
        return hash;
    }
}
