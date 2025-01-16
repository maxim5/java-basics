package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code int}) and
 * returns no result. Unlike most other functional interfaces, {@code BiIntObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiIntConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2025-01-14T10:07:33.500127300Z")
public interface BiIntObjConsumer<T> extends BiConsumer<Integer, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(int left, T right);

    @Override
    default void accept(Integer left, T right) {
        this.accept((int) left, right);
    }
}
