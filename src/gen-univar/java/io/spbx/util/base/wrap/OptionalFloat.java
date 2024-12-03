package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.FloatOps;
import io.spbx.util.func.FloatConsumer;
import io.spbx.util.func.FloatFunction;
import io.spbx.util.func.FloatPredicate;
import io.spbx.util.func.FloatSupplier;
import io.spbx.util.func.FloatUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A specialized {@code Optional} version for {@code float}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2024-12-02T15:53:08.708570Z")
public final class OptionalFloat {
    private static final OptionalFloat EMPTY = new OptionalFloat(FloatOps.ZERO);

    private final float value;

    private OptionalFloat(float value) {
        this.value = value;
    }

    public static @NotNull OptionalFloat empty() {
        return OptionalFloat.EMPTY;
    }

    public static @NotNull OptionalFloat of(float value) {
        return new OptionalFloat(value);
    }

    public static @NotNull OptionalFloat ofNonZero(float value) {
        return value == FloatOps.ZERO ? OptionalFloat.empty() : OptionalFloat.of(value);
    }

    public static @NotNull OptionalFloat ofNullable(@Nullable Float value) {
        return value == null ? OptionalFloat.empty() : OptionalFloat.of(value);
    }

    public static @NotNull OptionalFloat ofOptional(@Nullable Optional<Float> optional) {
        return optional == null || optional.isEmpty() ? OptionalFloat.empty() : OptionalFloat.of(optional.orElseThrow());
    }

    public float value() {
        return value;
    }

    public float orElse(float other) {
        return isPresent() ? value : other;
    }

    public float orElseGet(@NotNull FloatSupplier supplier) {
        return isPresent() ? value : supplier.getAsFloat();
    }

    public float orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalFloat.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalFloat.EMPTY;
    }

    public void ifPresent(@NotNull FloatConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull FloatConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalFloat filter(@NotNull FloatPredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalFloat.empty();
        }
    }

    public @NotNull OptionalFloat mapToFloat(@NotNull FloatUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalFloat.empty();
        } else {
            return OptionalFloat.of(mapper.applyToFloat(value));
        }
    }

    public @NotNull OptionalFloat flatMapToFloat(@NotNull FloatFunction<OptionalFloat> mapper) {
        if (isEmpty()) {
            return OptionalFloat.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull FloatFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull FloatFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }
    public @NotNull Stream<Float> stream() {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public @NotNull Optional<Float> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalFloat that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Float.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalFloat[" + value + "]" : "OptionalFloat.empty";
    }
}
