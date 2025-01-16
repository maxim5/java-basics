package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments: {@code byte} and {@code int}.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate}.
 *
 * @see BiPredicate
 * @see BiBytePredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Left$$Right$Predicate.java", date = "2025-01-14T10:38:22.038299500Z")
public interface BiByteIntPredicate extends
        BiPredicate<Byte, Integer>,
        BiByteObjPredicate<Integer>,
        BiByteIntFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code byte} and {@code int} arguments.
     */
    boolean test(byte left, int right);

    @Override
    default boolean test(Byte left, Integer right) {
        return test((byte) left, (int) right);
    }

    @Override
    default boolean test(byte left, Integer right) {
        return test(left, (int) right);
    }

    @Override
    default Boolean apply(byte left, int right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Byte left, Integer right) {
        return test((byte) left, (int) right);
    }
}
