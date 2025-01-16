package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.ByteOps;
import io.spbx.util.func.ByteConsumer;
import io.spbx.util.func.ByteFunction;
import io.spbx.util.func.BytePredicate;
import io.spbx.util.func.ByteSupplier;
import io.spbx.util.func.ByteUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A specialized {@code Optional} version for {@code byte}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2025-01-14T10:07:33.436113500Z")
public final class OptionalByte {
    private static final OptionalByte EMPTY = new OptionalByte(ByteOps.ZERO);

    private final byte value;

    private OptionalByte(byte value) {
        this.value = value;
    }

    public static @NotNull OptionalByte empty() {
        return OptionalByte.EMPTY;
    }

    public static @NotNull OptionalByte of(byte value) {
        return new OptionalByte(value);
    }

    public static @NotNull OptionalByte ofNonZero(byte value) {
        return value == ByteOps.ZERO ? OptionalByte.empty() : OptionalByte.of(value);
    }

    public static @NotNull OptionalByte ofNullable(@Nullable Byte value) {
        return value == null ? OptionalByte.empty() : OptionalByte.of(value);
    }

    public static @NotNull OptionalByte ofOptional(@Nullable Optional<Byte> optional) {
        return optional == null || optional.isEmpty() ? OptionalByte.empty() : OptionalByte.of(optional.orElseThrow());
    }

    public byte value() {
        return value;
    }

    public byte orElse(byte other) {
        return isPresent() ? value : other;
    }

    public byte orElseGet(@NotNull ByteSupplier supplier) {
        return isPresent() ? value : supplier.getAsByte();
    }

    public byte orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalByte.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalByte.EMPTY;
    }

    public void ifPresent(@NotNull ByteConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull ByteConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalByte filter(@NotNull BytePredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalByte.empty();
        }
    }

    public @NotNull OptionalByte mapToByte(@NotNull ByteUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalByte.empty();
        } else {
            return OptionalByte.of(mapper.applyToByte(value));
        }
    }

    public @NotNull OptionalByte flatMapToByte(@NotNull ByteFunction<OptionalByte> mapper) {
        if (isEmpty()) {
            return OptionalByte.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull ByteFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull ByteFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }
    public @NotNull Stream<Byte> stream() {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public @NotNull Optional<Byte> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalByte that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Byte.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalByte[" + value + "]" : "OptionalByte.empty";
    }
}
