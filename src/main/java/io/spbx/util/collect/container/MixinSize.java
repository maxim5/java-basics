package io.spbx.util.collect.container;

/**
 * Represents an abstract container which can store more either up to {@link Integer#MAX_VALUE} items or
 * more than {@link Integer#MAX_VALUE} items, depending on the context or a particular implementation.
 * In case of the former, both {@link #size32()} and {@link #size64()} return the same value.
 * In case of the latter, {@link #size32()} is expected to saturize the size at {@link Integer#MAX_VALUE}.
 *
 * @see IntSize
 * @see LongSize
 */
public interface MixinSize {
    /**
     * Returns the {@code int} size of this container, i.e. total number of items.
     * The returned value is always non-negative.
     */
    int size32();

    /**
     * Returns the {@code long} size of this container, i.e. total number of items.
     * The returned value is always non-negative.
     */
    long size64();

    /**
     * Returns whether this container is empty, i.e. has a size of {@code 0}.
     */
    default boolean isEmpty() {
        return this.size64() == 0;
    }

    /**
     * Returns whether this container is not empty, i.e. has a size greater than {@code 0}.
     */
    default boolean isNotEmpty() {
        return !this.isEmpty();
    }
}
