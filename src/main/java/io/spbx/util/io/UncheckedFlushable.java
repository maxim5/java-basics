package io.spbx.util.io;

import java.io.Flushable;
import java.io.UncheckedIOException;

/**
 * A type of {@link Flushable} which reports I/O errors via {@link UncheckedIOException}.
 *
 * @see Flushable
 */
public interface UncheckedFlushable extends Flushable {
    /**
     * Flushes this stream by writing any buffered output to the underlying
     * stream.
     *
     * @throws UncheckedIOException If an I/O error occurs
     */
    @Override
    void flush() throws UncheckedIOException;
}
