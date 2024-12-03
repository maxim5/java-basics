package io.spbx.util.base.str;

import io.spbx.util.base.annotate.NegativeIndexingSupported;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;
import org.jetbrains.annotations.NotNull;

import java.nio.CharBuffer;
import java.util.Arrays;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

/**
 * A mutable version of the {@link CharArray}.
 */
public class MutableCharArray extends CharArray {
    protected MutableCharArray(char @NotNull[] chars, int start, int end) {
        super(chars, start, end);
    }

    public static @NotNull MutableCharArray wrap(char @NotNull[] chars) {
        return new MutableCharArray(chars, 0, chars.length);
    }

    @NegativeIndexingSupported
    public static @NotNull MutableCharArray wrap(char @NotNull[] chars, int start, int end) {
        assert RangeCheck.with(chars.length, wrap(chars)).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return new MutableCharArray(chars,
                                    LowLevel.translateIndex(start, chars.length),
                                    LowLevel.translateIndex(end, chars.length));
    }

    public static @NotNull MutableCharArray copyOf(char @NotNull[] chars) {
        return MutableCharArray.wrap(Arrays.copyOf(chars, chars.length));
    }

    @NegativeIndexingSupported
    public static @NotNull MutableCharArray copyOf(char @NotNull[] chars, int start, int end) {
        assert RangeCheck.with(chars.length, wrap(chars)).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return MutableCharArray.wrap(
            Arrays.copyOfRange(chars,
                               LowLevel.translateIndex(start, chars.length),
                               LowLevel.translateIndex(end, chars.length))
        );
    }

    public static @NotNull MutableCharArray of(@NotNull String s) {
        return MutableCharArray.of(s, 0, s.length());
    }

    public static @NotNull MutableCharArray of(@NotNull CharSequence s) {
        return s instanceof CharArray array ? MutableCharArray.of(array) : MutableCharArray.of(s.toString());
    }

    public static @NotNull MutableCharArray of(@NotNull CharArray array) {
        return array.mutableCopy();
    }

    @NegativeIndexingSupported
    public static @NotNull MutableCharArray of(@NotNull String s, int start, int end) {
        assert RangeCheck.with(s.length(), s).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return MutableCharArray.wrap(s.toCharArray(),
                                     LowLevel.translateIndex(start, s.length()),
                                     LowLevel.translateIndex(end, s.length()));
    }

    @NegativeIndexingSupported
    public static @NotNull MutableCharArray of(@NotNull CharSequence s, int start, int end) {
        assert RangeCheck.with(s.length(), s).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return MutableCharArray.of(s.toString(),
                                   LowLevel.translateIndex(start, s.length()),
                                   LowLevel.translateIndex(end, s.length()));
    }

    public static @NotNull MutableCharArray of(@NotNull CharBuffer buffer) {
        return MutableCharArray.wrap(buffer.isReadOnly() ? buffer.toString().toCharArray() : buffer.array(),
                                     buffer.isReadOnly() ? 0 : buffer.position(),
                                     buffer.isReadOnly() ? buffer.length() : buffer.position() + buffer.length());
    }

    public static @NotNull MutableCharArray of(char ch) {
        return MutableCharArray.wrap(new char[] { ch });
    }

    public static @NotNull MutableCharArray asMutableCharArray(@NotNull CharSequence s) {
        return s instanceof CharArray array ? array.mutable() : MutableCharArray.of(s);
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

    @NegativeIndexingSupported
    public @NotNull MutableCharArray mutableSubstring(int start, int end) {
        assert rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return MutableCharArray.wrap(chars, this.start + translateIndex(start), this.start + translateIndex(end));
    }

    @NegativeIndexingSupported
    public void sliceInPlace(int start, int end) {
        assert rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        this.end = this.start + translateIndex(end);
        this.start = this.start + translateIndex(start);
    }

    @NegativeIndexingSupported
    public void sliceFromInPlace(int start) {
        sliceInPlace(start, length());
    }

    @NegativeIndexingSupported
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

    public void offsetPrefix(@NotNull CharArray prefix) {
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

    public void offsetSuffix(@NotNull CharArray suffix) {
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
        // One more unnecessary object creation, but otherwise need to expose `chars`, `start` and `end`.
        return CharArray.join(lhs, rhs).mutableCopy();
    }
}
