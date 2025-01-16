package io.spbx.util.cmd;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.base.tuple.Triple;
import io.spbx.util.collect.tab.RowListTabular;
import io.spbx.util.collect.tab.Tabular;
import io.spbx.util.collect.tab.TabularFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.concurrent.Immutable;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Immutable
public class CommandLine {
    static final CommandLine EMPTY = new CommandLine(new String[0], CommandLineArgs.EMPTY, CommandLineOptions.EMPTY);

    private final String[] originalArgs;
    private final CommandLineArgs args;
    private final CommandLineOptions options;

    CommandLine(@NotNull String @NotNull[] originalArgs,
                @NotNull CommandLineArgs args,
                @NotNull CommandLineOptions options) {
        this.originalArgs = originalArgs;
        this.args = args;
        this.options = options;
    }

    public static @NotNull Init of(@NotNull CommandLineSpec spec) {
        return CommandLine.of(spec, true);
    }

    public static @NotNull Init of(@NotNull CommandLineSpec spec, boolean processHelp) {
        return new Init(spec, processHelp);
    }

    public static @NotNull Init allowAll() {
        return CommandLine.of(CommandLineSpec.EMPTY_ALLOW_ALL);
    }

    public @NotNull ImmutableList<String> originalArgs() {
        return ImmutableList.copyOf(originalArgs);
    }

    public @NotNull CommandLineArgs args() {
        return args;
    }

    public @NotNull CommandLineOptions options() {
        return options;
    }

    public static final class Init {
        private final CommandLineSpec spec;
        private final boolean processHelp;

        Init(@NotNull CommandLineSpec spec, boolean processHelp) {
            this.spec = spec;
            this.processHelp = processHelp;
        }

        public @NotNull CommandLine initOrExit(@NotNull String @NotNull[] args) {
            try {
                return initOrThrow(args);
            } catch (InvalidCommandLineException e) {
                Exit.instance().exitWithError(e);
                return EMPTY;   // unreachable (unless Exit is mocked)
            }
        }

        public @NotNull CommandLine initOrThrow(@NotNull String @NotNull[] args) throws InvalidCommandLineException {
            if (isHelpRequested(args)) {
                Exit.instance().exitWithSuccess(toHelpMessage());
                return EMPTY;   // unreachable (unless Exit is mocked)
            }
            CommandLineParser.Result result = CommandLineParser.parse(args, spec);
            return new CommandLine(args, result.args(), result.options());
        }

        private boolean isHelpRequested(@NotNull String @NotNull [] args) {
            return processHelp && spec.isHelpEnabled() && args.length == 1 && isHelpRequested(args[0]);
        }

        private static boolean isHelpRequested(@NotNull String arg) {
            return CommandLineSpec.HELP_SPEC.isMatch(Predicate.isEqual(arg));
        }

        private @NotNull String toHelpMessage() {
            List<Triple<String, String, String>> rows = spec.allArgSpecs().stream()
                .sorted(Comparator.comparing(CommandLineSpec.ArgSpec::key))
                .map(Init::toHelpRow)
                .toList();
            Tabular<String> tabular = RowListTabular.ofTriples(rows);
            return TabularFormatter.BORDERLESS_FORMATTER.formatIntoTableString(tabular);
        }

        private static @NotNull Triple<String, String, String> toHelpRow(@NotNull CommandLineSpec.ArgSpec spec) {
            return Triple.of("%s %s".formatted(spec.key(), spec.aliases().isEmpty() ? "" : spec.aliases()),
                             spec.isMandatory() ? "mandatory" : "optional",
                             BasicStrings.firstNotEmpty(spec.help(), "<No doc provided>"));
        }
    }

    // Encapsulates exit calls, to avoid the ugly mess of System.exit() mocking:
    // https://stackoverflow.com/questions/309396/how-to-test-methods-that-call-system-exit
    @VisibleForTesting
    @Stateless
    static class Exit {
        private static final AtomicReference<Exit> instance = new AtomicReference<>(new Exit());

        static @NotNull Exit instance() {
            return instance.get();
        }

        void exitWithSuccess(@NotNull String message) {
            System.out.println(message);
            Runtime.getRuntime().exit(0);
        }

        void exitWithError(@NotNull InvalidCommandLineException e) {
            exitWithError(toJointErrorMessage(e));
        }

        void exitWithError(@NotNull String error) {
            System.err.println("Command-line arguments error");
            System.err.println(error);
            Runtime.getRuntime().exit(1);
        }

        private static @NotNull String toJointErrorMessage(@NotNull InvalidCommandLineException e) {
            return e.errors().stream().map(Throwable::getMessage).sorted().collect(Collectors.joining("\n"));
        }
    }
}
