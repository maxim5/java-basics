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
@Generated(value = "Bi$Left$$Right$Function.java", date = "2025-01-14T10:38:22.030297800Z")
public interface BiCharIntFunction<R> extends
    BiFunction<Character, Integer, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(char left, int right);

    @Override
    default R apply(Character left, Integer right) {
        return apply((char) left, (int) right);
    }
}
