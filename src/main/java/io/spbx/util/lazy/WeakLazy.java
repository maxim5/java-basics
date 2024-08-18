package io.spbx.util.lazy;

import com.google.errorprone.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.function.Supplier;

/**
 * Allows to store a lazy non-null value. The clients only provide a way to supply it.
 * <p>
 * Similar to {@link Lazy} utility, but allows the value to be garbage collected and re-provided from the
 * same supplier. The supplier may be called many times and is expected to always return the same value (idempotency).
 *
 * @see Lazy
 */
@ThreadSafe
public class WeakLazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private volatile WeakReference<T> ref = new WeakReference<>(null);

    public WeakLazy(@NotNull Supplier<@NotNull T> supplier) {
        this.supplier = supplier;
    }

    public static <T> @NotNull WeakLazy<T> of(@NotNull Supplier<@NotNull T> supplier) {
        return supplier instanceof WeakLazy<T> lazy ? lazy : new WeakLazy<>(supplier);
    }

    @Override
    public @NotNull T get() {
        T value = ref.get();
        if (value == null) {
            value = supplier.get();
            ref = new WeakReference<>(value);
        }
        return value;
    }
}
