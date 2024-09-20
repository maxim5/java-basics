package io.spbx.util.array;

import io.spbx.util.annotate.NegativeIndexingSupported;
import io.spbx.util.buf.BaseCharBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * A String-like wrapper around {@code char[]} array, providing efficient slicing, searching,
 * for-each iteration and other operations.
 * <p>
 * {@link CharArray} supports python-style negative indexing, e.g. {@code array.substring(0, -2)} is equivalent to
 * {@code array.substring(0, array.length()-2)}.
 * <p>
 * {@link CharArray} provides immutable API but does not fully guarantee immutability because
 * it doesn't exclusively own the underlying char array. For example, {@link CharArray#wrap(char[])}
 * does not copy the native {@code char[]} for performance reasons, hence can be modified concurrently
 * outside the {@link CharArray} instance.
 * But {@link CharArray} itself treats both the native array and indices as immutable.
 *
 * @see MutableCharArray
 */
@NegativeIndexingSupported
public class CharArray extends BaseCharBuf<CharArray> implements CharSequence {
    public static final CharArray EMPTY = CharArray.wrap(new char[0]);

    protected CharArray(char @NotNull[] chars, int start, int end) {
        super(chars, start, end);
    }

    /* Construction */

    public static @NotNull CharArray wrap(char @NotNull[] chars) {
        return new CharArray(chars, 0, chars.length);
    }

    @NegativeIndexingSupported
    public static @NotNull CharArray wrap(char @NotNull[] chars, int start, int end) {
        assert rangeCheck(start, end, chars.length, wrap(chars), BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return new CharArray(chars,
                             translateIndex(start, chars.length),
                             translateIndex(end, chars.length));
    }

    public static @NotNull CharArray copyOf(char @NotNull[] chars) {
        return CharArray.wrap(Arrays.copyOf(chars, chars.length));
    }

    @NegativeIndexingSupported
    public static @NotNull CharArray copyOf(char @NotNull[] chars, int start, int end) {
        assert rangeCheck(start, end, chars.length, wrap(chars), BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return CharArray.wrap(
            Arrays.copyOfRange(chars,
                               translateIndex(start, chars.length),
                               translateIndex(end, chars.length))
        );
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

    @NegativeIndexingSupported
    public static @NotNull CharArray of(@NotNull String s, int start, int end) {
        assert rangeCheck(start, end, s.length(), s, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return CharArray.wrap(s.toCharArray(),
                              translateIndex(start, s.length()),
                              translateIndex(end, s.length()));
    }

    @NegativeIndexingSupported
    public static @NotNull CharArray of(@NotNull CharSequence s, int start, int end) {
        assert rangeCheck(start, end, s.length(), s.length(), BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return CharArray.of(s.toString(),
                            translateIndex(start, s.length()),
                            translateIndex(end, s.length()));
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

    /* Substring */

    @NegativeIndexingSupported
    public @NotNull CharArray substringFrom(int start) {
        return substring(start, length());
    }

    @NegativeIndexingSupported
    public @NotNull CharArray substringUntil(int end) {
        return substring(0, end);
    }

    @NegativeIndexingSupported
    public @NotNull CharArray substring(int start, int end) {
        assert rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return CharArray.wrap(chars, translateIndex(start) + this.start, this.start + translateIndex(end));
    }

    @Override
    @NegativeIndexingSupported
    public @NotNull CharSequence subSequence(int start, int end) {
        return substring(start, end);
    }

    /* Starts-with and ends-with */

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

    @Override
    public @NotNull IntStream chars() {
        return asRawBuffer().chars();
    }

    @Override
    public @NotNull IntStream codePoints() {
        return asRawBuffer().codePoints();
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

    /* Index of: `CharSequence` */

    public int indexOf(@NotNull CharSequence str) {
        return this.indexOf(CharArray.of(str));
    }

    @NegativeIndexingSupported
    public int indexOf(@NotNull CharSequence str, int from) {
        return this.indexOf(CharArray.of(str), from);
    }

    @NegativeIndexingSupported
    public int indexOf(@NotNull CharSequence str, int from, int def) {
        return this.indexOf(CharArray.of(str), from, def);
    }

    /* Index of: regex `Pattern` */

    public @Nullable Matcher indexOf(@NotNull Pattern pattern) {
        return this.indexOf(pattern, 0);
    }

    @NegativeIndexingSupported
    public @Nullable Matcher indexOf(@NotNull Pattern pattern, int from) {
        assert rangeCheck(from, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        Matcher matcher = pattern.matcher(this);
        return matcher.find(translateIndex(from)) ? matcher : null;
    }

    /* Last index of: `CharSequence` */

    public int lastIndexOf(@NotNull CharSequence str) {
        return this.lastIndexOf(CharArray.of(str));
    }

    @NegativeIndexingSupported
    public int lastIndexOf(@NotNull CharSequence str, int from) {
        return this.lastIndexOf(CharArray.of(str), from, -1);
    }

    @NegativeIndexingSupported
    public int lastIndexOf(@NotNull CharSequence str, int from, int def) {
        return this.lastIndexOf(CharArray.of(str), from, def);
    }

    /* Contains */

    public boolean contains(@NotNull CharSequence str) {
        return indexOf(str) >= 0;
    }

    public boolean contains(@NotNull Pattern pattern) {
        return indexOf(pattern) != null;
    }

    /* Split */

    public @NotNull Iterator<CharArray> split(@NotNull CharSequence str) {
        assert !str.isEmpty() : "Separator is empty";
        return split(start -> indexOf(str, start), str.length());
    }

    public void split(@NotNull CharSequence str, @NotNull Consumer<CharArray> callback) {
        assert !str.isEmpty() : "Separator is empty";
        split(start -> indexOf(str, start), callback, str.length());
    }

    public @NotNull Iterator<CharArray> split(@NotNull Pattern pattern) {
        return new Iterator<>() {
            private final Matcher matcher = pattern.matcher(CharArray.this);
            private final int len = length();
            private int index = 0;
            @Override public boolean hasNext() {
                return index <= len;
            }
            @Override public CharArray next() {
                assert index <= len : "No more elements to iterate";
                if (matcher.find(index)) {
                    assert matcher.start() < matcher.end() : "Empty match at " + matcher.start();
                    CharArray buf = _wrap(chars, start + index, start + matcher.start());
                    index = matcher.end();
                    return buf;
                } else {
                    CharArray buf = _wrap(chars, start + index, end);
                    index = len + 1;
                    return buf;
                }
            }
        };
    }

    // https://stackoverflow.com/questions/426397/do-line-endings-differ-between-windows-and-linux
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\r\n|\n|\r");

    public @NotNull Iterator<CharArray> lines() {
        return split(NEW_LINE_PATTERN);
    }

    /* Trim */

    public @NotNull CharArray trim() {
        return trim(Character::isWhitespace);
    }

    /* Common prefix */

    public int commonPrefix(@NotNull CharSequence str) {
        return commonPrefix(CharArray.of(str));
    }

    public boolean isPrefixOf(@NotNull CharSequence str) {
        return isPrefixOf(CharArray.of(str));
    }

    /* Common suffix */

    public int commonSuffix(@NotNull CharSequence str) {
        return commonSuffix(CharArray.of(str));
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

    public @NotNull CharArray removePrefix(@NotNull CharSequence prefix) {
        return removePrefix(CharArray.of(prefix));
    }

    /* Remove suffix */

    public @NotNull CharArray removeSuffix(@NotNull CharSequence suffix) {
        return removeSuffix(CharArray.of(suffix));
    }

    /* Comparison */

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
        return this == o || o instanceof CharArray that && contentEquals(that);
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
        return hashCode(chars, start, end);
    }

    public int hashCodeIgnoreCase() {
        return hashCode(chars, start, end, Character::toLowerCase);
    }

    /* Helpers */

    final char[] _chars() {
        return chars;
    }

    @Override protected final @NotNull CharArray _this() {
        return this;
    }

    @Override protected final @NotNull CharArray _wrap(char @NotNull[] chars, int start, int end) {
        return CharArray.wrap(chars, start, end);
    }
}
