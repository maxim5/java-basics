package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code short}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2024-12-02T15:53:08.764582400Z")
public interface ShortSupplier extends
    Supplier<Short> {
    /**
     * Returns the {@code short} result.
     */
    short getAsShort();

    @Override
    default Short get() {
        return this.getAsShort();
    }
}
