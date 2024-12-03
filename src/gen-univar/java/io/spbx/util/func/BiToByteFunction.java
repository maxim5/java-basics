package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a binary function that produces a primitive {@code byte}-valued result.
 */
@FunctionalInterface
@Generated(value = "BiTo$Type$Function.java", date = "2024-12-02T15:53:08.808592300Z")
public interface BiToByteFunction<T, U> extends BiFunction<T, U, Byte> {
    /**
     * Applies this function to the given arguments.
     */
    byte applyToByte(T left, U right);

    @Override
    default Byte apply(T left, U right) {
        return applyToByte(left, right);
    }
}
