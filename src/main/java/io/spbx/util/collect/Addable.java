package io.spbx.util.collect;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A wrapper for a value (any nullability) which is intended to be added to the builder.
 * So instead of
 * {@snippet lang = "java": builder.add(runFunction(params)); }
 * one could write
 * {@snippet lang = "java": runFunction(params).addTo(builder); }
 * which is slightly easier to read.
 */
public class Addable<T> implements Supplier<T> {
    private final T value;

    public Addable(T value) {
        this.value = value;
    }

    public static <T> @NotNull Addable<T> of(T value) {
        return new Addable<>(value);
    }

    @Override
    public T get() {
        return value;
    }

    public void addTo(@NotNull StringBuilder builder) {
        builder.append(value);
    }

    public void addTo(@NotNull ListBuilder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull ImmutableCollection.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull ImmutableList.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull ImmutableSet.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull ImmutableSortedSet.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull ImmutableMultiset.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull ImmutableSortedMultiset.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull AppendOnly<T> appendOnly) {
        appendOnly.append(value);
    }

    public void addTo(@NotNull Collection<T> collection) {
        collection.add(value);
    }

    public void andThen(@NotNull Consumer<T> consumer) {
        consumer.accept(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Addable<?> that && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Addable[%s]".formatted(value);
    }
}
