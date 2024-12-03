package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code char} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code char}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjCharFunction
 * @see CharFunction
 * @see BiToCharFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2024-12-02T15:53:08.796590100Z")
public interface BiCharObjToCharFunction<T> extends BiCharObjFunction<T, Character>, BiToCharFunction<Character, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    char applyToChar(char left, T right);

    @Override
    default Character apply(char left, T right) {
        return applyToChar(left, right);
    }

    @Override
    default char applyToChar(Character left, T right) {
        return applyToChar((char) left, right);
    }

    @Override
    default Character apply(Character left, T right) {
        return applyToChar((char) left, right);
    }
}
