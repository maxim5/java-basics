package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.LongOps;
import io.spbx.util.func.LongConsumer;
import io.spbx.util.func.LongFunction;
import io.spbx.util.func.LongPredicate;
import io.spbx.util.func.LongSupplier;
import io.spbx.util.func.LongUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.LongStream;

/**
 * A specialized {@code Optional} version for {@code long}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2025-01-14T10:07:33.436113500Z")
public final class OptionalLong {
    private static final OptionalLong EMPTY = new OptionalLong(LongOps.ZERO);

    private final long value;

    private OptionalLong(long value) {
        this.value = value;
    }

    public static @NotNull OptionalLong empty() {
        return OptionalLong.EMPTY;
    }

    public static @NotNull OptionalLong of(long value) {
        return new OptionalLong(value);
    }

    public static @NotNull OptionalLong ofNonZero(long value) {
        return value == LongOps.ZERO ? OptionalLong.empty() : OptionalLong.of(value);
    }

    public static @NotNull OptionalLong ofNullable(@Nullable Long value) {
        return value == null ? OptionalLong.empty() : OptionalLong.of(value);
    }

    public static @NotNull OptionalLong ofOptional(@Nullable Optional<Long> optional) {
        return optional == null || optional.isEmpty() ? OptionalLong.empty() : OptionalLong.of(optional.orElseThrow());
    }

    public long value() {
        return value;
    }

    public long orElse(long other) {
        return isPresent() ? value : other;
    }

    public long orElseGet(@NotNull LongSupplier supplier) {
        return isPresent() ? value : supplier.getAsLong();
    }

    public long orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalLong.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalLong.EMPTY;
    }

    public void ifPresent(@NotNull LongConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull LongConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalLong filter(@NotNull LongPredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalLong.empty();
        }
    }

    public @NotNull OptionalLong mapToLong(@NotNull LongUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalLong.empty();
        } else {
            return OptionalLong.of(mapper.applyToLong(value));
        }
    }

    public @NotNull OptionalLong flatMapToLong(@NotNull LongFunction<OptionalLong> mapper) {
        if (isEmpty()) {
            return OptionalLong.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull LongFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull LongFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }

    public @NotNull LongStream stream() {
        if (isEmpty()) {
            return LongStream.empty();
        } else {
            return LongStream.of(value);
        }
    }

    public @NotNull Optional<Long> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalLong that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Long.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalLong[" + value + "]" : "OptionalLong.empty";
    }
}
