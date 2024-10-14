package io.spbx.util.lazy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents the instance which might have a default value upon initialization, but may later be sealed with
 * the final value.
 * <p>
 * Once the instance is sealed, it is final and can't be changed, i.e. becomes immutable.
 * The instance can be sealed with the initial default value.
 * Both initial and sealed value can be {@code null}.
 * <p>
 * {@link Sealed} is technically not a lazy, because it's always initialized, but shares some properties, i.e.
 * "real" initialization is not immediate and must happen exactly once.
 *
 * @see Lazy
 */
public interface Sealed<T> extends Supplier<T> {
    /**
     * Returns true if this instance is sealed and no further changes are possible.
     */
    boolean isSealed();

    /**
     * Seals this instance with {@code value} if it's not already, otherwise fails
     * (even if it stores the same value).
     * Calling this method twice is guaranteed to throw.
     */
    void sealOrDie(@Nullable T value);

    /**
     * Seals the instance with its current value if it's not already, otherwise fails.
     * Calling this method twice is guaranteed to throw.
     */
    default void sealOrDie() {
        sealOrDie(this.get());
    }

    /**
     * Seals the instance with its current value after applying the {@code finalizer} if it's not already,
     * otherwise fails.
     * Calling this method twice is guaranteed to throw.
     */
    default void finalizeAndSealOrDie(@NotNull Function<T, T> finalizer) {
        sealOrDie(finalizer.apply(this.get()));
    }

    /**
     * Seals this instance if it's not already, otherwise does nothing.
     * This method never throws and does not compare values.
     */
    void sealIfNotYet(@Nullable T value);

    /**
     * Seals the instance with its current value if it's not already, otherwise does nothing.
     * This method never throws.
     */
    default void sealIfNotYet() {
        sealIfNotYet(this.get());
    }

    /**
     * Seals the instance with its current value after applying the {@code finalizer} if it's not already,
     * otherwise does nothing.
     * This method never throws.
     */
    default void finalizeAndSealIfNotYet(@NotNull Function<T, T> finalizer) {
        sealIfNotYet(finalizer.apply(this.get()));
    }

    /**
     * Seals this instance with {@code value} if it's not already, otherwise fails
     * (unless it stores the same value).
     * Calling this method twice with different arguments is guaranteed to throw.
     */
    void compareAndSeal(@Nullable T value);
}
