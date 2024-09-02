package io.spbx.util.array;

import io.spbx.util.annotate.NegativeIndexingSupported;
import io.spbx.util.buf.BaseByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@NegativeIndexingSupported
public class AsciiByteArray extends BaseByteBuf<AsciiByteArray> implements CharSequence, Comparable<AsciiByteArray>, Serializable {
    public static final AsciiByteArray EMPTY = AsciiByteArray.wrap(new byte[0]);

    protected AsciiByteArray(byte @NotNull[] bytes, int start, int end) {
        super(bytes, start, end);
    }

    /* Construction */

    public static @NotNull AsciiByteArray wrap(byte @NotNull[] bytes) {
        return new AsciiByteArray(bytes, 0, bytes.length);
    }

    @NegativeIndexingSupported
    public static @NotNull AsciiByteArray wrap(byte @NotNull[] bytes, int start, int end) {
        assert rangeCheck(start, end, bytes.length, wrap(bytes), BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return new AsciiByteArray(bytes,
                                  translateIndex(start, bytes.length),
                                  translateIndex(end, bytes.length));
    }

    public static @NotNull AsciiByteArray copyOf(byte @NotNull[] bytes) {
        return AsciiByteArray.wrap(Arrays.copyOf(bytes, bytes.length));
    }

    @NegativeIndexingSupported
    public static @NotNull AsciiByteArray copyOf(byte @NotNull[] bytes, int start, int end) {
        assert rangeCheck(start, end, bytes.length, wrap(bytes), BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.wrap(
            Arrays.copyOfRange(bytes,
                               translateIndex(start, bytes.length),
                               translateIndex(end, bytes.length))
        );
    }

    public static @NotNull AsciiByteArray of(@NotNull String s) {
        return AsciiByteArray.of(s, 0, s.length());
    }

    public static @NotNull AsciiByteArray of(@NotNull CharSequence s) {
        return s instanceof AsciiByteArray array ? AsciiByteArray.of(array) : AsciiByteArray.of(s.toString());
    }

    public static @NotNull AsciiByteArray of(@NotNull AsciiByteArray array) {
        return AsciiByteArray.wrap(array.bytes, array.start, array.end);
    }

    @NegativeIndexingSupported
    public static @NotNull AsciiByteArray of(@NotNull String s, int start, int end) {
        assert rangeCheck(start, end, s.length(), s, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.wrap(s.getBytes(StandardCharsets.US_ASCII),
                                   translateIndex(start, s.length()),
                                   translateIndex(end, s.length()));
    }

    @NegativeIndexingSupported
    public static @NotNull AsciiByteArray of(@NotNull CharSequence s, int start, int end) {
        assert rangeCheck(start, end, s.length(), s.length(), BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.of(s.toString(),
                                 translateIndex(start, s.length()),
                                 translateIndex(end, s.length()));
    }

    public static @NotNull AsciiByteArray of(byte b) {
        return AsciiByteArray.wrap(new byte[] { b });
    }

    public static @NotNull AsciiByteArray asAsciiByteArray(@NotNull CharSequence s) {
        return s instanceof AsciiByteArray array ? array : AsciiByteArray.of(s);
    }

    /* Chars access */

    @Override
    @NegativeIndexingSupported
    public char charAt(int index) {
        return (char) byteAt(index);
    }

    /* Substring */

    @NegativeIndexingSupported
    public @NotNull AsciiByteArray substringFrom(int start) {
        return substring(start, length());
    }

    @NegativeIndexingSupported
    public @NotNull AsciiByteArray substringUntil(int end) {
        return substring(0, end);
    }

    @NegativeIndexingSupported
    public @NotNull AsciiByteArray substring(int start, int end) {
        assert rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.wrap(bytes, translateIndex(start) + this.start, this.start + translateIndex(end));
    }

    @Override
    @NegativeIndexingSupported
    public @NotNull CharSequence subSequence(int start, int end) {
        return substring(start, end);
    }

    /* Comparison */

    @Override
    public int compareTo(@NotNull AsciiByteArray that) {
        return Arrays.compare(bytes, start, end, that.bytes, that.start, that.end);
    }

    public int compareTo(@NotNull CharSequence str) {
        return CharSequence.compare(this, str);
    }

    /* To string */

    @Override
    public @NotNull String toString() {
        return new String(bytes, start, end - start, StandardCharsets.US_ASCII);
    }

    /* Equality check */

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof AsciiByteArray that && contentEquals(that);
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

    /* Hash code */

    @Override
    public int hashCode() {
        return hashCode(bytes, start, end);
    }

    /* Helpers */

    final byte[] _bytes() {
        return bytes;
    }

    @Override protected @NotNull AsciiByteArray _this() {
        return this;
    }

    @Override protected @NotNull AsciiByteArray _wrap(byte @NotNull[] bytes, int start, int end) {
        return AsciiByteArray.wrap(bytes, start, end);
    }
}
