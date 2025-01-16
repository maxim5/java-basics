package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code int}-valued argument
 * and produces an {@code short} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.012293500Z")
public interface IntToShortFunction extends
        Function<Integer, Short>,
        IntFunction<Short>,
        ToShortFunction<Integer> {
    /**
     * Applies this function to the given argument.
     */
    short applyToShort(int value);

    @Override
    default Short apply(int value) {
        return applyToShort(value);
    }

    @Override
    default Short apply(Integer value) {
        return this.applyToShort((int) value);
    }

    @Override
    default short applyToShort(Integer value) {
        return this.applyToShort((int) value);
    }
}
