package io.spbx.util.collect;

/**
 * Represents the way the clients may query the container size.
 *
 * @see IntSize
 * @see LongSize
 */
public enum QueryOption {
    /**
     * For the queries which are expected to return quickly, i.e.
     * if the exact size is available in memory, does not require I/O ops and can be easily calculated in {@code O(1)}.
     */
    ONLY_IF_CACHED,
    /**
     * For the potentially expensive queries which must return the exact size
     * even if it requires fetching I/O data or longer than {@code O(1)} calculation, e.g. counting.
     */
    FORCE_EXACT,
}
