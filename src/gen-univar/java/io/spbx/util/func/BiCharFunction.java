package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code char}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2025-01-14T10:07:33.497126600Z")
public interface BiCharFunction<R> extends
        BiFunction<Character, Character, R>,
        BiCharObjFunction<Character, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(char left, char right);

    @Override
    default R apply(Character left, Character right) {
        return this.apply((char) left, (char) right);
    }

    @Override
    default R apply(char left, Character right) {
        return this.apply(left, (char) right);
    }
}
