package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@code double} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.012293500Z")
public interface LongToDoubleFunction extends
        Function<Long, Double>,
        LongFunction<Double>,
        ToDoubleFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    double applyToDouble(long value);

    @Override
    default Double apply(long value) {
        return applyToDouble(value);
    }

    @Override
    default Double apply(Long value) {
        return this.applyToDouble((long) value);
    }

    @Override
    default double applyToDouble(Long value) {
        return this.applyToDouble((long) value);
    }
}
