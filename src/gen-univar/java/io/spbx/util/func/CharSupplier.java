package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@code char}-valued results.
 */
@FunctionalInterface
@Generated(value = "$Type$Supplier.java", date = "2024-12-02T15:53:08.764582400Z")
public interface CharSupplier extends
    Supplier<Character> {
    /**
     * Returns the {@code char} result.
     */
    char getAsChar();

    @Override
    default Character get() {
        return this.getAsChar();
    }
}
