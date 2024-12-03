package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code byte} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiByteConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiByteObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2024-12-02T15:53:08.775585Z")
public interface BiByteConsumer extends BiConsumer<Byte, Byte>, BiByteObjConsumer<Byte> {
    /**
     * Performs this operation on the {@code byte} arguments.
     */
    void accept(byte left, byte right);

    @Override
    default void accept(Byte left, Byte right) {
        this.accept((byte) left, (byte) right);
    }

    @Override
    default void accept(byte left, Byte right) {
        this.accept(left, (byte) right);
    }
}
