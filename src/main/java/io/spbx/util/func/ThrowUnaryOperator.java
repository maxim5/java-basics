package io.spbx.util.func;

/**
 * Represents an operation on a single operand that produces a result of the
 * same type as its operand and potentially throws a {@link Throwable}.
 * This is a specialization of {@link ThrowFunction} for
 * the case where the operand and result are of the same type.
 *
 * @param <T> the type of the operand and result of the operator
 * @param <E> the type of Throwable thrown
 * @see ThrowFunction
 */
@FunctionalInterface
public interface ThrowUnaryOperator<T, E extends Throwable> extends ThrowFunction<T, T, E> {
}
