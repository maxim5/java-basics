package io.spbx.util.lazy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a group of {@link Sealed} instances.
 * <p>
 * The group can be updated via {@link #addToGroup(Sealed)} and {@link #createAndAdd(Object)} methods
 * until it is itself sealed. Upon seal, both the group and all constituent instances are sealed and immutable.
 * <p>
 * The {@link SealedGroup} is most useful when a number of {@link Sealed} instances possibly with different lifecycles
 * need to be finalized and sealed at once.
 *
 * @see Sealed
 */
public interface SealedGroup extends Iterable<Sealed<?>> {
    /**
     * Returns true if this group is sealed and no further updates are possible.
     */
    boolean isSealed();

    /**
     * Seals this group if it's not already, otherwise fails.
     * All constituent instances are sealed if they are not already.
     */
    void sealAllOrDie();

    /**
     * Seals this group if it's not already, otherwise does nothing.
     * All constituent instances are sealed if they are not already.
     */
    void sealAllIfNotYet();

    /**
     * Adds the new {@code sealed} instance to the group. The {@code sealed} may be already sealed before this call.
     */
    <T> void addToGroup(@NotNull Sealed<T> sealed);

    /**
     * Creates the new {@code sealed} instance with specified {@code initialValue} and adds it to the group.
     */
    <T> @NotNull Sealed<T> createAndAdd(@Nullable T initialValue);
}
