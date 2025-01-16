package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code float}-valued operands and producing a
 * {@code int}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiFloatToIntFunction extends
        BiFunction<Float, Float, Integer>,
        BiFloatFunction<Integer>,
        BiToIntFunction<Float, Float>,
        BiFloatObjFunction<Float, Integer> {
    /**
     * Applies this operator to the given operands.
     */
    int applyToInt(float left, float right);

    @Override
    default int applyToInt(Float left, Float right) {
        return applyToInt((float) left, (float) right);
    }

    @Override
    default Integer apply(float left, float right) {
        return applyToInt(left, right);
    }

    @Override
    default Integer apply(Float left, Float right) {
        return applyToInt((float) left, (float) right);
    }

    @Override
    default Integer apply(float left, Float right) {
        return applyToInt(left, (float) right);
    }
}
