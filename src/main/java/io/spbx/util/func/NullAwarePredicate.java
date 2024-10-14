package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Represents a specialized {@link Predicate} which distinguishes nullable and non-null input and output.
 * <p>
 * The client may call {@link #test(Object)} just like a usual {@link Predicate}. This method allows nulls
 * for the input {@code T}. Or may use a non-null version {@link #testNotNull(Object)}.
 * <p>
 * Different implementations may
 * <ul>
 *     <li>do not allow null values and throw</li>
 *     <li>allow nullable values both ways and exclusively map {@code null} to {@code false}</li>
 *     <li>allow nullable values only in {@code T}</li>
 *     <li>mix and match nulls and non-nulls in some other way</li>
 * </ul>
 *
 * @param <T> argument type
 */
@FunctionalInterface
public interface NullAwarePredicate<T> extends Predicate<T> {
    /**
     * Evaluates this predicate on the non-null argument.
     *
     * @param t the (non-null) function argument
     *@return {@code true} if the input argument matches the predicate, otherwise {@code false}
     */
    boolean testNotNull(@NotNull T t);

    /**
     * Applies a null-allowable version of this function. By default, exclusively maps {@code null} to {@code false}.
     *
     * @param t the (nullable) function argument
     * @return the (nullable) function result
     */
    @Override
    default boolean test(@Nullable T t) {
        return t != null && testNotNull(t);
    }

    /**
     * Converts a simple non-null {@link Predicate} into a {@link NullAwarePredicate}.
     * A wrapped predicate safely accepts nullable inputs and passes them through.
     *
     * @param predicate the non-null predicate to wrap
     * @return the null-aware predicate
     */
    static <T> @NotNull NullAwarePredicate<T> wrap(@NotNull Predicate<T> predicate) {
        return predicate::test;
    }
}
