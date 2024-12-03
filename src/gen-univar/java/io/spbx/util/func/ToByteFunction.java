package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that produces a primitive {@code byte}-valued result.
 */
@FunctionalInterface
@Generated(value = "To$Type$Function.java", date = "2024-12-02T15:53:08.825596700Z")
public interface ToByteFunction<T> extends Function<T, Byte> {
    /**
     * Applies this function to the given argument.
     */
    byte applyToByte(T value);

    @Override
    default Byte apply(T value) {
        return applyToByte(value);
    }
}
