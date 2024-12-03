package io.spbx.util.base.wrap;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Objects;

@NotThreadSafe
public class MutableObj<T> {
    public T value = null;

    public MutableObj() {
    }

    public MutableObj(T value) {
        this.value = value;
    }

    public static <T> @NotNull MutableObj<T> newMutableObj() {
        return new MutableObj<T>();
    }

    public static <T> @NotNull MutableObj<T> newMutableObj(T value) {
        return new MutableObj<>(value);
    }

    public T value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof MutableObj<?> that && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
