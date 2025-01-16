package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;

/**
 * Represents a reversible function {@code long}-valued specialization: from {@code T} to {@code long}.
 *
 * @param <T> the type to convert
 * @see Reversible
 */
@Generated(value = "$Type$Reversible.java", date = "2025-01-14T10:07:33.480123100Z")
public interface LongReversible<T> extends Reversible<T, Long> {
    /**
     * A non-null version of the converter from {@code T} to {@code long}.
     *
     * @param t the (non-null) function argument in {@code T} space
     * @return the function result in {@code long} space
     */
    long forwardToLong(@NotNull T t);

    /**
     * A non-null version of the converter from {@code long} to {@code T}.
     *
     * @param i the function argument in {@code long} space
     * @return the (non-null) function result in {@code T} space
     */
    @NotNull T backward(long i);

    @Override default @NotNull Long forward(@NotNull T t) {
        return forwardToLong(t);
    }

    @Override default @NotNull T backward(@NotNull Long i) {
        return backward((long) i);
    }

    @Override default @NotNull ToLongReversible<T> reverse() {
        return new ToLongReversible<>() {
            @Override public @NotNull T forward(long i) {
                return LongReversible.this.backward(i);
            }

            @Override public long backwardToLong(@NotNull T t) {
                return LongReversible.this.forwardToLong(t);
            }

            @Override public @NotNull LongReversible<T> reverse() {
                return LongReversible.this;
            }
        };
    }

    /**
     * The inverse {@code long}-valued reversible function specialization: from {@code T} to {@code long}.
     *
     * <b>Note: </b> to ensure consistency, do not implement this interface directly,
     * implement {@code LongReversible} instead.
     *
     * @param <T> the type to convert
     */
    interface ToLongReversible<T> extends Reversible<Long, T> {
        /**
         * A non-null version of the converter from {@code long} to {@code T}.
         *
         * @param i the (non-null) function argument in {@code long} space
         * @return the (non-null) function result in {@code T} space
         */
        @NotNull T forward(long i);

        /**
         * A non-null version of the converter from {@code long} to {@code T}.
         *
         * @param t the (non-null) function argument in {@code long} space
         * @return the (non-null) function result in {@code T} space
         */
        long backwardToLong(@NotNull T t);

        @Override default @NotNull T forward(@NotNull Long i) {
            return forward((long) i);
        }

        @Override default @NotNull Long backward(@NotNull T t) {
            return backwardToLong(t);
        }
    }
}
