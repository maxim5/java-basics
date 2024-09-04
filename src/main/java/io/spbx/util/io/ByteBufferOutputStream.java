package io.spbx.util.io;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @link <a href="https://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream">StackOverflow</a>
 */
public class ByteBufferOutputStream extends OutputStream {
    private final ByteBuffer buf;

    public ByteBufferOutputStream(@NotNull ByteBuffer buf) {
        this.buf = buf;
    }

    public ByteBufferOutputStream(int bufferSize) {
        this(ByteBuffer.allocate(bufferSize));
    }

    public @NotNull ByteBuffer buf() {
        return buf;
    }

    @Override public void write(int b) {
        buf.put((byte) b);
    }

    @Override public void write(byte @NotNull[] bytes, int off, int len) {
        buf.put(bytes, off, len);
    }

    @Override public void close() {
    }
}
