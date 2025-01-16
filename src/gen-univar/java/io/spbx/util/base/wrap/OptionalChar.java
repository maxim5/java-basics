package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.spbx.util.base.ops.CharOps;
import io.spbx.util.func.CharConsumer;
import io.spbx.util.func.CharFunction;
import io.spbx.util.func.CharPredicate;
import io.spbx.util.func.CharSupplier;
import io.spbx.util.func.CharUnaryOperator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A specialized {@code Optional} version for {@code char}.
 *
 * @see Optional
 */
@Immutable
@Generated(value = "Optional$Type$.java", date = "2025-01-14T10:07:33.436113500Z")
public final class OptionalChar {
    private static final OptionalChar EMPTY = new OptionalChar(CharOps.ZERO);

    private final char value;

    private OptionalChar(char value) {
        this.value = value;
    }

    public static @NotNull OptionalChar empty() {
        return OptionalChar.EMPTY;
    }

    public static @NotNull OptionalChar of(char value) {
        return new OptionalChar(value);
    }

    public static @NotNull OptionalChar ofNonZero(char value) {
        return value == CharOps.ZERO ? OptionalChar.empty() : OptionalChar.of(value);
    }

    public static @NotNull OptionalChar ofNullable(@Nullable Character value) {
        return value == null ? OptionalChar.empty() : OptionalChar.of(value);
    }

    public static @NotNull OptionalChar ofOptional(@Nullable Optional<Character> optional) {
        return optional == null || optional.isEmpty() ? OptionalChar.empty() : OptionalChar.of(optional.orElseThrow());
    }

    public char value() {
        return value;
    }

    public char orElse(char other) {
        return isPresent() ? value : other;
    }

    public char orElseGet(@NotNull CharSupplier supplier) {
        return isPresent() ? value : supplier.getAsChar();
    }

    public char orElseThrow() {
        if (isPresent()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this != OptionalChar.EMPTY;
    }

    public boolean isEmpty() {
        return this == OptionalChar.EMPTY;
    }

    public void ifPresent(@NotNull CharConsumer action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(@NotNull CharConsumer action, @NotNull Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public @NotNull OptionalChar filter(@NotNull CharPredicate predicate) {
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(value) ? this : OptionalChar.empty();
        }
    }

    public @NotNull OptionalChar mapToChar(@NotNull CharUnaryOperator mapper) {
        if (isEmpty()) {
            return OptionalChar.empty();
        } else {
            return OptionalChar.of(mapper.applyToChar(value));
        }
    }

    public @NotNull OptionalChar flatMapToChar(@NotNull CharFunction<OptionalChar> mapper) {
        if (isEmpty()) {
            return OptionalChar.empty();
        } else {
            return mapper.apply(value);
        }
    }

    public <U> @NotNull Optional<U> map(@NotNull CharFunction<? extends U> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    public <U> @NotNull Optional<U> flatMap(@NotNull CharFunction<? extends Optional<? extends U>> mapper) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            return (Optional<U>) mapper.apply(value);
        }
    }
    public @NotNull Stream<Character> stream() {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public @NotNull Optional<Character> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OptionalChar that && this.value == that.value && this.isPresent() == that.isPresent();
    }

    @Override
    public int hashCode() {
        return isPresent() ? Character.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent() ? "OptionalChar[" + value + "]" : "OptionalChar.empty";
    }
}
