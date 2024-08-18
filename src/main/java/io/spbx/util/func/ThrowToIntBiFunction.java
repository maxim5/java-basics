package io.spbx.util.func;

/**
 * Represents a function that accepts two arguments and produces an int-valued
 * result and potentially throws a {@link Throwable}.
 * This is the {@code int}-producing primitive specialization for {@link ThrowBiFunction}.
 * <p>
 * The {@code ThrowToIntBiFunction} interface is similar to
 * {@link java.util.function.ToIntBiFunction}, except that a {@code ThrowToIntBiFunction}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <E> the type of Throwable thrown
 */
@FunctionalInterface
public interface ThrowToIntBiFunction<T, U, E extends Throwable> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    int applyAsInt(T t, U u) throws E;
}
