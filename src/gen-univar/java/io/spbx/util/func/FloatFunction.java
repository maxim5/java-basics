package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code float}-valued argument
 * and produces an {@link Object} result of type {@code R}.
 */
@FunctionalInterface
@Generated(value = "$Type$Function.java", date = "2025-01-14T10:07:33.465118500Z")
public interface FloatFunction<R> extends Function<Float, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(float value);

    @Override
    default R apply(Float value) {
        return this.apply((float) value);
    }
}
