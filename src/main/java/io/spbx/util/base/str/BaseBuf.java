package io.spbx.util.base.str;

import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.lang.IntLength;

/**
 * A base class for all array buffers.
 */
public abstract class BaseBuf implements IntLength {
    /* Negative index translation and Range check */

    protected int translateIndex(int i) {
        return RangeCheck.translateIndex(this, i);
    }

    protected boolean rangeCheck(int i, int flags) {
        return RangeCheck.rangeCheck(this, i, flags);
    }

    protected boolean rangeCheck(int i, int j, int flags) {
        return RangeCheck.rangeCheck(this, i, j, flags);
    }

    protected boolean outOfRangeCheck(int def) {
        return RangeCheck.outOfRangeCheck(this, def);
    }
}
