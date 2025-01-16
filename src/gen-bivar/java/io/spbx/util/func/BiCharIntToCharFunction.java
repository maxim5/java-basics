package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code char} and {@code int},
 * and the result is {@code char}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$To$Left$Function.java", date = "2025-01-14T10:38:22.045301100Z")
public interface BiCharIntToCharFunction extends
        BiFunction<Character, Integer, Character>,
        BiToCharFunction<Character, Integer>,
        BiCharIntFunction<Character> {
    /**
     * Applies this function to the given argument.
     */
    char applyToChar(char left, int right);

    @Override
    default char applyToChar(Character left, Integer right) {
        return applyToChar((char) left, (int) right);
    }

    @Override
    default Character apply(Character left, Integer right) {
        return applyToChar((char) left, (int) right);
    }

    @Override
    default Character apply(char left, int right) {
        return applyToChar(left, right);
    }
}
