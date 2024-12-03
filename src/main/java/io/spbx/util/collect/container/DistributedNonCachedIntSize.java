package io.spbx.util.collect.container;

import io.spbx.util.base.lang.Maybe;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.base.ops.IntOps.firstNonNegative;
import static io.spbx.util.collect.container.SizeOps.assertNonNegative;

/**
 * The {@link DistributedIntSize} specialization which does not support quick {@link #size()}
 * operation because the size is not cached in memory.
 */
public interface DistributedNonCachedIntSize extends DistributedIntSize {
    @Override
    int size();

    @Override
    default int size(@NotNull QueryOption option) {
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
    default int sizeOr(int def) {
        return firstNonNegative(size(QueryOption.ONLY_IF_CACHED), assertNonNegative(def));
    }
}
