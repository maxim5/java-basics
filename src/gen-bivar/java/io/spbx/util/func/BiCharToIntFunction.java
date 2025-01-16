package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code char}-valued operands and producing a
 * {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiCharToIntFunction extends
        BiFunction<Character, Character, Integer>,
        BiCharFunction<Integer>,
        BiToIntFunction<Character, Character>,
        BiCharObjFunction<Character, Integer> {
    /**
     * Applies this operator to the given operands.
     */
    int applyToInt(char left, char right);

    @Override
    default int applyToInt(Character left, Character right) {
        return applyToInt((char) left, (char) right);
    }

    @Override
    default Integer apply(char left, char right) {
        return applyToInt(left, right);
    }

    @Override
    default Integer apply(Character left, Character right) {
        return applyToInt((char) left, (char) right);
    }

    @Override
    default Integer apply(char left, Character right) {
        return applyToInt(left, (char) right);
    }
}
