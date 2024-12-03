package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two {@code byte}-valued arguments.
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code byte}.
 *
 * @see BiPredicate
 * @see BiByteObjPredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$Predicate.java", date = "2024-12-02T15:53:08.800591400Z")
public interface BiBytePredicate extends
        BiPredicate<Byte, Byte>,
        BiByteObjPredicate<Byte>,
        BiByteObjFunction<Byte, Boolean>,
        BiByteFunction<Boolean> {
    /**
     * Evaluates this predicate on the {@code byte} arguments.
     */
    boolean test(byte left, byte right);

    @Override
    default boolean test(Byte left, Byte right) {
        return test(left, right);
    }

    @Override
    default boolean test(byte left, Byte right) {
        return test(left, (byte) right);
    }

    @Override
    default Boolean apply(byte left, Byte right) {
        return test(left, (byte) right);
    }

    @Override
    default Boolean apply(byte left, byte right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Byte left, Byte right) {
        return test((byte) left, (byte) right);
    }
}
