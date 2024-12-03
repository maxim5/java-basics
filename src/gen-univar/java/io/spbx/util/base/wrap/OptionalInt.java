package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.IntOps;
import io.spbx.util.func.IntConsumer;
import io.spbx.util.func.IntFunction;
import io.spbx.util.func.IntPredicate;
import io.spbx.util.func.IntSupplier;
import io.spbx.util.func.IntUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A specialized {@code Optional} version for {@code int}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2024-12-02T15:53:08.708570Z")
public final class OptionalInt {
    private static final OptionalInt EMPTY = new OptionalInt(IntOps.ZERO);

    private final int value;

    private OptionalInt(int value) {
        this.value = value;
    }

    public static @NotNull OptionalInt empty() {
        return OptionalInt.EMPTY;
    }

    public static @NotNull OptionalInt of(int value) {
        return new OptionalInt(value);
    }

    public static @NotNull OptionalInt ofNonZero(int value) {
        return value == IntOps.ZERO ? OptionalInt.empty() : OptionalInt.of(value);
    }

    public static @NotNull OptionalInt ofNullable(@Nullable Integer value) {
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    public static @NotNull OptionalInt ofOptional(@Nullable Optional<Integer> optional) {
        return optional == null || optional.isEmpty() ? OptionalInt.empty() : OptionalInt.of(optional.orElseThrow());
    }

    public int value() {
        return value;
    }

    public int orElse(int other) {
        return isPresent() ? value : other;
    }

    public int orElseGet(@NotNull IntSupplier supplier) {
        return isPresent() ? value : supplier.getAsInt();
    }

    public int orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalInt.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalInt.EMPTY;
    }

    public void ifPresent(@NotNull IntConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull IntConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalInt filter(@NotNull IntPredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalInt.empty();
        }
    }

    public @NotNull OptionalInt mapToInt(@NotNull IntUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(mapper.applyToInt(value));
        }
    }

    public @NotNull OptionalInt flatMapToInt(@NotNull IntFunction<OptionalInt> mapper) {
        if (isEmpty()) {
            return OptionalInt.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull IntFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull IntFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }

    public @NotNull IntStream stream() {
        if (isEmpty()) {
            return IntStream.empty();
        } else {
            return IntStream.of(value);
        }
    }

    public @NotNull Optional<Integer> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalInt that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Integer.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalInt[" + value + "]" : "OptionalInt.empty";
    }
}
