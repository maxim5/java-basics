package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code long}) and
 * returns no result. Unlike most other functional interfaces, {@code BiLongObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiLongConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2024-12-02T15:53:08.783586900Z")
public interface BiLongObjConsumer<T> extends BiConsumer<Long, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(long left, T right);

    @Override
    default void accept(Long left, T right) {
        this.accept((long) left, right);
    }
}
