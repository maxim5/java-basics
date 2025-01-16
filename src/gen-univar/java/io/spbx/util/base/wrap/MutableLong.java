package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;

/**
 * A simple mutable wrapper around {@code long}.
 */
@NotThreadSafe
@Generated(value = "Mutable$Type$.java", date = "2025-01-14T10:07:33.432111200Z")
public class MutableLong {
    public long value = 0;

    public MutableLong() {
    }

    public MutableLong(long value) {
        this.value = value;
    }

    public static @NotNull MutableLong newMutableLong() {
        return new MutableLong();
    }

    public static @NotNull MutableLong of(long value) {
        return new MutableLong(value);
    }

    public static @NotNull MutableLong of(@Nullable Long value) {
        return new MutableLong(value != null ? value : 0);
    }

    public long value() {
        return value;
    }

    public void set(long value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public void update(long delta) {
        value += delta;
    }

    public void reset() {
        value = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableLong that && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
