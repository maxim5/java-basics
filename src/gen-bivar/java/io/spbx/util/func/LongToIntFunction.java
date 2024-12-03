package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@code int} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface LongToIntFunction extends
        Function<Long, Integer>,
        LongFunction<Integer>,
        ToIntFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    int applyToInt(long value);

    @Override
    default Integer apply(long value) {
        return applyToInt(value);
    }

    @Override
    default Integer apply(Long value) {
        return this.applyToInt((long) value);
    }

    @Override
    default int applyToInt(Long value) {
        return this.applyToInt((long) value);
    }
}
