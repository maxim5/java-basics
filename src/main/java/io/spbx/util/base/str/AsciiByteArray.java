package io.spbx.util.base.str;

import io.spbx.util.base.annotate.AllowPythonIndexing;
import io.spbx.util.base.annotate.PyIndex;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;
import io.spbx.util.base.ops.ByteOps;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

@AllowPythonIndexing
public class AsciiByteArray extends BaseByteBuf<AsciiByteArray> implements CharSequence {
    public static final AsciiByteArray EMPTY = AsciiByteArray.wrap(ByteOps.EMPTY_ARRAY);

    protected AsciiByteArray(byte @NotNull[] bytes, int start, int end) {
        super(bytes, start, end);
    }

    /* Construction */

    public static @NotNull AsciiByteArray wrap(byte @NotNull[] bytes) {
        return new AsciiByteArray(bytes, 0, bytes.length);
    }

    @AllowPythonIndexing
    public static @NotNull AsciiByteArray wrap(byte @NotNull[] bytes, @PyIndex int start, @PyIndex int end) {
        assert RangeCheck.with(bytes.length, wrap(bytes)).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return new AsciiByteArray(bytes,
                                  LowLevel.translateIndex(start, bytes.length),
                                  LowLevel.translateIndex(end, bytes.length));
    }

    public static @NotNull AsciiByteArray copyOf(byte @NotNull[] bytes) {
        return AsciiByteArray.wrap(Arrays.copyOf(bytes, bytes.length));
    }

    @AllowPythonIndexing
    public static @NotNull AsciiByteArray copyOf(byte @NotNull[] bytes, @PyIndex int start, @PyIndex int end) {
        assert RangeCheck.with(bytes.length, wrap(bytes)).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.wrap(
            Arrays.copyOfRange(bytes,
                               LowLevel.translateIndex(start, bytes.length),
                               LowLevel.translateIndex(end, bytes.length))
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

    @AllowPythonIndexing
    public static @NotNull AsciiByteArray of(@NotNull String s, @PyIndex int start, @PyIndex int end) {
        assert RangeCheck.with(s.length(), s).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.wrap(s.getBytes(StandardCharsets.US_ASCII),
                                   LowLevel.translateIndex(start, s.length()),
                                   LowLevel.translateIndex(end, s.length()));
    }

    @AllowPythonIndexing
    public static @NotNull AsciiByteArray of(@NotNull CharSequence s, @PyIndex int start, @PyIndex int end) {
        assert RangeCheck.with(s.length(), s).rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.of(s.toString(),
                                 LowLevel.translateIndex(start, s.length()),
                                 LowLevel.translateIndex(end, s.length()));
    }

    public static @NotNull AsciiByteArray of(byte b) {
        return AsciiByteArray.wrap(new byte[] { b });
    }

    public static @NotNull AsciiByteArray asAsciiByteArray(@NotNull CharSequence s) {
        return s instanceof AsciiByteArray array ? array : AsciiByteArray.of(s);
    }

    /* Chars access */

    @Override
    @AllowPythonIndexing
    public char charAt(@PyIndex int index) {
        return (char) byteAt(index);
    }

    /* Substring */

    @AllowPythonIndexing
    public @NotNull AsciiByteArray substringFrom(@PyIndex int start) {
        return substring(start, length());
    }

    @AllowPythonIndexing
    public @NotNull AsciiByteArray substringUntil(@PyIndex int end) {
        return substring(0, end);
    }

    @AllowPythonIndexing
    public @NotNull AsciiByteArray substring(@PyIndex int start, @PyIndex int end) {
        assert rangeCheck(start, end, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        return AsciiByteArray.wrap(bytes, translateIndex(start) + this.start, this.start + translateIndex(end));
    }

    @Override
    @AllowPythonIndexing
    public @NotNull CharSequence subSequence(@PyIndex int start, @PyIndex int end) {
        return substring(start, end);
    }

    /* Comparison */

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

    @AllowPythonIndexing
    @Override protected @NotNull AsciiByteArray _wrap(byte @NotNull[] bytes, @PyIndex int start, @PyIndex int end) {
        return AsciiByteArray.wrap(bytes, start, end);
    }
}
