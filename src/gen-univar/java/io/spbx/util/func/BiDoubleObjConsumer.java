package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code double}) and
 * returns no result. Unlike most other functional interfaces, {@code BiDoubleObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiDoubleConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2025-01-14T10:07:33.500127300Z")
public interface BiDoubleObjConsumer<T> extends BiConsumer<Double, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(double left, T right);

    @Override
    default void accept(Double left, T right) {
        this.accept((double) left, right);
    }
}
