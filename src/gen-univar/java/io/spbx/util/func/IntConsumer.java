package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code int} argument and returns no
 * result. Unlike most other functional interfaces, {@code IntConsumer} is expected
 * to operate via side-effects.
 */
@FunctionalInterface
@Generated(value = "$Type$Consumer.java", date = "2025-01-14T10:07:33.458118300Z")
public interface IntConsumer extends
    java.util.function.IntConsumer,
    Consumer<Integer> {
    /**
     * Performs this operation on the {@code int} argument.
     */
    @Override
    void accept(int value);

    @Override
    default void accept(Integer value) {
        this.accept((int) value);
    }
}
