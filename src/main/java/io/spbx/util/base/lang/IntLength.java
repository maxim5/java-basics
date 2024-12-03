package io.spbx.util.base.lang;

/**
 * Represents an abstract entity which has an {@code int} length.
 * Unlike size, the length of an entity is constant, moreover the entity itself is often immutable.
 *
 * @see io.spbx.util.collect.container.IntSize
 */
public interface IntLength {
    /**
     * Returns the {@code int} length. The result is always non-negative.
     */
    int length();

    /**
     * Returns whether this instance is empty, i.e. has a length of {@code 0}.
     */
    default boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Returns whether this instance is not empty, i.e. has a length greater than {@code 0}.
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
