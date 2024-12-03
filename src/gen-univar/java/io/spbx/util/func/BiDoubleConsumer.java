package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code double} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiDoubleConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiDoubleObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2024-12-02T15:53:08.775585Z")
public interface BiDoubleConsumer extends BiConsumer<Double, Double>, BiDoubleObjConsumer<Double> {
    /**
     * Performs this operation on the {@code double} arguments.
     */
    void accept(double left, double right);

    @Override
    default void accept(Double left, Double right) {
        this.accept((double) left, (double) right);
    }

    @Override
    default void accept(double left, Double right) {
        this.accept(left, (double) right);
    }
}
