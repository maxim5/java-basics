package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result.
 * This is the specialization of {@link BiFunction}, the arguments for which are {@code byte} and {@code int},
 * and the result is {@code byte}.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Function.java", date = "2025-01-14T10:38:22.030297800Z")
public interface BiByteIntFunction<R> extends
    BiFunction<Byte, Integer, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(byte left, int right);

    @Override
    default R apply(Byte left, Integer right) {
        return apply((byte) left, (int) right);
    }
}
