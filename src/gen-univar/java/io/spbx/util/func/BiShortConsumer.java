package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code short} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiShortConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiShortObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2025-01-14T10:07:33.493125400Z")
public interface BiShortConsumer extends
        BiConsumer<Short, Short>,
        BiShortObjConsumer<Short> {
    /**
     * Performs this operation on the {@code short} arguments.
     */
    void accept(short left, short right);

    @Override
    default void accept(Short left, Short right) {
        this.accept((short) left, (short) right);
    }

    @Override
    default void accept(short left, Short right) {
        this.accept(left, (short) right);
    }
}
