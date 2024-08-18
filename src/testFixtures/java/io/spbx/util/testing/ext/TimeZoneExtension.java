package io.spbx.util.testing.ext;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.TimeZone;

public class TimeZoneExtension implements BeforeAllCallback, AfterAllCallback {
    private final TimeZone original;
    private final TimeZone forced;

    private TimeZoneExtension(@NotNull TimeZone timeZone) {
        this.original = TimeZone.getDefault();
        this.forced = timeZone;
    }

    public static @NotNull TimeZoneExtension force(@NotNull TimeZone timeZone) {
        return new TimeZoneExtension(timeZone);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        TimeZone.setDefault(forced);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        TimeZone.setDefault(original);
    }
}
