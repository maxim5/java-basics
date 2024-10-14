package io.spbx.util.func;

import java.util.function.UnaryOperator;

/**
 * Represents a reversible operation on a single operand that produces a result of the
 * same type as its operand.
 * <p>
 * An example of that might be an escape function, encoding, bitwise flip, etc.
 *
 * @param <T> the type of the operand and result of the operator
 * @see Reversible
 */
public interface ReversibleOperator<T> extends Reversible<T, T>, UnaryOperator<T> {
}
