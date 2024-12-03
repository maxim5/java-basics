package io.spbx.util.base.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@ThreadSafe
public class Gated<T> implements Supplier<T> {
    private volatile T init;
    private volatile T current;

    private Gated(@Nullable T init) {
        this.init = init;
        this.current = init;
    }

    public static <T> @NotNull Gated<T> of(@Nullable T init) {
        return new Gated<>(init);
    }

    @Override
    public @Nullable T get() {
        return current;
    }

    public @NotNull T nonNull() {
        assert current != null : "The value is null";
        return current;
    }

    public @NotNull T nonNullOr(@NotNull T def) {
        return current == null ? def : current;
    }

    public boolean isNull() {
        return current == null;
    }

    public boolean isNonNull() {
        return current != null;
    }

    public boolean isInitialValue() {
        return current == null;
    }

    public @Nullable T also(@NotNull Consumer<T> action) {
        action.accept(current);
        return current;
    }

    public T with(@NotNull UnaryOperator<T> action) {
        return action.apply(current);
    }

    public void assign(@Nullable T value) {
        current = value;
    }

    public void assignGated(@Nullable T value) {
        assert current == init : "The value is already initialized";
        current = value;
    }

    public void update(@NotNull Function<T, T> updater) {
        T prev = current, updated = updater.apply(prev);
        if (current == prev) {
            current = updated;
        }
    }

    public void updateGated(@NotNull Function<T, T> updater) {
        assert current == init : "The value is already initialized";
        update(updater);
    }

    public void reinit(@Nullable T value) {
        assert current == init : "The value is already initialized";
        init = current = value;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Gated<?> gated &&
                            Objects.equals(init, gated.init) && Objects.equals(current, gated.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(init, current);
    }

    @Override
    public String toString() {
        return "Gated{%s}".formatted(current);
    }
}
