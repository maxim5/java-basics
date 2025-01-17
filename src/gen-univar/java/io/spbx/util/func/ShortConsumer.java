package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code short} argument and returns no
 * result. Unlike most other functional interfaces, {@code ShortConsumer} is expected
 * to operate via side-effects.
 */
@FunctionalInterface
@Generated(value = "$Type$Consumer.java", date = "2025-01-14T10:07:33.458118300Z")
public interface ShortConsumer extends
    Consumer<Short> {
    /**
     * Performs this operation on the {@code short} argument.
     */
    void accept(short value);

    @Override
    default void accept(Short value) {
        this.accept((short) value);
    }
}
