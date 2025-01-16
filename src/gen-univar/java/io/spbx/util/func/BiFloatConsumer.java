package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code float} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiFloatConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiFloatObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2025-01-14T10:07:33.493125400Z")
public interface BiFloatConsumer extends
        BiConsumer<Float, Float>,
        BiFloatObjConsumer<Float> {
    /**
     * Performs this operation on the {@code float} arguments.
     */
    void accept(float left, float right);

    @Override
    default void accept(Float left, Float right) {
        this.accept((float) left, (float) right);
    }

    @Override
    default void accept(float left, Float right) {
        this.accept(left, (float) right);
    }
}
