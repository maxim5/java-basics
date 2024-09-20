package io.spbx.util.func;

/**
 * Represents a function that accepts two arguments and produces an int-valued
 * result and potentially throws a {@link Throwable}.
 * This is the {@code long}-producing primitive specialization for {@link ThrowBiFunction}.
 * <p>
 * The {@code ThrowToLongBiFunction} interface is similar to
 * {@link java.util.function.ToLongBiFunction}, except that a {@code ThrowToLongBiFunction}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <E> the type of Throwable thrown
 */
@FunctionalInterface
public interface ThrowToLongBiFunction<T, U, E extends Throwable> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    long applyAsLong(T left, U right) throws E;
}
