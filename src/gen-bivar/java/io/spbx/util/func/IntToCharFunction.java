package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code int}-valued argument
 * and produces an {@code char} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.012293500Z")
public interface IntToCharFunction extends
        Function<Integer, Character>,
        IntFunction<Character>,
        ToCharFunction<Integer> {
    /**
     * Applies this function to the given argument.
     */
    char applyToChar(int value);

    @Override
    default Character apply(int value) {
        return applyToChar(value);
    }

    @Override
    default Character apply(Integer value) {
        return this.applyToChar((int) value);
    }

    @Override
    default char applyToChar(Integer value) {
        return this.applyToChar((int) value);
    }
}
