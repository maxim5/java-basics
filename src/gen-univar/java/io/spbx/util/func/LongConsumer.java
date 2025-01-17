package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code long} argument and returns no
 * result. Unlike most other functional interfaces, {@code LongConsumer} is expected
 * to operate via side-effects.
 */
@FunctionalInterface
@Generated(value = "$Type$Consumer.java", date = "2025-01-14T10:07:33.458118300Z")
public interface LongConsumer extends
    java.util.function.LongConsumer,
    Consumer<Long> {
    /**
     * Performs this operation on the {@code long} argument.
     */
    @Override
    void accept(long value);

    @Override
    default void accept(Long value) {
        this.accept((long) value);
    }
}
