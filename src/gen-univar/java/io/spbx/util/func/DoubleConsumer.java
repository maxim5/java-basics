package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code double} argument and returns no
 * result. Unlike most other functional interfaces, {@code DoubleConsumer} is expected
 * to operate via side-effects.
 */
@FunctionalInterface
@Generated(value = "$Type$Consumer.java", date = "2025-01-14T10:07:33.458118300Z")
public interface DoubleConsumer extends
    java.util.function.DoubleConsumer,
    Consumer<Double> {
    /**
     * Performs this operation on the {@code double} argument.
     */
    @Override
    void accept(double value);

    @Override
    default void accept(Double value) {
        this.accept((double) value);
    }
}
