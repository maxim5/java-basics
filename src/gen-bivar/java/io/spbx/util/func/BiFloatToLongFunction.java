package io.spbx.util.func;

import javax.annotation.processing.Generated;
import java.util.function.BiFunction;

/**
 * Represents an operation upon two {@code float}-valued operands and producing a
 * {@code long}-valued result.
 */
@FunctionalInterface
@Generated(value = "Bi$Left$To$Right$Function.java", date = "2025-01-14T10:38:22.052303600Z")
public interface BiFloatToLongFunction extends
        BiFunction<Float, Float, Long>,
        BiFloatFunction<Long>,
        BiToLongFunction<Float, Float>,
        BiFloatObjFunction<Float, Long> {
    /**
     * Applies this operator to the given operands.
     */
    long applyToLong(float left, float right);

    @Override
    default long applyToLong(Float left, Float right) {
        return applyToLong((float) left, (float) right);
    }

    @Override
    default Long apply(float left, float right) {
        return applyToLong(left, right);
    }

    @Override
    default Long apply(Float left, Float right) {
        return applyToLong((float) left, (float) right);
    }

    @Override
    default Long apply(float left, Float right) {
        return applyToLong(left, (float) right);
    }
}
