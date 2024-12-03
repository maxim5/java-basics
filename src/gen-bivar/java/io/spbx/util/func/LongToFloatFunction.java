package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@code float} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface LongToFloatFunction extends
        Function<Long, Float>,
        LongFunction<Float>,
        ToFloatFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    float applyToFloat(long value);

    @Override
    default Float apply(long value) {
        return applyToFloat(value);
    }

    @Override
    default Float apply(Long value) {
        return this.applyToFloat((long) value);
    }

    @Override
    default float applyToFloat(Long value) {
        return this.applyToFloat((long) value);
    }
}
