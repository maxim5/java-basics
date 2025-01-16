package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code char} argument and returns no
 * result. Unlike most other functional interfaces, {@code CharConsumer} is expected
 * to operate via side-effects.
 */
@FunctionalInterface
@Generated(value = "$Type$Consumer.java", date = "2025-01-14T10:07:33.458118300Z")
public interface CharConsumer extends
    Consumer<Character> {
    /**
     * Performs this operation on the {@code char} argument.
     */
    void accept(char value);

    @Override
    default void accept(Character value) {
        this.accept((char) value);
    }
}
