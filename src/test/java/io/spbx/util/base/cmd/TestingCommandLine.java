package io.spbx.util.base.cmd;

import io.spbx.util.base.annotate.MustBeClosed;
import io.spbx.util.io.UncheckedClosable;
import io.spbx.util.reflect.BasicMembers.FieldValue;
import io.spbx.util.reflect.BasicMembers.Fields;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.collect.stream.BasicStreams.streamOf;

public class TestingCommandLine {
    public static @NotNull CommandLineSpec.ArgSpec spec(@NotNull String name) {
        return CommandLineSpec.ArgSpec.of(name);
    }

    public static @NotNull CommandLineSpec.ArgSpec spec(@NotNull String name, @NotNull String alias) {
        return CommandLineSpec.ArgSpec.of(name, alias);
    }

    public static @NotNull CommandLineSpec.ArgSpec spec(@NotNull String @NotNull... names) {
        assert names.length > 0 : "Empty names provided";
        return CommandLineSpec.ArgSpec.of(names[0], streamOf(names).skip(1).toList());
    }

    public static class MockExit extends CommandLine.Exit implements UncheckedClosable {
        private final CommandLine.Exit ref;
        private final AtomicReference<String> success = new AtomicReference<>();
        private final AtomicReference<String> failure = new AtomicReference<>();

        private MockExit(@NotNull CommandLine.Exit ref) {
            this.ref = ref;
        }

        @MustBeClosed
        public static @NotNull MockExit install() {
            MockExit mockExit = new MockExit(CommandLine.Exit.instance());
            injectInstance(mockExit);
            return mockExit;
        }

        public @Nullable String success() {
            return success.get();
        }

        public @Nullable String failure() {
            return failure.get();
        }

        @Override void exitWithSuccess(@NotNull String message) {
            success.set(message);
        }

        @Override void exitWithError(@NotNull String error) {
            failure.set(error);
        }

        @Override public void close() {
            injectInstance(ref);
        }
    }

    public static class ThrowingExit extends CommandLine.Exit implements UncheckedClosable {
        private final CommandLine.Exit ref;

        private ThrowingExit(@NotNull CommandLine.Exit ref) {
            this.ref = ref;
        }

        @MustBeClosed
        public static @NotNull ThrowingExit install() {
            ThrowingExit throwingExit = new ThrowingExit(CommandLine.Exit.instance());
            injectInstance(throwingExit);
            return throwingExit;
        }

        @Override void exitWithError(@NotNull InvalidCommandLineException e) {
            throw e;
        }

        @Override public void close() {
            injectInstance(ref);
        }
    }

    private static void injectInstance(@NotNull CommandLine.Exit exit) {
        AtomicReference<CommandLine.Exit> ref = castAny(FieldValue.of(Fields.of(CommandLine.Exit.class).getOrDie("instance")).getOrDie(null));
        ref.set(exit);
    }
}
