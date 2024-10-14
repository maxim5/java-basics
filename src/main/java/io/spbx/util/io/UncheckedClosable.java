package io.spbx.util.io;

import java.io.Closeable;
import java.io.UncheckedIOException;

/**
 * A type of {@link Closeable} which reports I/O errors via {@link UncheckedIOException}.
 *
 * @see Closeable
 */
public interface UncheckedClosable extends Closeable {
    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     * <p>
     * As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code UncheckedIOException}.
     *
     * @throws UncheckedIOException if an I/O error occurs
     */
    @Override
    void close() throws UncheckedIOException;
}
