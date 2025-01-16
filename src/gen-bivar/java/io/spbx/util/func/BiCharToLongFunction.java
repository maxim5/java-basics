package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code char}-valued operands and producing a
 * {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiCharToLongFunction extends
        BiFunction<Character, Character, Long>,
        BiCharFunction<Long>,
        BiToLongFunction<Character, Character>,
        BiCharObjFunction<Character, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(char left, char right);

    @Override
    default long applyToLong(Character left, Character right) {
        return applyToLong((char) left, (char) right);
    }

    @Override
    default Long apply(char left, char right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(Character left, Character right) {
        return applyToLong((char) left, (char) right);
    }

    @Override
    default Long apply(char left, Character right) {
        return applyToLong(left, (char) right);
    }
}
