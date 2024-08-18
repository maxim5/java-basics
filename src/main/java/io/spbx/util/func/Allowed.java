package io.spbx.util.func;

import io.spbx.util.base.Unchecked;
import io.spbx.util.collect.BasicIterables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Allowed<T> extends Predicate<T> {
    default @Nullable T allowOrNull(@Nullable T value) {
        return value != null && test(value) ? value : null;
    }

    default @NotNull Optional<T> allowOrEmpty(@Nullable T value) {
        return Optional.ofNullable(allowOrNull(value));
    }

    default @NotNull T allowOrDie(@Nullable T value, @NotNull Supplier<Throwable> error) {
        return value != null && test(value) ? value : Unchecked.throwAny(error.get());
    }

    default @NotNull T allowOrDie(@Nullable T value) {
        return allowOrDie(value, () -> new IllegalArgumentException("Value is not allowed: " + value));
    }

    static <E> @NotNull Allowed<E> allAllowed() {
        return e -> true;
    }

    static <E> @NotNull Allowed<E> allDisallowed() {
        return e -> false;
    }

    static <E> @NotNull Allowed<E> whitelistOf(@NotNull E value) {
        return e -> e.equals(value);
    }

    static <E> @NotNull Allowed<E> whitelistOf(@NotNull E value1, @NotNull E value2) {
        return e -> e.equals(value1) || e.equals(value2);
    }

    static <E> @NotNull Allowed<E> whitelistOf(@NotNull E value1, @NotNull E value2, @NotNull E value3) {
        return e -> e.equals(value1) || e.equals(value2) || e.equals(value3);
    }

    static @SafeVarargs <E> @NotNull Allowed<E> whitelistOf(@NotNull E @NotNull ... values) {
        return new Whitelist<>(Set.of(values));
    }

    static <E> @NotNull Allowed<E> whitelistOf(@NotNull Iterable<E> values) {
        return new Whitelist<>(BasicIterables.asSet(values));
    }

    static @SafeVarargs <E extends Enum<E>> @NotNull Allowed<E> whitelistOf(@NotNull E first, @NotNull E @NotNull ... rest) {
        return new Whitelist<>(EnumSet.of(first, rest));
    }

    static <E> @NotNull Allowed<E> blacklistOf(@NotNull E value) {
        return e -> !e.equals(value);
    }

    static <E> @NotNull Allowed<E> blacklistOf(@NotNull E value1, @NotNull E value2) {
        return e -> !e.equals(value1) && !e.equals(value2);
    }

    static <E> @NotNull Allowed<E> blacklistOf(@NotNull E value1, @NotNull E value2, @NotNull E value3) {
        return e -> !e.equals(value1) && !e.equals(value2) && !e.equals(value3);
    }

    static @SafeVarargs <E> @NotNull Allowed<E> blacklistOf(@NotNull E @NotNull ... values) {
        return new Blacklist<>(Set.of(values));
    }

    static <E> @NotNull Allowed<E> blacklistOf(@NotNull Iterable<E> values) {
        return new Blacklist<>(BasicIterables.asSet(values));
    }

    static @SafeVarargs <E extends Enum<E>> @NotNull Allowed<E> blacklistOf(@NotNull E first, @NotNull E @NotNull ... rest) {
        return new Blacklist<>(EnumSet.of(first, rest));
    }

    record Whitelist<E, C extends Collection<E>>(@NotNull C whitelist) implements Allowed<E> {
        @Override public boolean test(E e) {
            return whitelist.contains(e);
        }
    }

    record Blacklist<E, C extends Collection<E>>(@NotNull C blacklist) implements Allowed<E> {
        @Override public boolean test(E e) {
            return !blacklist.contains(e);
        }
    }
}
