package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code short}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2024-12-02T15:53:08.779584900Z")
public interface BiShortFunction<R> extends
        BiFunction<Short, Short, R>,
        BiShortObjFunction<Short, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(short left, short right);

    @Override
    default R apply(Short left, Short right) {
        return this.apply((short) left, (short) right);
    }

    @Override
    default R apply(short left, Short right) {
        return this.apply(left, (short) right);
    }
}
