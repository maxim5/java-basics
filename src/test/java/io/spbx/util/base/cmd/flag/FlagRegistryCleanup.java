package io.spbx.util.base.cmd.flag;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

class FlagRegistryCleanup implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        Flags.Registry.instance().reset();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Flags.Registry.instance().reset();
    }
}
