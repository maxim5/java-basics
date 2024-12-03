package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;

/**
 * A simple mutable wrapper around {@code int}.
 */
@NotThreadSafe
@Generated(value = "Mutable$Type$.java", date = "2024-12-02T15:53:08.702567700Z")
public class MutableInt {
    public int value = 0;

    public MutableInt() {
    }

    public MutableInt(int value) {
        this.value = value;
    }

    public static @NotNull MutableInt newMutableInt() {
        return new MutableInt();
    }

    public static @NotNull MutableInt of(int value) {
        return new MutableInt(value);
    }

    public static @NotNull MutableInt of(@Nullable Integer value) {
        return new MutableInt(value != null ? value : 0);
    }

    public int value() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public void update(int delta) {
        value += delta;
    }

    public void reset() {
        value = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableInt that && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
