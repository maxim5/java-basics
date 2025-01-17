package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code short}) and
 * returns no result. Unlike most other functional interfaces, {@code BiShortObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiShortConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2025-01-14T10:07:33.500127300Z")
public interface BiShortObjConsumer<T> extends BiConsumer<Short, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(short left, T right);

    @Override
    default void accept(Short left, T right) {
        this.accept((short) left, right);
    }
}
