package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a supplier of {@code int}-valued results, potentially throwing a {@link Throwable}.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * The {@code ThrowIntSupplier} interface is similar to
 * {@link java.util.function.IntSupplier}, except that a {@code ThrowIntSupplier}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Supplier
 * @see java.util.function.IntSupplier
 */
@FunctionalInterface
@Generated(value = "Throw$Type$Supplier.java", date = "2024-12-02T15:53:08.813593500Z")
public interface ThrowIntSupplier<E extends Throwable> {
    /**
     * Gets a result, potentially throwing an exception.
     *
     * @return a result
     */
    int getAsInt() throws E;
}
