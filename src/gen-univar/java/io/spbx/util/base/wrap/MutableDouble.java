package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;

/**
 * A simple mutable wrapper around {@code double}.
 */
@NotThreadSafe
@Generated(value = "Mutable$Type$.java", date = "2025-01-14T10:07:33.432111200Z")
public class MutableDouble {
    public double value = 0;

    public MutableDouble() {
    }

    public MutableDouble(double value) {
        this.value = value;
    }

    public static @NotNull MutableDouble newMutableDouble() {
        return new MutableDouble();
    }

    public static @NotNull MutableDouble of(double value) {
        return new MutableDouble(value);
    }

    public static @NotNull MutableDouble of(@Nullable Double value) {
        return new MutableDouble(value != null ? value : 0);
    }

    public double value() {
        return value;
    }

    public void set(double value) {
        this.value = value;
    }

    public void reset() {
        value = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableDouble that && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
