package io.spbx.util.lazy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class AtomicSealed<T> implements Sealed<T> {
    private final AtomicReference<T> ref;
    private final AtomicBoolean sealed = new AtomicBoolean();

    protected AtomicSealed(@Nullable T initialValue) {
        ref = new AtomicReference<>(initialValue);
    }

    public static <T> @NotNull Sealed<T> create(@Nullable T initialValue) {
        return new AtomicSealed<>(initialValue);
    }

    @Override
    public T get() {
        return ref.get();
    }

    @Override
    public boolean isSealed() {
        return sealed.get();
    }

    @Override
    public void sealOrDie(@Nullable T value) {
        boolean success = sealed.compareAndSet(false, true);
        assert success : "Invalid state. %s is already sealed: %s".formatted(getClass().getSimpleName(), ref.get());
        ref.set(value);
    }

    @Override
    public void sealOrDie() {
        boolean success = sealed.compareAndSet(false, true);
        assert success : "Invalid state. %s is already sealed: %s".formatted(getClass().getSimpleName(), ref.get());
    }

    @Override
    public void sealIfNotYet(@Nullable T value) {
        if (sealed.compareAndSet(false, true)) {
            ref.set(value);
        }
    }

    @Override
    public void sealIfNotYet() {
        sealed.set(true);
    }

    @Override
    public void compareAndSeal(@Nullable T value) {
        boolean success = sealed.compareAndSet(false, true);
        assert success || ref.get() == value :
            "Invalid state. %s already sealed with another value: %s. New value: %s"
                .formatted(getClass().getSimpleName(), ref.get(), value);
        ref.set(value);
    }
}
