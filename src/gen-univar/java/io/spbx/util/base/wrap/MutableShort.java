package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;

/**
 * A simple mutable wrapper around {@code short}.
 */
@NotThreadSafe
@Generated(value = "Mutable$Type$.java", date = "2024-12-02T15:53:08.702567700Z")
public class MutableShort {
    public short value = 0;

    public MutableShort() {
    }

    public MutableShort(short value) {
        this.value = value;
    }

    public static @NotNull MutableShort newMutableShort() {
        return new MutableShort();
    }

    public static @NotNull MutableShort of(short value) {
        return new MutableShort(value);
    }

    public static @NotNull MutableShort of(@Nullable Short value) {
        return new MutableShort(value != null ? value : 0);
    }

    public short value() {
        return value;
    }

    public void set(short value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public void update(short delta) {
        value += delta;
    }

    public void reset() {
        value = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableShort that && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
