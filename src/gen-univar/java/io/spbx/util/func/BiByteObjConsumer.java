package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code byte}) and
 * returns no result. Unlike most other functional interfaces, {@code BiByteObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiByteConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2025-01-14T10:07:33.500127300Z")
public interface BiByteObjConsumer<T> extends BiConsumer<Byte, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(byte left, T right);

    @Override
    default void accept(Byte left, T right) {
        this.accept((byte) left, right);
    }
}
