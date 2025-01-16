package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two {@code char} arguments and returns no
 * result. Unlike most other functional interfaces, {@code BiCharConsumer} is expected
 * to operate via side-effects.
 *
 * @see BiConsumer
 * @see BiCharObjConsumer
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Consumer.java", date = "2025-01-14T10:07:33.493125400Z")
public interface BiCharConsumer extends
        BiConsumer<Character, Character>,
        BiCharObjConsumer<Character> {
    /**
     * Performs this operation on the {@code char} arguments.
     */
    void accept(char left, char right);

    @Override
    default void accept(Character left, Character right) {
        this.accept((char) left, (char) right);
    }

    @Override
    default void accept(char left, Character right) {
        this.accept(left, (char) right);
    }
}
