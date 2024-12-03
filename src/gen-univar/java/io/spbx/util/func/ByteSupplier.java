package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code byte}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2024-12-02T15:53:08.764582400Z")
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
