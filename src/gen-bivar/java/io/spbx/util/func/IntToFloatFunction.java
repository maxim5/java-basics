package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code int}-valued argument
 * and produces an {@code float} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.012293500Z")
public interface IntToFloatFunction extends
        Function<Integer, Float>,
        IntFunction<Float>,
        ToFloatFunction<Integer> {
    /**
     * Applies this function to the given argument.
     */
    float applyToFloat(int value);

    @Override
    default Float apply(int value) {
        return applyToFloat(value);
    }

    @Override
    default Float apply(Integer value) {
        return this.applyToFloat((int) value);
    }

    @Override
    default float applyToFloat(Integer value) {
        return this.applyToFloat((int) value);
    }
}
