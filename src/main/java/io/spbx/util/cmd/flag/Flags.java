package io.spbx.util.cmd.flag;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.BasicExceptions.InternalErrors;
import io.spbx.util.cmd.CommandLine;
import io.spbx.util.cmd.CommandLineOptions;
import io.spbx.util.cmd.CommandLineSpec;
import io.spbx.util.func.ScopeFunctions;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Stateless
public class Flags {
    private static final Logger log = Logger.forEnclosingClass();

    public static @NotNull StringFlag.Builder defaultValue(@NotNull String defaultValue) {
        return StringFlag.defaultValue(defaultValue);
    }

    public static @NotNull IntFlag.Builder defaultValue(int defaultValue) {
        return IntFlag.defaultValue(defaultValue);
    }

    public static @NotNull LongFlag.Builder defaultValue(long defaultValue) {
        return LongFlag.defaultValue(defaultValue);
    }

    public static @NotNull BooleanFlag.Builder defaultValue(boolean defaultValue) {
        return BooleanFlag.defaultValue(defaultValue);
    }

    @CanIgnoreReturnValue
    public static @NotNull CommandLine initializeFromCommandLine(@NotNull String @NotNull[] args) {
        List<CommandLineSpec.ArgSpec> specs = Registry.instance().defineCommandLineSpecs();
        CommandLineSpec spec = CommandLineSpec.of(specs).allowArbitraryOptions(false);
        CommandLine commandLine = CommandLine.of(spec).initOrExit(args);
        Registry.instance().finalizeFlags(commandLine.options());
        return commandLine;
    }

    @ThreadSafe
    public static class Registry {
        private static final Registry instance = new Registry();

        public static @NotNull Registry instance() {
            return instance;
        }

        private static final int COLLECTING    = 0;
        private static final int DEFINING_SPEC = 1;
        private static final int DEFINED_SPEC  = 2;
        private static final int FINALIZING    = 3;
        private static final int COMPLETED     = 4;

        private final AtomicInteger state = new AtomicInteger(COLLECTING);
        private final ConcurrentLinkedQueue<BaseFlag<?>> queue = new ConcurrentLinkedQueue<>();
        private final AtomicReference<CommandLineOptions> optionsRef = new AtomicReference<>();

        public void registerFlag(@NotNull BaseFlag<?> flag) {
            int state = this.state.get();
            if (state == COLLECTING) {
                queue.add(flag);
                log.trace().log("Flag registered: %s", flag);
            } else {
                if (state != COMPLETED) {
                    queue.add(flag);
                }
                log.error().log("Flag registered too late [state=%s], trying to recover: %s", state, flag);
                ScopeFunctions.let(optionsRef.get(), flag::initialize);
                ScopeFunctions.let(optionsRef.get(), () -> queue.remove(flag));
            }
        }

        public @NotNull List<CommandLineSpec.ArgSpec> defineCommandLineSpecs() {
            InternalErrors.assure(state.compareAndSet(COLLECTING, DEFINING_SPEC), "Invalid state:", state);
            log.trace().log("Command-line spec definition started");

            List<CommandLineSpec.ArgSpec> result = queue.stream().map(BaseFlag::toArgSpec).toList();

            log.trace().log("Command-line spec defined");
            InternalErrors.assure(state.incrementAndGet() == DEFINED_SPEC, "Invalid state:", state);
            return result;
        }

        public void finalizeFlags(@NotNull CommandLineOptions options) {
            InternalErrors.assure(state.compareAndSet(DEFINED_SPEC, FINALIZING), "Invalid state:", state);
            log.trace().log("Flags finalization started");

            optionsRef.set(options);
            while (!queue.isEmpty()) {
                BaseFlag<?> flag = queue.poll();
                flag.initialize(options);
                log.trace().log("Flag finalized: %s", flag);
            }

            log.trace().log("Flags finalized");
            InternalErrors.assure(state.incrementAndGet() == COMPLETED, "Invalid state:", state);
        }

        @VisibleForTesting
        void reset() {
            state.set(COLLECTING);
            queue.clear();
            optionsRef.set(null);
        }
    }
}
