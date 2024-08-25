package io.spbx.util.buf;

import io.spbx.util.errors.RangeCheck;

/**
 * A base class for all array buffers.
 */
public abstract class BaseBuf extends RangeCheck {
    public abstract int length();

    /* Negative index translation and Range check */

    protected int translateIndex(int i) {
        return i >= 0 ? i : i + length();
    }

    protected boolean rangeCheck(int i, int flags) {
        return rangeCheck(i, length(), this, flags);
    }

    protected boolean rangeCheck(int i, int j, int flags) {
        return rangeCheck(i, j, length(), this, flags);
    }

    protected boolean outOfRangeCheck(int def) {
        return outOfRangeCheck(def, length(), this);
    }
}
