package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a supplier of {@code char}-valued results, potentially throwing a {@link Throwable}.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * The {@code ThrowCharSupplier} interface is similar to
 * {@link java.util.function.CharSupplier}, except that a {@code ThrowCharSupplier}
 * can throw any kind of exception, including checked exceptions.
 *
 * @param <E> the type of Throwable thrown
 * @see java.util.function.Supplier
 * @see java.util.function.CharSupplier
 */
@FunctionalInterface
@Generated(value = "Throw$Type$Supplier.java", date = "2024-12-02T15:53:08.813593500Z")
public interface ThrowCharSupplier<E extends Throwable> {
    /**
     * Gets a result, potentially throwing an exception.
     *
     * @return a result
     */
    char getAsChar() throws E;
}
