package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@code byte} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface LongToByteFunction extends
        Function<Long, Byte>,
        LongFunction<Byte>,
        ToByteFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    byte applyToByte(long value);

    @Override
    default Byte apply(long value) {
        return applyToByte(value);
    }

    @Override
    default Byte apply(Long value) {
        return this.applyToByte((long) value);
    }

    @Override
    default byte applyToByte(Long value) {
        return this.applyToByte((long) value);
    }
}
