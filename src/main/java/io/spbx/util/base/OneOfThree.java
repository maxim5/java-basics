package io.spbx.util.base;

import com.google.errorprone.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Holds an immutable triple of nullable objects, only one of which is set at a time.
 *
 * @see Triple
 * @see OneOf
 * @see Tuple
 */
@Immutable
public final class OneOfThree<U, V, W> implements Serializable {
    private final U first;
    private final V second;
    private final W third;
    private final Which which;

    private OneOfThree(U first, V second, W third, Which which) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.which = which;
    }

    public static <U, V, W> @NotNull OneOfThree<U, V, W> of(@Nullable U first, @Nullable V second, @Nullable W third) {
        assert first == null ? second == null ^ third == null : second == null && third == null :
            "Exactly one must be non-null: first=%s second=%s third=%s".formatted(first, second, third);
        if (first != null) {
            return ofFirst(first);
        } else if (second != null) {
            return ofSecond(second);
        } else {
            return ofThird(third);
        }
    }

    public static <U, V, W> @NotNull OneOfThree<U, V, W> ofFirst(@Nullable U first) {
        assert first != null : "Expected non-null `first`";
        return new OneOfThree<>(first, null, null, Which.FIRST);
    }

    public static <U, V, W> @NotNull OneOfThree<U, V, W> ofSecond(@Nullable V second) {
        assert second != null : "Expected non-null `second`";
        return new OneOfThree<>(null, second, null, Which.SECOND);
    }

    public static <U, V, W> @NotNull OneOfThree<U, V, W> ofThird(@Nullable W third) {
        assert third != null : "Expected non-null `third`";
        return new OneOfThree<>(null, null, third, Which.THIRD);
    }

    public @NotNull Which getCase() {
        return which;
    }

    public boolean hasFirst() {
        return which == Which.FIRST;
    }

    public U first() {
        return first;
    }

    public boolean hasSecond() {
        return which == Which.SECOND;
    }

    public V second() {
        return second;
    }

    public boolean hasThird() {
        return which == Which.THIRD;
    }

    public W third() {
        return third;
    }

    public @NotNull Triple<U, V, W> toTriple() {
        return Triple.of(first, second, third);
    }

    public <T, S, Q> @NotNull OneOfThree<T, S, Q> map(@NotNull Function<U, T> convertFirst,
                                                      @NotNull Function<V, S> convertSecond,
                                                      @NotNull Function<W, Q> convertThird) {
        return mapToObj(first -> ofFirst(convertFirst.apply(first)),
                        second -> ofSecond(convertSecond.apply(second)),
                        third -> ofThird(convertThird.apply(third)));
    }

    public <T> @NotNull OneOfThree<T, V, W> mapFirst(@NotNull Function<U, T> convert) {
        assert hasFirst() : "Can't mapFirst() because first is not set: " + this;
        return map(convert, second -> second, third -> third);
    }

    public <T> @NotNull OneOfThree<U, T, W> mapSecond(@NotNull Function<V, T> convert) {
        assert hasSecond() : "Can't mapSecond() because second is not set: " + this;
        return map(first -> first, convert, third -> third);
    }

    public <T> @NotNull OneOfThree<U, V, T> mapThird(@NotNull Function<W, T> convert) {
        assert hasThird() : "Can't mapThird() because second is not set: " + this;
        return map(first -> first, second -> second, convert);
    }

    public <T> @NotNull T mapToObj(@NotNull Function<U, T> fromFirst,
                                   @NotNull Function<V, T> fromSecond,
                                   @NotNull Function<W, T> fromThird) {
        return switch (which) {
            case FIRST -> fromFirst.apply(first);
            case SECOND -> fromSecond.apply(second);
            case THIRD -> fromThird.apply(third);
        };
    }

    public int mapToInt(@NotNull ToIntFunction<U> fromFirst,
                        @NotNull ToIntFunction<V> fromSecond,
                        @NotNull ToIntFunction<W> fromThird) {
        return switch (which) {
            case FIRST -> fromFirst.applyAsInt(first);
            case SECOND -> fromSecond.applyAsInt(second);
            case THIRD -> fromThird.applyAsInt(third);
        };
    }

    public long mapToLong(@NotNull ToLongFunction<U> fromFirst,
                          @NotNull ToLongFunction<V> fromSecond,
                          @NotNull ToLongFunction<W> fromThird) {
        return switch (which) {
            case FIRST -> fromFirst.applyAsLong(first);
            case SECOND -> fromSecond.applyAsLong(second);
            case THIRD -> fromThird.applyAsLong(third);
        };
    }

    public double mapToDouble(@NotNull ToDoubleFunction<U> fromFirst,
                              @NotNull ToDoubleFunction<V> fromSecond,
                              @NotNull ToDoubleFunction<W> fromThird) {
        return switch (which) {
            case FIRST -> fromFirst.applyAsDouble(first);
            case SECOND -> fromSecond.applyAsDouble(second);
            case THIRD -> fromThird.applyAsDouble(third);
        };
    }

    public boolean testFirstIfSet(@NotNull Predicate<U> predicate) {
        return hasFirst() && predicate.test(first);
    }

    public boolean testSecondIfSet(@NotNull Predicate<V> predicate) {
        return hasSecond() && predicate.test(second);
    }

    public boolean testThirdIfSet(@NotNull Predicate<W> predicate) {
        return hasThird() && predicate.test(third);
    }

    public boolean test(@NotNull Predicate<U> forFirst, @NotNull Predicate<V> forSecond, @NotNull Predicate<W> forThird) {
        return switch (which) {
            case FIRST -> forFirst.test(first);
            case SECOND -> forSecond.test(second);
            case THIRD -> forThird.test(third);
        };
    }

    public void apply(@NotNull Consumer<U> takeFirst, @NotNull Consumer<V> takeSecond, @NotNull Consumer<W> takeThird) {
        switch (which) {
            case FIRST -> takeFirst.accept(first);
            case SECOND -> takeSecond.accept(second);
            case THIRD -> takeThird.accept(third);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OneOfThree<?, ?, ?> that &&
               Objects.equals(this.first, that.first) &&
               Objects.equals(this.second, that.second) &&
               Objects.equals(this.third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return mapToObj("OneOf{first=%s}"::formatted, "OneOf{second=%s}"::formatted, "OneOf{third=%s}"::formatted);
    }

    public enum Which {
        FIRST,
        SECOND,
        THIRD,
    }
}
