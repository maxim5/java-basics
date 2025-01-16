package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a supplier of {@code long}-valued results, potentially throwing a {@link Throwable}.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * The {@code ThrowLongSupplier} interface is similar to
 * {@link java.util.function.LongSupplier}, except that a {@code ThrowLongSupplier}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Supplier
 * @see java.util.function.LongSupplier
 */
@FunctionalInterface
@Generated(value = "Throw$Type$Supplier.java", date = "2025-01-14T10:07:33.529133100Z")
public interface ThrowLongSupplier<E extends Throwable> {
    /**
     * Gets a result, potentially throwing an exception.
     *
     * @return a result
     */
    long getAsLong() throws E;
}
