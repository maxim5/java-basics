package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Represents an operation that accepts {@code float} and {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code Bi$Type$Consumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiFloatObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Consumer.java", date = "2025-01-14T10:38:22.023296500Z")
public interface BiFloatIntConsumer extends
        BiConsumer<Float, Integer>,
        BiFloatObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code float} and {@code int} arguments.
     */
    void accept(float left, int right);

    @Override
    default void accept(float left, Integer right) {
        accept(left, (int) right);
    }

    @Override
    default void accept(Float left, Integer right) {
        accept((float) left, (int) right);
    }
}
