package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code byte} argument and returns no
 * result. Unlike most other functional interfaces, {@code ByteConsumer} is expected
 * to operate via side-effects.
 */
@FunctionalInterface
@Generated(value = "$Type$Consumer.java", date = "2025-01-14T10:07:33.458118300Z")
public interface ByteConsumer extends
    Consumer<Byte> {
    /**
     * Performs this operation on the {@code byte} argument.
     */
    void accept(byte value);

    @Override
    default void accept(Byte value) {
        this.accept((byte) value);
    }
}
