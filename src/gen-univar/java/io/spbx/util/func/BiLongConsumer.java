package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code long} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiLongConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiLongObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2024-12-02T15:53:08.775585Z")
public interface BiLongConsumer extends BiConsumer<Long, Long>, BiLongObjConsumer<Long> {
    /**
     * Performs this operation on the {@code long} arguments.
     */
    void accept(long left, long right);

    @Override
    default void accept(Long left, Long right) {
        this.accept((long) left, (long) right);
    }

    @Override
    default void accept(long left, Long right) {
        this.accept(left, (long) right);
    }
}
