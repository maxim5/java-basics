package io.spbx.util.cmd;

import com.google.common.truth.StringSubject;
import io.spbx.util.cmd.TestingCommandLine.MockExit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.cmd.TestingCommandLine.spec;
import static io.spbx.util.testing.TestingBasics.arrayOf;

@Tag("fast")
public class CommandLineTest {
    @Test
    public void init_error_options_missing() {
        mockExit(() -> {
            CommandLineSpec spec = CommandLineSpec.of(spec("--foo"));
            CommandLine.of(spec).initOrExit(arrayOf());
        }).assertThatFailure().isEqualTo("Mandatory option is missing: `--foo`");

        mockExit(() -> {
            CommandLineSpec spec = CommandLineSpec.of(spec("--foo", "-f"));
            CommandLine.of(spec).initOrExit(arrayOf());
        }).assertThatFailure().isEqualTo("Mandatory option is missing: `--foo` [-f]");

        mockExit(() -> {
            CommandLineSpec spec = CommandLineSpec.of(spec("--foo"), spec("-bar"));
            CommandLine.of(spec).initOrExit(arrayOf());
        }).assertThatFailure().isEqualTo("""
                                         Mandatory option is missing: `--foo`
                                         Mandatory option is missing: `-bar`""");
    }

    @Test
    public void init_help() {
        mockExit(() -> {
            CommandLineSpec spec = CommandLineSpec.of(spec("--foo", "-f"), spec("--bar"));
            CommandLine.of(spec).initOrExit(arrayOf("--help"));
        }).assertThatSuccess().isEqualTo("""
            --bar        mandatory  <No doc provided>
            --foo [-f]   mandatory  <No doc provided>
            --help [-h]  optional   Prints help      \
            """);
    }

    private static @NotNull Exited mockExit(@NotNull Runnable runnable) {
        try (MockExit mockExit = MockExit.install()) {
            runnable.run();
            return new Exited(mockExit.success(), mockExit.failure());
        }
    }

    private record Exited(@Nullable String success, @Nullable String failure) {
        public @NotNull StringSubject assertThatSuccess() {
            return assertThat(success);
        }
        public @NotNull StringSubject assertThatFailure() {
            return assertThat(failure);
        }
    }
}
