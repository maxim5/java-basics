package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code double} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code double}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjDoubleFunction
 * @see DoubleFunction
 * @see BiToDoubleFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2024-12-02T15:53:08.796590100Z")
public interface BiDoubleObjToDoubleFunction<T> extends BiDoubleObjFunction<T, Double>, BiToDoubleFunction<Double, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    double applyToDouble(double left, T right);

    @Override
    default Double apply(double left, T right) {
        return applyToDouble(left, right);
    }

    @Override
    default double applyToDouble(Double left, T right) {
        return applyToDouble((double) left, right);
    }

    @Override
    default Double apply(Double left, T right) {
        return applyToDouble((double) left, right);
    }
}
