package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents an operation that accepts two input arguments one of which is an {@code float}
 * and returns no result, potentially throwing a {@link Throwable}.
 * <p>
 * The {@code ThrowObjFloatConsumer} interface is similar to
 * {@link java.util.function.BiConsumer}, except that a {@code ThrowObjFloatConsumer}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <U> the type of one of the inputs to the function
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Consumer
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
@Generated(value = "ThrowObj$Type$Consumer.java", date = "2025-01-14T10:07:33.533133800Z")
public interface ThrowObjFloatConsumer<U, E extends Throwable> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param left the first input argument
     * @param right the second input argument
     */
    void accept(U left, float right) throws E;
}
