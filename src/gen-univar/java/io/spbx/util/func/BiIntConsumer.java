package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code int} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiIntConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiIntObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2024-12-02T15:53:08.775585Z")
public interface BiIntConsumer extends BiConsumer<Integer, Integer>, BiIntObjConsumer<Integer> {
    /**
     * Performs this operation on the {@code int} arguments.
     */
    void accept(int left, int right);

    @Override
    default void accept(Integer left, Integer right) {
        this.accept((int) left, (int) right);
    }

    @Override
    default void accept(int left, Integer right) {
        this.accept(left, (int) right);
    }
}
