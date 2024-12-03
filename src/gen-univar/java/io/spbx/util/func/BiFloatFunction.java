package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents a function that accepts two primitive {@code float}-valued arguments
 * and produces an {@link Object} result.
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Function.java", date = "2024-12-02T15:53:08.779584900Z")
public interface BiFloatFunction<R> extends
        BiFunction<Float, Float, R>,
        BiFloatObjFunction<Float, R> {
    /**
     * Applies this function to the given arguments.
     */
    R apply(float left, float right);

    @Override
    default R apply(Float left, Float right) {
        return this.apply((float) left, (float) right);
    }

    @Override
    default R apply(float left, Float right) {
        return this.apply(left, (float) right);
    }
}
