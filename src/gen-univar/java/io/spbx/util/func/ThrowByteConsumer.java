package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents an operation that accepts a single {@code byte} argument and returns no
 * result, potentially throwing a {@link Throwable}.
 * <p>
 * The {@code ThrowByteConsumer} interface is similar to
 * {@link java.util.function.Consumer}, except that a {@code ThrowByteConsumer}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Consumer
 */
@FunctionalInterface
@Generated(value = "Throw$Type$Consumer.java", date = "2025-01-14T10:07:33.526132200Z")
public interface ThrowByteConsumer<E extends Throwable> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param left the first input argument
     * @param value the second input argument
     */
    void accept(byte value) throws E;
}
