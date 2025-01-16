package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Represents an operation that accepts {@code double} and {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code Bi$Type$Consumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiDoubleObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Consumer.java", date = "2025-01-14T10:38:22.023296500Z")
public interface BiDoubleIntConsumer extends
        BiConsumer<Double, Integer>,
        BiDoubleObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code double} and {@code int} arguments.
     */
    void accept(double left, int right);

    @Override
    default void accept(double left, Integer right) {
        accept(left, (int) right);
    }

    @Override
    default void accept(Double left, Integer right) {
        accept((double) left, (int) right);
    }
}
