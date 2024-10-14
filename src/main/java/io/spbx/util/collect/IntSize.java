package io.spbx.util.collect;

import io.spbx.util.base.Maybe;
import org.jetbrains.annotations.NotNull;

import static io.spbx.util.base.BasicExceptions.newInternalError;
import static io.spbx.util.collect.QueryOption.FORCE_EXACT;
import static io.spbx.util.collect.QueryOption.ONLY_IF_CACHED;

/**
 * Represents an abstract container which may store up to {@link Integer#MAX_VALUE} items,
 * i.e. the size fits into {@code int}.
 * <p>
 * It's expected that the {@link #size()} implementation is fast (works in {@code O(1)} and does not require I/O ops).
 * If this is not necessarily the case, the use of {@link DistributedIntSize} is recommended.
 *
 * @see LongSize
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
        return assertNonNegative(size()) == 0;
    }

    /**
     * Returns whether this container is not empty, i.e. has a size greater than {@code 0}.
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Represents a distributed {@link IntSize} specialization which may not fit in memory.
     * <p>
     * The API assumes that the entity exact size is not necessarily stored in memory, so the clients can do either
     * a quick cheap query (isn't always available) or a potentially expensive query (though always works).
     *
     * @see QueryOption
     */
    interface DistributedIntSize extends IntSize {
        /**
         * Returns the {@code int} size given the {@code option}, or {@code -1} if the {@code option} is not supported.
         */
        int size(@NotNull QueryOption option);

        /**
         * Returns the {@code int} size using the best supported option.
         */
        @Override default int size() {
            int result;
            return (result = size(ONLY_IF_CACHED)) >= 0 ? result : assertNonNegative(size(FORCE_EXACT));
        }

        /**
         * Returns whether this container is empty, i.e. has a size of {@code 0}, given the {@code option}.
         * The result is {@link Maybe#UNKNOWN} if the {@code option} is unavailable.
         */
        default @NotNull Maybe isEmpty(@NotNull QueryOption option) {
            return switch (size(option)) {
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
            return switch (size(option)) {
                case -1 -> Maybe.UNKNOWN;
                case 0 -> Maybe.FALSE;
                default -> Maybe.TRUE;
            };
        }
    }

    private static int assertNonNegative(int value) {
        assert value >= 0 : newInternalError("The size must be calculated exactly:", value);
        return value;
    }
}
