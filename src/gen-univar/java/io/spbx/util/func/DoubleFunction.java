package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code double}-valued argument
 * and produces an {@link Object} result of type {@code R}.
 */
@FunctionalInterface
@Generated(value = "$Type$Function.java", date = "2024-12-02T15:53:08.743578Z")
public interface DoubleFunction<R> extends Function<Double, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(double value);

    @Override
    default R apply(Double value) {
        return this.apply((double) value);
    }
}
