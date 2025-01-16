package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two input arguments (one of which is {@code char}) and
 * returns no result. Unlike most other functional interfaces, {@code BiCharObjConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiCharConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjConsumer.java", date = "2025-01-14T10:07:33.500127300Z")
public interface BiCharObjConsumer<T> extends BiConsumer<Character, T> {
    /**
     * Performs this operation on the arguments.
     */
    void accept(char left, T right);

    @Override
    default void accept(Character left, T right) {
        this.accept((char) left, right);
    }
}
