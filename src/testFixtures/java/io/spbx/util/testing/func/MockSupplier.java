package io.spbx.util.testing.func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Supplier;

public class MockSupplier<T> implements Supplier<T> {
    private final T[] returns;
    private int pos;

    protected MockSupplier(@Nullable T @NotNull[] returns) {
        this.returns = returns;
    }

    public static @SafeVarargs <T> @NotNull MockSupplier<T> mock(@Nullable T @NotNull... returns) {
        return new MockSupplier<>(returns);
    }

    @Override
    public T get() {
        assert pos < returns.length :
            "MockSupplier is called too many times: %d, returns=%s".formatted(pos + 1, Arrays.toString(returns));
        return returns[pos++];
    }

    public int timesCalled() {
        return pos;
    }
}
