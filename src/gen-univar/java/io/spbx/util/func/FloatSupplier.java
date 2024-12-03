package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code float}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2024-12-02T15:53:08.764582400Z")
public interface FloatSupplier extends
    Supplier<Float> {
    /**
     * Returns the {@code float} result.
     */
    float getAsFloat();

    @Override
    default Float get() {
        return this.getAsFloat();
    }
}
