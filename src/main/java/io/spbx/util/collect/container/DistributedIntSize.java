package io.spbx.util.collect.container;

import io.spbx.util.base.lang.Maybe;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.collect.container.QueryOption.FORCE_EXACT;
import static io.spbx.util.collect.container.QueryOption.ONLY_IF_CACHED;
import static io.spbx.util.collect.container.SizeOps.assertNonNegative;

/**
 * Represents a distributed {@link IntSize} specialization which may not fit in memory.
 * <p>
 * The API assumes that the entity exact size is not necessarily stored in memory, so the clients can do either
 * a quick cheap query (isn't always available) or a potentially expensive query (though always works).
 *
 * @see QueryOption
 */
public interface DistributedIntSize extends IntSize {
    /**
     * Returns the {@code int} size given the {@code option}, or {@code -1} if the {@code option} is not supported.
     */
    int size(@NotNull QueryOption option);

    /**
     * Returns the {@code int} size using the best supported option.
     */
    @Override
    default int size() {
        int result;
        return (result = this.size(ONLY_IF_CACHED)) >= 0 ? result : assertNonNegative(this.size(FORCE_EXACT));
    }

    /**
     * Returns whether this container is empty, i.e. has a size of {@code 0}, given the {@code option}.
     * The result is {@link Maybe#UNKNOWN} if the {@code option} is unavailable.
     */
    default @NotNull Maybe isEmpty(@NotNull QueryOption option) {
        return switch (this.size(option)) {
            case -1 -> Maybe.UNKNOWN;
            case 0 -> Maybe.TRUE;
            default -> Maybe.FALSE;
        };
    }

    /**
     * Returns whether this container is not empty, i.e. has a size of {@code 0}, given the {@code option}.
     * The result is {@link Maybe#UNKNOWN} if the {@code option} is unavailable.
     */
    default @NotNull Maybe isNotEmpty(@NotNull QueryOption option) {
        return switch (this.size(option)) {
            case -1 -> Maybe.UNKNOWN;
            case 0 -> Maybe.FALSE;
            default -> Maybe.TRUE;
        };
    }
}
