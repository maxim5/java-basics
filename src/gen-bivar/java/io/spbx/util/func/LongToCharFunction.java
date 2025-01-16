package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@code char} result.
 */
@FunctionalInterface
@Generated(value = "$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.012293500Z")
public interface LongToCharFunction extends
        Function<Long, Character>,
        LongFunction<Character>,
        ToCharFunction<Long> {
    /**
     * Applies this function to the given argument.
     */
    char applyToChar(long value);

    @Override
    default Character apply(long value) {
        return applyToChar(value);
    }

    @Override
    default Character apply(Long value) {
        return this.applyToChar((long) value);
    }

    @Override
    default char applyToChar(Long value) {
        return this.applyToChar((long) value);
    }
}
