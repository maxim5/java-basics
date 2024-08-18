package io.spbx.util.base;

import org.jetbrains.annotations.NotNull;

import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * A mutable version of the {@link CharArray}.
 */
public class MutableCharArray extends CharArray {
    protected MutableCharArray(char @NotNull[] chars, int start, int end) {
        super(chars, start, end);
    }

    public static @NotNull MutableCharArray wrap(char @NotNull[] chars) {
        return MutableCharArray.wrap(chars, 0, chars.length);
    }

    public static @NotNull MutableCharArray wrap(char @NotNull[] chars, int start, int end) {
        return new MutableCharArray(chars, start, end);
    }

    public static @NotNull MutableCharArray copyOf(char @NotNull[] chars) {
        return MutableCharArray.wrap(Arrays.copyOf(chars, chars.length));
    }

    public static @NotNull MutableCharArray copyOf(char @NotNull[] chars, int start, int end) {
        return MutableCharArray.wrap(Arrays.copyOfRange(chars, start, end));
    }

    public static @NotNull MutableCharArray of(@NotNull String s) {
        return MutableCharArray.of(s, 0, s.length());
    }

    public static @NotNull MutableCharArray of(@NotNull CharSequence s) {
        return s instanceof CharArray array ? MutableCharArray.of(array) : MutableCharArray.of(s.toString());
    }

    public static @NotNull MutableCharArray of(@NotNull CharArray array) {
        return MutableCharArray.wrap(array.chars, array.start, array.end);
    }

    public static @NotNull MutableCharArray of(@NotNull String s, int start, int end) {
        return MutableCharArray.wrap(s.toCharArray(), start, end);
    }

    public static @NotNull MutableCharArray of(@NotNull CharSequence s, int start, int end) {
        return MutableCharArray.of(s.toString(), start, end);
    }

    public static @NotNull MutableCharArray of(@NotNull CharBuffer buffer) {
        return MutableCharArray.wrap(buffer.isReadOnly() ? buffer.toString().toCharArray() : buffer.array(),
                                     buffer.isReadOnly() ? 0 : buffer.position(),
                                     buffer.isReadOnly() ? buffer.length() : buffer.position() + buffer.length());
    }

    public static @NotNull MutableCharArray asMutableCharArray(@NotNull CharSequence s) {
        return s instanceof CharArray array ? array.mutable() : MutableCharArray.of(s);
    }

    @Override
    public int start() {
        return start;
    }

    @Override
    public int end() {
        return end;
    }

    @Override
    public @NotNull CharBuffer asNioBuffer() {
        return asRawBuffer();
    }

    @Override
    public @NotNull MutableCharArray mutable() {
        return this;
    }

    @Override
    public @NotNull CharArray immutable() {
        return CharArray.wrap(chars, start, end);
    }

    public @NotNull MutableCharArray mutableSubstring(int start, int end) {
        start = start >= 0 ? start : start + length();  // allows negative from the end (-1 is `length()-1`)
        end = end >= 0 ? end : end + length();          // allows negative from the end (-1 is `length()-1`)
        assert start >= 0 && start <= length() : "Start index is out of range: %d in `%s`".formatted(start, this);
        assert end >= 0 && end <= length() : "End index is out of range: %d in `%s`".formatted(end, this);
        assert start <= end : "Start index can't be larger than end index: %d >= %d".formatted(start, end);
        return MutableCharArray.wrap(chars, this.start + start, this.start + end);
    }

    public void sliceInPlace(int start, int end) {
        start = start >= 0 ? start : start + length();  // allows negative from the end (-1 is `length()-1`)
        end = end >= 0 ? end : end + length();          // allows negative from the end (-1 is `length()-1`)
        assert start >= 0 && start <= length() : "Start index is out of range: %d in `%s`".formatted(start, this);
        assert end >= 0 && end <= length() : "End index is out of range: %d in `%s`".formatted(end, this);
        assert start <= end : "Start index can't be larger than end index: %d >= %d".formatted(start, end);
        this.end = this.start + end;
        this.start = this.start + start;
    }

    public void sliceFromInPlace(int start) {
        sliceInPlace(start, length());
    }

    public void sliceUntilInPlace(int end) {
        sliceInPlace(0, end);
    }

    public void reset() {
        start = 0;
        end = chars.length;
    }

    public void resetStart() {
        start = 0;
    }

    public void resetEnd() {
        end = chars.length;
    }

    public void offsetStart(int offset) {
        assert start + offset <= end : "Invalid offset: makes start=%d greater than end=%d".formatted(start + offset, end);
        start += offset;
    }

    public void offsetEnd(int offset) {
        assert start <= end - offset : "Invalid offset: makes start=%d greater than end=%d".formatted(start, end - offset);
        end -= offset;
    }

    public void offsetPrefix(CharArray prefix) {
        int len = commonPrefix(prefix);
        if (len == prefix.length()) {
            offsetStart(len);
        }
    }

    public void offsetPrefix(char ch) {
        if (startsWith(ch)) {
            offsetStart(1);
        }
    }

    public void offsetSuffix(CharArray suffix) {
        int len = commonSuffix(suffix);
        if (len == suffix.length()) {
            offsetEnd(len);
        }
    }

    public void offsetSuffix(char ch) {
        if (endsWith(ch)) {
            offsetEnd(1);
        }
    }

    public static @NotNull MutableCharArray join(@NotNull CharArray lhs, @NotNull CharArray rhs) {
        if (lhs.chars == rhs.chars && lhs.end == rhs.start) {
            return MutableCharArray.wrap(lhs.chars, lhs.start, rhs.end);
        }
        return MutableCharArray.of(new StringBuilder(lhs.length() + rhs.length()).append(lhs).append(rhs));
    }
}
