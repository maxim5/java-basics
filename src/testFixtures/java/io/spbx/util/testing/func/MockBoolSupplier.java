package io.spbx.util.testing.func;

import com.google.common.primitives.Booleans;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class MockBoolSupplier extends MockSupplier<Boolean> implements BooleanSupplier {
    protected MockBoolSupplier(boolean[] returns) {
        super(Booleans.asList(returns).toArray(Boolean[]::new));
    }

    public static @NotNull MockBoolSupplier of(boolean... returns) {
        return new MockBoolSupplier(returns);
    }

    @Override
    public boolean getAsBoolean() {
        return get();
    }
}
