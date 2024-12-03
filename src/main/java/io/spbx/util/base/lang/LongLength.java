package io.spbx.util.base.lang;

/**
 * Represents an abstract entity which has a {@code long} length.
 * Unlike size, the length of an entity is constant, moreover the entity itself is often immutable.
 *
 * @see io.spbx.util.collect.container.LongSize
 */
public interface LongLength {
    /**
     * Returns the {@code long} length. The result is always non-negative.
     */
    long length();

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
