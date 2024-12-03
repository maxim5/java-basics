package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Function;

/**
 * Represents a function that accepts a primitive {@code long}-valued argument
 * and produces an {@link Object} result of type {@code R}.
 */
@FunctionalInterface
@Generated(value = "$Type$Function.java", date = "2024-12-02T15:53:08.743578Z")
public interface LongFunction<R> extends Function<Long, R> {
    /**
     * Applies this function to the given argument.
     */
    R apply(long value);

    @Override
    default R apply(Long value) {
        return this.apply((long) value);
    }
}
