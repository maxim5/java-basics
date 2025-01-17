package io.spbx.util.testing;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class ForcedFailure {
    private final AtomicReference<Throwable> failure;

    public ForcedFailure() {
        this(null);
    }

    public ForcedFailure(@Nullable Throwable throwable) {
        failure = new AtomicReference<>(throwable);
    }

    public boolean isSet() {
        return failure.get() != null;
    }

    @CanIgnoreReturnValue
    public boolean force(@NotNull Throwable throwable) {
        return failure.compareAndSet(null, throwable);
    }

    public void throwIfSet() {
        Throwable throwable = failure.get();
        if (throwable != null) {
            Unchecked.throwAny(throwable);
        }
    }
}
