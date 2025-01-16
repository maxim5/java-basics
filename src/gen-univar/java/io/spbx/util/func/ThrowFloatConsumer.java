package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents an operation that accepts a single {@code float} argument and returns no
 * result, potentially throwing a {@link Throwable}.
 * <p>
 * The {@code ThrowFloatConsumer} interface is similar to
 * {@link java.util.function.Consumer}, except that a {@code ThrowFloatConsumer}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Consumer
 */
@FunctionalInterface
@Generated(value = "Throw$Type$Consumer.java", date = "2025-01-14T10:07:33.526132200Z")
public interface ThrowFloatConsumer<E extends Throwable> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param left the first input argument
     * @param value the second input argument
     */
    void accept(float value) throws E;
}
