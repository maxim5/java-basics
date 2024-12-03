package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;

/**
 * Represents a reversible function {@code int}-valued specialization: from {@code T} to {@code int}.
 *
 * @param <T> the type to convert
 * @see Reversible
 */
@Generated(value = "$Type$Reversible.java", date = "2024-12-02T15:53:08.758581200Z")
public interface IntReversible<T> extends Reversible<T, Integer> {
    /**
     * A non-null version of the converter from {@code T} to {@code int}.
     *
     * @param t the (non-null) function argument in {@code T} space
     * @return the function result in {@code int} space
     */
    int forwardToInt(@NotNull T t);

    /**
     * A non-null version of the converter from {@code int} to {@code T}.
     *
     * @param i the function argument in {@code int} space
     * @return the (non-null) function result in {@code T} space
     */
    @NotNull T backward(int i);

    @Override default @NotNull Integer forward(@NotNull T t) {
        return forwardToInt(t);
    }

    @Override default @NotNull T backward(@NotNull Integer i) {
        return backward((int) i);
    }

    @Override default @NotNull ToIntReversible<T> reverse() {
        return new ToIntReversible<>() {
            @Override public @NotNull T forward(int i) {
                return IntReversible.this.backward(i);
            }

            @Override public int backwardToInt(@NotNull T t) {
                return IntReversible.this.forwardToInt(t);
            }

            @Override public @NotNull IntReversible<T> reverse() {
                return IntReversible.this;
            }
        };
    }

    /**
     * The inverse {@code int}-valued reversible function specialization: from {@code T} to {@code int}.
     *
     * <b>Note: </b> to ensure consistency, do not implement this interface directly,
     * implement {@code IntReversible} instead.
     *
     * @param <T> the type to convert
     */
    interface ToIntReversible<T> extends Reversible<Integer, T> {
        /**
         * A non-null version of the converter from {@code int} to {@code T}.
         *
         * @param i the (non-null) function argument in {@code int} space
         * @return the (non-null) function result in {@code T} space
         */
        @NotNull T forward(int i);

        /**
         * A non-null version of the converter from {@code int} to {@code T}.
         *
         * @param t the (non-null) function argument in {@code int} space
         * @return the (non-null) function result in {@code T} space
         */
        int backwardToInt(@NotNull T t);

        @Override default @NotNull T forward(@NotNull Integer i) {
            return forward((int) i);
        }

        @Override default @NotNull Integer backward(@NotNull T t) {
            return backwardToInt(t);
        }
    }
}
