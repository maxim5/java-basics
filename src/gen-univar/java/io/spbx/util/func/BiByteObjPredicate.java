package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiPredicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments (one of which is {@code byte}).
 * This is the two-arity specialization of {@link Predicate}.
 * This is the primitive type specialization of {@link BiPredicate} for {@code byte} as one of the arguments.
 *
 * @see BiPredicate
 * @see BiBytePredicate
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjPredicate.java", date = "2024-12-02T15:53:08.791588800Z")
public interface BiByteObjPredicate<T> extends
        BiPredicate<Byte, T>,
        BiByteObjFunction<T, Boolean> {
    /**
     * Evaluates this predicate on the {@code byte} arguments.
     */
    boolean test(byte left, T right);

    @Override
    default boolean test(Byte left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(byte left, T right) {
        return test(left, right);
    }

    @Override
    default Boolean apply(Byte left, T right) {
        return test((byte) left, right);
    }
}
