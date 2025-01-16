package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code long}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2025-01-14T10:07:33.484124100Z")
public interface LongSupplier extends
    java.util.function.LongSupplier,
    Supplier<Long> {
    /**
     * Returns the {@code long} result.
     */
    @Override
    long getAsLong();

    @Override
    default Long get() {
        return this.getAsLong();
    }
}
