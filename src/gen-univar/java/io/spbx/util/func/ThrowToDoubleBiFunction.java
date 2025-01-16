package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces an int-valued
 * result and potentially throws a {@link Throwable}.
 * This is the {@code double}-producing primitive specialization for {@link ThrowBiFunction}.
 * <p>
 * The {@code ThrowToDoubleBiFunction} interface is similar to
 * {@link java.util.function.ToDoubleBiFunction}, except that a {@code ThrowToDoubleBiFunction}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <E> the type of Throwable thrown
 */
@FunctionalInterface
@Generated(value = "ThrowTo$Type$BiFunction.java", date = "2025-01-14T10:07:33.536135300Z")
public interface ThrowToDoubleBiFunction<T, U, E extends Throwable> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    double applyAsDouble(T left, U right) throws E;
}
