package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.DoubleOps;
import io.spbx.util.func.DoubleConsumer;
import io.spbx.util.func.DoubleFunction;
import io.spbx.util.func.DoublePredicate;
import io.spbx.util.func.DoubleSupplier;
import io.spbx.util.func.DoubleUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A specialized {@code Optional} version for {@code double}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2024-12-02T15:53:08.708570Z")
public final class OptionalDouble {
    private static final OptionalDouble EMPTY = new OptionalDouble(DoubleOps.ZERO);

    private final double value;

    private OptionalDouble(double value) {
        this.value = value;
    }

    public static @NotNull OptionalDouble empty() {
        return OptionalDouble.EMPTY;
    }

    public static @NotNull OptionalDouble of(double value) {
        return new OptionalDouble(value);
    }

    public static @NotNull OptionalDouble ofNonZero(double value) {
        return value == DoubleOps.ZERO ? OptionalDouble.empty() : OptionalDouble.of(value);
    }

    public static @NotNull OptionalDouble ofNullable(@Nullable Double value) {
        return value == null ? OptionalDouble.empty() : OptionalDouble.of(value);
    }

    public static @NotNull OptionalDouble ofOptional(@Nullable Optional<Double> optional) {
        return optional == null || optional.isEmpty() ? OptionalDouble.empty() : OptionalDouble.of(optional.orElseThrow());
    }

    public double value() {
        return value;
    }

    public double orElse(double other) {
        return isPresent() ? value : other;
    }

    public double orElseGet(@NotNull DoubleSupplier supplier) {
        return isPresent() ? value : supplier.getAsDouble();
    }

    public double orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalDouble.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalDouble.EMPTY;
    }

    public void ifPresent(@NotNull DoubleConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull DoubleConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalDouble filter(@NotNull DoublePredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalDouble.empty();
        }
    }

    public @NotNull OptionalDouble mapToDouble(@NotNull DoubleUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of(mapper.applyToDouble(value));
        }
    }

    public @NotNull OptionalDouble flatMapToDouble(@NotNull DoubleFunction<OptionalDouble> mapper) {
        if (isEmpty()) {
            return OptionalDouble.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull DoubleFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull DoubleFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }
    public @NotNull Stream<Double> stream() {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public @NotNull Optional<Double> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalDouble that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Double.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalDouble[" + value + "]" : "OptionalDouble.empty";
    }
}
