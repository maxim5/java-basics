package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.UnaryOperator;

/**
 * Represents an operation on a single {@code char}-valued operand that produces
 * an {@code char}-valued result.
 */
@FunctionalInterface
@Generated(value = "$Type$UnaryOperator.java", date = "2024-12-02T15:53:08.770583300Z")
public interface CharUnaryOperator extends
        UnaryOperator<Character>, CharFunction<Character>, ToCharFunction<Character> {
    /**
     * Applies this operator to the given operands.
     */
    char applyToChar(char operand);

    @Override
    default Character apply(char value) {
        return this.applyToChar(value);
    }

    @Override
    default Character apply(Character value) {
        return this.applyToChar(value);
    }

    @Override
    default char applyToChar(Character value) {
        return this.applyToChar((char) value);
    }
}
