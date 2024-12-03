package io.spbx.util.collect.container;

import io.spbx.util.base.lang.Maybe;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link DistributedIntSize} specialization which guarantees quick {@link #size()}
 * operation because the size is cached in memory.
 */
public interface DistributedCachedIntSize extends DistributedIntSize {
    @Override
    int size();

    @Override
    default int size(@NotNull QueryOption option) {
        return this.size();
    }

    @Override
    default @NotNull Maybe isEmpty(@NotNull QueryOption option) {
        return Maybe.of(this.isEmpty());
    }

    @Override
    default @NotNull Maybe isNotEmpty(@NotNull QueryOption option) {
        return Maybe.of(this.isNotEmpty());
    }
}
