package io.spbx.util.collect.container;

import io.spbx.util.base.annotate.Stateless;

import static io.spbx.util.base.error.BasicExceptions.newInternalError;

@Stateless
class SizeOps {
    static int assertNonNegative(int value) {
        assert value >= 0 : newInternalError("The size must be calculated exactly (must be >= 0):", value);
        return value;
    }

    static long assertNonNegative(long value) {
        assert value >= 0 : newInternalError("The size must be calculated exactly (must be >= 0):", value);
        return value;
    }

    static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    static int exactCast(long value) {
        int result = (int) value;
        assert result == value : "Out of range: " + value;
        return result;
    }
}
