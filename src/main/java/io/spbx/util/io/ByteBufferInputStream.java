package io.spbx.util.io;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @link <a href="https://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream">StackOverflow</a>
 */
public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buf;

    public ByteBufferInputStream(@NotNull ByteBuffer buf) {
        this.buf = buf;
    }

    public ByteBufferInputStream(int bufferSize) {
        this(ByteBuffer.allocate(bufferSize));
        buf.flip();
    }

    public @NotNull ByteBuffer buf() {
        return buf;
    }

    @Override public int available() {
        return buf.remaining();
    }

    @Override public int read() {
        return buf.hasRemaining() ? buf.get() & 0xFF : -1;
    }

    @Override public int read(byte @NotNull[] bytes, int offset, int length) {
        if (!buf.hasRemaining()) return -1;
        int len = Math.min(length, buf.remaining());
        buf.get(bytes, offset, len);
        return len;
    }

    @Override public void close() {
    }
}
