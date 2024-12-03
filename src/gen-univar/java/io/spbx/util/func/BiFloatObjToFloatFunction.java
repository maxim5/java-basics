package io.spbx.util.func;

import javax.annotation.processing.Generated;

/**
 * Represents a function that accepts two arguments and produces a {@code float} result.
 * This is the specialization of {@link BiFunction}, one of the arguments for which is an {@code float}
 * as well as the result.
 *
 * @param <T> the type of one of the arguments to the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see ObjFloatFunction
 * @see FloatFunction
 * @see BiToFloatFunction
 */
@FunctionalInterface
@Generated(value = "Bi$Type$ObjTo$Type$Function.java", date = "2024-12-02T15:53:08.796590100Z")
public interface BiFloatObjToFloatFunction<T> extends BiFloatObjFunction<T, Float>, BiToFloatFunction<Float, T> {
    /**
     * Applies this function to the given arguments.
     *
     * @param left the first function argument
     * @param right the second function argument
     * @return the function result
     */
    float applyToFloat(float left, T right);

    @Override
    default Float apply(float left, T right) {
        return applyToFloat(left, right);
    }

    @Override
    default float applyToFloat(Float left, T right) {
        return applyToFloat((float) left, right);
    }

    @Override
    default Float apply(Float left, T right) {
        return applyToFloat((float) left, right);
    }
}
