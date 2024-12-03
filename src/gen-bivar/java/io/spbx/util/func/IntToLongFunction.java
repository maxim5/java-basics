package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code int}-valued argument
 * and produces an {@code long} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface IntToLongFunction extends
        Function<Integer, Long>,
        IntFunction<Long>,
        ToLongFunction<Integer> {
    /**
     * Applies this function to the given argument.
     */
    long applyToLong(int value);

    @Override
    default Long apply(int value) {
        return applyToLong(value);
    }

    @Override
    default Long apply(Integer value) {
        return this.applyToLong((int) value);
    }

    @Override
    default long applyToLong(Integer value) {
        return this.applyToLong((int) value);
    }
}
