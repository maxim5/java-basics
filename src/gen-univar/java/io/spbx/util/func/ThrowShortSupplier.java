package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a supplier of {@code short}-valued results, potentially throwing a {@link Throwable}.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * The {@code ThrowShortSupplier} interface is similar to
 * {@link java.util.function.ShortSupplier}, except that a {@code ThrowShortSupplier}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Supplier
 * @see java.util.function.ShortSupplier
 */
@FunctionalInterface
@Generated(value = "Throw$Type$Supplier.java", date = "2024-12-02T15:53:08.813593500Z")
public interface ThrowShortSupplier<E extends Throwable> {
    /**
     * Gets a result, potentially throwing an exception.
     *
     * @return a result
     */
    short getAsShort() throws E;
}
