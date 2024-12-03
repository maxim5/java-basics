package io.spbx.util.func;

/**
 * Represents a supplier of {@code boolean}-valued results, potentially throwing a {@link Throwable}.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * The {@code ThrowBoolSupplier} interface is similar to
 * {@link java.util.function.BooleanSupplier}, except that a {@code ThrowSupplier}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.BooleanSupplier
 */
@FunctionalInterface
public interface ThrowBoolSupplier<E extends Throwable> {
    /**
     * Gets a result, potentially throwing an exception.
     *
     * @return a result
     */
    boolean getAsBoolean() throws E;
}
