package io.spbx.util.testing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class TestEnv {
    public static void assumeWindows(@Nullable String reason) {
        assumeTrue(OS.current() == OS.WINDOWS, () -> ignoreMessage("Windows", reason));
    }

    public static void assumeWindows() {
        assumeWindows(null);
    }

    public static void assumeLinux(@Nullable String reason) {
        assumeTrue(OS.current() == OS.LINUX, () -> ignoreMessage("Linux", reason));
    }

    public static void assumeLinux() {
        assumeLinux(null);
    }

    private static @NotNull String ignoreMessage(@NotNull String env, @Nullable String reason) {
        return "The test is only running in %s%s. Skipping".formatted(env, reason != null ? " (" + reason + ")" : "");
    }
}
