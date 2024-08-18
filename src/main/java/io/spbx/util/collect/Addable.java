package io.spbx.util.collect;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A wrapper for a value which is intended to be added to the builder.
 * So instead of
 *
 * <pre>builder.add(runFunction(params));</pre>
 *
 * one could write
 *
 * <pre>runFunction(params).addTo(builder);</pre>
 *
 * which is slightly easier to read.
 */
public record Addable<T>(T value) implements Supplier<T> {
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

    public void addTo(@NotNull ImmutableList.Builder<T> builder) {
        builder.add(value);
    }

    public void addTo(@NotNull Collection<T> collection) {
        collection.add(value);
    }

    public void andThen(@NotNull Consumer<T> consumer) {
        consumer.accept(value);
    }
}
