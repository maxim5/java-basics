package io.spbx.util.collect.container;

import static io.spbx.util.collect.container.SizeOps.assertNonNegative;

/**
 * Represents an abstract container which may store up to {@link Integer#MAX_VALUE} items,
 * i.e. the size fits into {@code int}.
 * <p>
 * It's expected that the {@link #size()} implementation is fast (works in {@code O(1)} and does not require I/O ops).
 * If this is not necessarily the case, the use of {@link DistributedIntSize} is recommended.
 *
 * @see LongSize
 * @see io.spbx.util.base.lang.IntLength
 */
public interface IntSize {
    /**
     * Returns the {@code int} size of this container.
     */
    int size();

    /**
     * Returns whether this container is empty, i.e. has a size of {@code 0}.
     */
    default boolean isEmpty() {
        return assertNonNegative(this.size()) == 0;
    }

    /**
     * Returns whether this container is not empty, i.e. has a size greater than {@code 0}.
     */
    default boolean isNotEmpty() {
        return !this.isEmpty();
    }
}
