package io.spbx.util.collect.container;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

import static io.spbx.util.collect.container.SizeOps.*;

/**
 * Represents an abstract container which may potentially store more than {@link Integer#MAX_VALUE} items,
 * i.e. the size does not fit into {@code int}.
 * <p>
 * It's expected that the {@link #size()} implementation is fast (works in {@code O(1)} and does not require I/O ops).
 * If this is not necessarily the case, the use of {@link DistributedLongSize} is recommended.
 *
 * @see IntSize
 * @see io.spbx.util.base.lang.LongLength
 */
public interface LongSize {
    /**
     * Returns the {@code long} size of this container, i.e. total number of items.
     */
    long size();

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

    /**
     * Returns the {@code int} size of this container saturated at {@link Integer#MAX_VALUE}.
     */
    default int saturatedIntSize() {
        return saturatedCast(this.size());
    }

    /**
     * Returns the {@code int} size of this container if it's up to {@link Integer#MAX_VALUE}, throws otherwise.
     */
    default int exactIntSize() {
        return exactCast(this.size());
    }

    /**
     * Returns the {@code long} size of the provided {@code collection}.
     */
    static long sizeOf(@NotNull Collection<?> collection) {
        return collection instanceof LongSize longSize ? longSize.size() : collection.size();
    }

    /**
     * Returns the {@code long} size of the provided {@code map}.
     */
    static long sizeOf(@NotNull Map<?, ?> map) {
        return map instanceof LongSize longSize ? longSize.size() : map.size();
    }
}
