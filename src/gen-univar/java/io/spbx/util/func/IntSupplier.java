package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code int}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2024-12-02T15:53:08.764582400Z")
public interface IntSupplier extends
    java.util.function.IntSupplier,
    Supplier<Integer> {
    /**
     * Returns the {@code int} result.
     */
    @Override
    int getAsInt();

    @Override
    default Integer get() {
        return this.getAsInt();
    }
}
