package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code int}-valued argument
 * and produces an {@code double} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface IntToDoubleFunction extends
        Function<Integer, Double>,
        IntFunction<Double>,
        ToDoubleFunction<Integer> {
    /**
     * Applies this function to the given argument.
     */
    double applyToDouble(int value);

    @Override
    default Double apply(int value) {
        return applyToDouble(value);
    }

    @Override
    default Double apply(Integer value) {
        return this.applyToDouble((int) value);
    }

    @Override
    default double applyToDouble(Integer value) {
        return this.applyToDouble((int) value);
    }
}
