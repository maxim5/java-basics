package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Represents an operation that accepts {@code short} and {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code Bi$Type$Consumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiShortObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Consumer.java", date = "2025-01-14T10:38:22.023296500Z")
public interface BiShortIntConsumer extends
        BiConsumer<Short, Integer>,
        BiShortObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code short} and {@code int} arguments.
     */
    void accept(short left, int right);

    @Override
    default void accept(short left, Integer right) {
        accept(left, (int) right);
    }

    @Override
    default void accept(Short left, Integer right) {
        accept((short) left, (int) right);
    }
}
