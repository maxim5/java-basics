package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code byte}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2024-12-02T15:53:08.779584900Z")
public interface BiByteFunction<R> extends
        BiFunction<Byte, Byte, R>,
        BiByteObjFunction<Byte, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(byte left, byte right);

    @Override
    default R apply(Byte left, Byte right) {
        return this.apply((byte) left, (byte) right);
    }

    @Override
    default R apply(byte left, Byte right) {
        return this.apply(left, (byte) right);
    }
}
