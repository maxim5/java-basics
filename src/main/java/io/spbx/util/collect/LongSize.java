package io.spbx.util.collect;

import io.spbx.util.base.Maybe;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.base.BasicExceptions.newInternalError;
import static io.spbx.util.collect.QueryOption.FORCE_EXACT;
import static io.spbx.util.collect.QueryOption.ONLY_IF_CACHED;

/**
 * Represents an abstract container which may potentially store more than {@link Integer#MAX_VALUE} items,
 * i.e. the size does not fit into {@code int}.
 * <p>
 * It's expected that the {@link #size()} implementation is fast (works in {@code O(1)} and does not require I/O ops).
 * If this is not necessarily the case, the use of {@link DistributedLongSize} is recommended.
 *
 * @see IntSize
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
        return assertNonNegative(size()) == 0;
    }

    /**
     * Returns whether this container is not empty, i.e. has a size greater than {@code 0}.
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Returns the {@code int} size of this container saturated at {@link Integer#MAX_VALUE}.
     */
    default int saturatedIntSize() {
        return saturatedCast(size());
    }

    /**
     * Returns the {@code int} size of this container if it's up to {@link Integer#MAX_VALUE}, throws otherwise.
     */
    default int exactIntSize() {
        return exactCast(size());
    }

    /**
     * Represents a sizable entity, of which the clients may request the current size (in a number of ways).
     * <p>
     * The API assumes that the entity exact size is not necessarily stored in memory, so the clients can do either
     * a quick cheap query (isn't always available) or a potentially expensive query (though always works).
     *
     * @see QueryOption
     */
    interface DistributedLongSize extends LongSize {
        /**
         * Returns the {@code long} size given the {@code option}, or {@code -1} if the {@code option} is not supported.
         */
        long size(@NotNull QueryOption option);

        /**
         * Returns the {@code long} size using the best supported option.
         */
        @Override default long size() {
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

    private static long assertNonNegative(long value) {
        assert value >= 0 : newInternalError("The size must be calculated exactly:", value);
        return value;
    }

    private static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    private static int exactCast(long value) {
        int result = (int) value;
        assert result == value : "Out of range: " + value;
        return result;
    }
}
