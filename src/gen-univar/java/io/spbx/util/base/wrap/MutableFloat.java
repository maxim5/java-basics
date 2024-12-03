package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;

/**
 * A simple mutable wrapper around {@code float}.
 */
@NotThreadSafe
@Generated(value = "Mutable$Type$.java", date = "2024-12-02T15:53:08.702567700Z")
public class MutableFloat {
    public float value = 0;

    public MutableFloat() {
    }

    public MutableFloat(float value) {
        this.value = value;
    }

    public static @NotNull MutableFloat newMutableFloat() {
        return new MutableFloat();
    }

    public static @NotNull MutableFloat of(float value) {
        return new MutableFloat(value);
    }

    public static @NotNull MutableFloat of(@Nullable Float value) {
        return new MutableFloat(value != null ? value : 0);
    }

    public float value() {
        return value;
    }

    public void set(float value) {
        this.value = value;
    }

    public void reset() {
        value = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableFloat that && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
