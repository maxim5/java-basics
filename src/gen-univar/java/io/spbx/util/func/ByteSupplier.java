package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code byte}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2025-01-14T10:07:33.484124100Z")
public interface ByteSupplier extends
    Supplier<Byte> {
    /**
     * Returns the {@code byte} result.
     */
    byte getAsByte();

    @Override
    default Byte get() {
        return this.getAsByte();
    }
}
