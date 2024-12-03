package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code int}-valued argument
 * and produces an {@code byte} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2024-11-26T15:08:01.460868500Z")
public interface IntToByteFunction extends
        Function<Integer, Byte>,
        IntFunction<Byte>,
        ToByteFunction<Integer> {
    /**
     * Applies this function to the given argument.
     */
    byte applyToByte(int value);

    @Override
    default Byte apply(int value) {
        return applyToByte(value);
    }

    @Override
    default Byte apply(Integer value) {
        return this.applyToByte((int) value);
    }

    @Override
    default byte applyToByte(Integer value) {
        return this.applyToByte((int) value);
    }
}
