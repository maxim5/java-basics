package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@code short} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface LongToShortFunction extends
        Function<Long, Short>,
        LongFunction<Short>,
        ToShortFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    short applyToShort(long value);

    @Override
    default Short apply(long value) {
        return applyToShort(value);
    }

    @Override
    default Short apply(Long value) {
        return this.applyToShort((long) value);
    }

    @Override
    default short applyToShort(Long value) {
        return this.applyToShort((long) value);
    }
}
