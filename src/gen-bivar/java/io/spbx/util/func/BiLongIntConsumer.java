package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Represents an operation that accepts {@code long} and {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code Bi$Type$Consumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiLongObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Consumer.java", date = "2025-01-14T10:38:22.023296500Z")
public interface BiLongIntConsumer extends
        BiConsumer<Long, Integer>,
        BiLongObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code long} and {@code int} arguments.
     */
    void accept(long left, int right);

    @Override
    default void accept(long left, Integer right) {
        accept(left, (int) right);
    }

    @Override
    default void accept(Long left, Integer right) {
        accept((long) left, (int) right);
    }
}
