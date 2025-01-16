package io.spbx.util.base.str;

import io.spbx.util.base.annotate.PyIndex;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.lang.IntLength;

/**
 * A base class for all array buffers.
 */
abstract class BaseBuf implements IntLength {
    /* Negative index translation and Range check */

    protected int translateIndex(@PyIndex int i) {
        return RangeCheck.translateIndex(this, i);
    }

    protected boolean rangeCheck(@PyIndex int i, int flags) {
        return RangeCheck.rangeCheck(this, i, flags);
    }

    protected boolean rangeCheck(@PyIndex int i, @PyIndex int j, int flags) {
        return RangeCheck.rangeCheck(this, i, j, flags);
    }

    protected boolean outOfRangeCheck(int def) {
        return RangeCheck.outOfRangeCheck(this, def);
    }
}
