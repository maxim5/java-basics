package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code float}) and
 * returns no result. Unlike most other functional interfaces, {@code BiFloatObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiFloatConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2025-01-14T10:07:33.500127300Z")
public interface BiFloatObjConsumer<T> extends BiConsumer<Float, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(float left, T right);

    @Override
    default void accept(Float left, T right) {
        this.accept((float) left, right);
    }
}
