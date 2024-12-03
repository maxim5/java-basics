package io.spbx.util.extern.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.str.BasicParsing;
import io.spbx.util.classpath.RuntimeRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static io.spbx.util.base.str.BasicParsing.DECIMAL;

@Stateless
@Pure
@CheckReturnValue
public class NettyByteBufs {
    static {
        RuntimeRequirement.verify("io.netty.buffer.ByteBuf");
    }

    public static @NotNull ByteBuf allocate(@NotNull ByteBufAllocator allocator, byte @NotNull[] bytes) {
        ByteBuf buffer = allocator.buffer(bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }

    public static @NotNull ByteBuf allocate(@NotNull ByteBufAllocator allocator, byte @NotNull[] bytes, int offset, int length) {
        ByteBuf buffer = allocator.buffer(length);
        buffer.writeBytes(bytes, offset, length);
        return buffer;
    }

    public static byte @NotNull[] copyToByteArray(@NotNull ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.duplicate().readBytes(bytes);
        return bytes;
    }

    public static @Nullable ByteBuf readUntil(@NotNull ByteBuf content, byte value, int minLength, int maxLength) {
        int start = content.readerIndex();
        int index = content.indexOf(start, start + Math.min(maxLength, content.readableBytes()), value);
        if (index < minLength) {
            return null;
        }
        ByteBuf result = content.readBytes(index - start);
        content.readBytes(1);  // Equal to `value`
        return result;
    }

    public static int parseInt(@NotNull ByteBuf content) {
        return parseInt(content, DECIMAL);
    }

    public static int parseInt(@NotNull ByteBuf content, int radix) {
        if (content.hasArray()) {
            return BasicParsing.parseInt(content.array(), content.readerIndex(), content.readableBytes(), radix);
        } else {
            return Integer.parseInt(content.toString(StandardCharsets.US_ASCII));
        }
    }

    public static int parseIntSafe(@NotNull ByteBuf content, int defaultValue) {
        return parseIntSafe(content, DECIMAL, defaultValue);
    }

    public static int parseIntSafe(@NotNull ByteBuf content, int radix, int defaultValue) {
        return BasicParsing.parseIntSafe(() -> parseInt(content, radix), defaultValue);
    }

    public static long parseLong(@NotNull ByteBuf content) {
        return parseLong(content, DECIMAL);
    }

    public static long parseLong(@NotNull ByteBuf content, int radix) {
        if (content.hasArray()) {
            return BasicParsing.parseLong(content.array(), content.readerIndex(), content.readableBytes(), radix);
        } else {
            return Long.parseLong(content.toString(StandardCharsets.US_ASCII));
        }
    }

    public static long parseLongSafe(@NotNull ByteBuf content, long defaultValue) {
        return parseLongSafe(content, DECIMAL, defaultValue);
    }

    public static long parseLongSafe(@NotNull ByteBuf content, int radix, long defaultValue) {
        try {
            return parseLong(content, radix);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static void writeIntString(int value, @NotNull ByteBuf dest) {
        dest.writeCharSequence(String.valueOf(value), StandardCharsets.US_ASCII);
    }

    public static void writeLongString(long value, @NotNull ByteBuf dest) {
        dest.writeCharSequence(String.valueOf(value), StandardCharsets.US_ASCII);
    }

    public static @Nullable ByteBuf wrapNullable(@Nullable ByteBuffer buffer) {
        return buffer != null ? Unpooled.wrappedBuffer(buffer) : null;
    }

    public static @Nullable ByteBuf wrapNullable(byte @Nullable[] bytes) {
        return bytes != null ? Unpooled.wrappedBuffer(bytes) : null;
    }
}
