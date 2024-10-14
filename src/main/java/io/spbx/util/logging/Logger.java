package io.spbx.util.logging;

import com.google.common.flogger.AbstractLogger;
import com.google.common.flogger.LogContext;
import com.google.common.flogger.LoggingApi;
import com.google.common.flogger.backend.Platform;
import com.google.common.flogger.parser.DefaultPrintfMessageParser;
import com.google.common.flogger.parser.MessageParser;
import com.google.common.flogger.util.CallerFinder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import static io.spbx.util.base.BasicExceptions.newIllegalStateException;

/**
 * A logger class which extends Google Fluent logging capabilities but at the same time hides it to avoid a direct dependency.
 */
public final class Logger {
    private final AbstractLogger<Api> logger;

    private Logger(@NotNull AbstractLogger<Api> flogger) {
        this.logger = flogger;
    }

    public static @NotNull Logger forEnclosingClass() {
        // NOTE: It is _vital_ that the call to "caller finder" is made directly inside the static factory method.
        String loggingClass = findLoggingClass(Logger.class);
        return new Logger(new OptimizedLogger(loggingClass));
    }

    public @NotNull Api fatal() {
        return at(Levels.FATAL);
    }

    public @NotNull Api error() {
        return at(Levels.ERROR);
    }

    public @NotNull Api warn() {
        return at(Levels.WARN);
    }

    public @NotNull Api info() {
        return at(Levels.INFO);
    }

    public @NotNull Api debug() {
        return at(Levels.DEBUG);
    }

    public @NotNull Api trace() {
        return at(Levels.TRACE);
    }

    public @NotNull Api ultra() {
        return at(Levels.ULTRA);
    }

    public @NotNull Api at(@NotNull Level level) {
        return logger.at(level);
    }

    /**
     * The non-wildcard, fully specified, logging API for this logger. Fluent logger implementations
     * should specify a non-wildcard API like this with which to generify the abstract logger.
     */
    public interface Api extends LoggingApi<Api> {}

    /**
     * The non-wildcard, fully specified, no-op API implementation. This is required to provide a
     * no-op implementation whose type is compatible with this logger's API.
     */
    private static final class NoOp extends LoggingApi.NoOp<Api> implements Api {}

    /**
     * Returns the name of the immediate caller of the given logger class. This is useful when
     * determining the class name with which to create a logger backend.
     *
     * @param loggerClass the class containing the log() methods whose caller we need to find.
     * @return the name of the class that called the specified logger.
     * @throws IllegalStateException if there was no caller of the specified logged passed on the
     *     stack (which may occur if the logger class was invoked directly by JNI).
     *
     * @see Platform.LogCallerFinder#findLoggingClass
     */
    private static @NotNull String findLoggingClass(@NotNull Class<?> loggerClass) {
        // We can skip at most only 1 method from the analysis, the inferLoggingClass() method itself.
        StackTraceElement caller = CallerFinder.findCallerOf(loggerClass, 1);
        if (caller != null) {
            return caller.getClassName();   // This might contain '$' for inner/nested classes, but that's okay.
        }
        throw newIllegalStateException("No caller found on the stack for:", loggerClass.getName());
    }

    // Applies the following optimizations:
    // - Force logging is disabled.
    // - Min loggable level is evaluated once.
    private static final class OptimizedLogger extends AbstractLogger<Api> {
        private OptimizedLogger(@NotNull String className) {
            super(Platform.getBackend(className));
        }

        @Override public @NotNull Api at(@NotNull Level level) {
            return isLoggableOptimized(level) ? new Context(level) : NO_OP;
        }

        private boolean isLoggableOptimized(@NotNull Level level) {
            return level.intValue() >= getMinLoggableLevel();
        }

        private static final AtomicInteger MinLoggableLevelCache = new AtomicInteger(Integer.MIN_VALUE);

        private int getMinLoggableLevel() {
            if (MinLoggableLevelCache.get() == Integer.MIN_VALUE) {
                int level = Levels.binarySearchMinLoggableLevel(value -> isLoggable(new Level("dummy", value) {}));
                MinLoggableLevelCache.compareAndSet(Integer.MIN_VALUE, level);
            }
            return MinLoggableLevelCache.get();
        }

        // Singleton instance of the no-op API. This variable is purposefully declared as an instance of
        // the NoOp type instead of the Api type. This helps ProGuard optimization recognize the type of
        // this field more easily. This allows ProGuard to strip away low-level logs in Android apps in
        // fewer optimization passes. Do not change this to 'Api', or any less specific type.
        private static final NoOp NO_OP = new NoOp();

        /** Logging context implementing the fully specified API for this logger. */
        private final class Context extends LogContext<OptimizedLogger, Api> implements Api {
            private Context(Level level) {
                super(level, false);
            }

            @Override protected OptimizedLogger getLogger() {
                return OptimizedLogger.this;
            }

            @Override protected Api api() {
                return this;
            }

            @Override protected Api noOp() {
                return NO_OP;
            }

            @Override protected MessageParser getMessageParser() {
                return DefaultPrintfMessageParser.getInstance();
            }
        }
    }
}
