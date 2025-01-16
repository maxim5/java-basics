package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code byte}-valued argument
 * and produces an {@link Object} result of type {@code R}.
 */
@FunctionalInterface
@Generated(value = "$Type$Function.java", date = "2025-01-14T10:07:33.465118500Z")
public interface ByteFunction<R> extends Function<Byte, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(byte value);

    @Override
    default R apply(Byte value) {
        return this.apply((byte) value);
    }
}
