package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class MutableBool {
    public boolean value = false;

    public MutableBool() {
    }

    public MutableBool(boolean value) {
        this.value = value;
    }

    public static @NotNull MutableBool newMutableBool() {
        return new MutableBool();
    }

    public static @NotNull MutableBool of(boolean value) {
        return new MutableBool(value);
    }

    public static @NotNull MutableBool of(@Nullable Boolean value) {
        return new MutableBool(value != null ? value : false);
    }

    public boolean value() {
        return value;
    }

    public void flip() {
        value = !value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public void set() {
        this.value = true;
    }

    public void clear() {
        this.value = false;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableBool that && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
