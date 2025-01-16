package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Represents an operation that accepts {@code char} and {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code Bi$Type$Consumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiCharObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Consumer.java", date = "2025-01-14T10:38:22.023296500Z")
public interface BiCharIntConsumer extends
        BiConsumer<Character, Integer>,
        BiCharObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code char} and {@code int} arguments.
     */
    void accept(char left, int right);

    @Override
    default void accept(char left, Integer right) {
        accept(left, (int) right);
    }

    @Override
    default void accept(Character left, Integer right) {
        accept((char) left, (int) right);
    }
}
