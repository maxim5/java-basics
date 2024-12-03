package io.spbx.util.testing.ext;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;

public class CloseAllExtension implements AfterEachCallback, AfterAllCallback {
    private final List<AutoCloseable> closeablesPerTest = new ArrayList<>();
    private final List<AutoCloseable> closeablesPerClass = new ArrayList<>();

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        for (AutoCloseable closeable : closeablesPerTest) {
            closeable.close();
        }
        closeablesPerTest.clear();      // Should clear in case of parameterized tests (not to run twice)
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        for (AutoCloseable closeable : closeablesPerClass) {
            closeable.close();
        }
        closeablesPerClass.clear();     // Should clear in case of parameterized tests (not to run twice)
    }

    @CanIgnoreReturnValue
    public <C extends AutoCloseable> @NotNull C addCloseablePerTest(@NotNull C closeable) {
        closeablesPerTest.add(closeable);
        return closeable;
    }

    @CanIgnoreReturnValue
    public <C extends AutoCloseable> @NotNull C addCloseablePerClass(@NotNull C closeable) {
        closeablesPerClass.add(closeable);
        return closeable;
    }
}
