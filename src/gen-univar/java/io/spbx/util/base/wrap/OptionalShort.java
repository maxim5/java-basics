package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.ShortOps;
import io.spbx.util.func.ShortConsumer;
import io.spbx.util.func.ShortFunction;
import io.spbx.util.func.ShortPredicate;
import io.spbx.util.func.ShortSupplier;
import io.spbx.util.func.ShortUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A specialized {@code Optional} version for {@code short}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2025-01-14T10:07:33.436113500Z")
public final class OptionalShort {
    private static final OptionalShort EMPTY = new OptionalShort(ShortOps.ZERO);

    private final short value;

    private OptionalShort(short value) {
        this.value = value;
    }

    public static @NotNull OptionalShort empty() {
        return OptionalShort.EMPTY;
    }

    public static @NotNull OptionalShort of(short value) {
        return new OptionalShort(value);
    }

    public static @NotNull OptionalShort ofNonZero(short value) {
        return value == ShortOps.ZERO ? OptionalShort.empty() : OptionalShort.of(value);
    }

    public static @NotNull OptionalShort ofNullable(@Nullable Short value) {
        return value == null ? OptionalShort.empty() : OptionalShort.of(value);
    }

    public static @NotNull OptionalShort ofOptional(@Nullable Optional<Short> optional) {
        return optional == null || optional.isEmpty() ? OptionalShort.empty() : OptionalShort.of(optional.orElseThrow());
    }

    public short value() {
        return value;
    }

    public short orElse(short other) {
        return isPresent() ? value : other;
    }

    public short orElseGet(@NotNull ShortSupplier supplier) {
        return isPresent() ? value : supplier.getAsShort();
    }

    public short orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalShort.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalShort.EMPTY;
    }

    public void ifPresent(@NotNull ShortConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull ShortConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalShort filter(@NotNull ShortPredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalShort.empty();
        }
    }

    public @NotNull OptionalShort mapToShort(@NotNull ShortUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalShort.empty();
        } else {
            return OptionalShort.of(mapper.applyToShort(value));
        }
    }

    public @NotNull OptionalShort flatMapToShort(@NotNull ShortFunction<OptionalShort> mapper) {
        if (isEmpty()) {
            return OptionalShort.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull ShortFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull ShortFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }
    public @NotNull Stream<Short> stream() {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public @NotNull Optional<Short> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalShort that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Short.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalShort[" + value + "]" : "OptionalShort.empty";
    }
}
