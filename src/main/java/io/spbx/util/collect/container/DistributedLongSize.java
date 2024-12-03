package io.spbx.util.collect.container;

import io.spbx.util.base.lang.Maybe;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.collect.container.QueryOption.FORCE_EXACT;
import static io.spbx.util.collect.container.QueryOption.ONLY_IF_CACHED;
import static io.spbx.util.collect.container.SizeOps.*;

/**
 * Represents a sizable entity, of which the clients may request the current size (in a number of ways).
 * <p>
 * The API assumes that the entity exact size is not necessarily stored in memory, so the clients can do either
 * a quick cheap query (isn't always available) or a potentially expensive query (though always works).
 * <p>
 * This interface can be used by various distributed containers. It is recommended to be extended directly
 * by the interfaces, while the concrete classes should better implement either {@link DistributedCachedLongSize}
 * or {@link DistributedNonCachedLongSize} depending on the implementation.
 *
 * @see QueryOption
 */
public interface DistributedLongSize extends LongSize {
    /**
     * Returns the {@code long} size given the {@code option}, or {@code -1} if the {@code option} is not supported.
     */
    long size(@NotNull QueryOption option);

    /**
     * Returns the {@code long} size using the best supported option.
     */
    @Override
    default long size() {
        long result;
        return (result = size(ONLY_IF_CACHED)) >= 0 ? result : assertNonNegative(size(FORCE_EXACT));
    }

    /**
     * Returns whether this container is empty, i.e. has a size of {@code 0}, given the {@code option}.
     * The result is {@link Maybe#UNKNOWN} if the {@code option} is unavailable.
     */
    default @NotNull Maybe isEmpty(@NotNull QueryOption option) {
        return switch (saturatedCast(size(option))) {
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
        return switch (saturatedCast(size(option))) {
            case -1 -> Maybe.UNKNOWN;
            case 0 -> Maybe.FALSE;
            default -> Maybe.TRUE;
        };
    }

    /**
     * Returns the {@code int} size of this container given the {@code option} saturated at {@link Integer#MAX_VALUE}.
     */
    default int saturatedIntSize(@NotNull QueryOption option) {
        return saturatedCast(size(option));
    }

    /**
     * Returns the {@code int} size of this container given the {@code option} if it's up to {@link Integer#MAX_VALUE},
     * throws otherwise.
     */
    default int exactIntSize(@NotNull QueryOption option) {
        return exactCast(size(option));
    }
}
