package io.spbx.util.collect.container;

import io.spbx.util.base.lang.Maybe;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.base.ops.LongOps.firstNonNegative;
import static io.spbx.util.collect.container.SizeOps.*;

/**
 * The {@link DistributedLongSize} specialization which does not support quick {@link #size()}
 * operation because the size is not cached in memory.
 */
public interface DistributedNonCachedLongSize extends DistributedLongSize {
    @Override
    long size();

    @Override
    default long size(@NotNull QueryOption option) {
        return switch (option) {
            case ONLY_IF_CACHED -> -1;
            case FORCE_EXACT -> size();
        };
    }

    @Override
    default @NotNull Maybe isEmpty(@NotNull QueryOption option) {
        return switch (option) {
            case ONLY_IF_CACHED -> Maybe.UNKNOWN;
            case FORCE_EXACT -> Maybe.of(isEmpty());
        };
    }

    /**
     * Returns the {@code long} size of this container if the {@code option} is supported or a default {@code def}.
     */
    default long sizeOr(long def) {
        return firstNonNegative(size(QueryOption.ONLY_IF_CACHED), assertNonNegative(def));
    }

    /**
     * Returns the saturated {@code int} size of this container if the {@code option} is supported
     * or a default {@code def}.
     * @see #saturatedIntSize()
     */
    default int saturatedIntSize(int def) {
        return saturatedCast(sizeOr(def));
    }

    /**
     * Returns the saturated {@code int} size of this container if the {@code option} is supported
     * or a default {@code def}.
     * @see #exactIntSize()
     */
    default int exactIntSize(int def) {
        return exactCast(sizeOr(def));
    }
}
