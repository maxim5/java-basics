package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Represents an operation that accepts {@code byte} and {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code Bi$Type$Consumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiByteObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Consumer.java", date = "2025-01-14T10:38:22.023296500Z")
public interface BiByteIntConsumer extends
        BiConsumer<Byte, Integer>,
        BiByteObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code byte} and {@code int} arguments.
     */
    void accept(byte left, int right);

    @Override
    default void accept(byte left, Integer right) {
        accept(left, (int) right);
    }

    @Override
    default void accept(Byte left, Integer right) {
        accept((byte) left, (int) right);
    }
}
