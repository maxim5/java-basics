package io.spbx.util.testing.ext;

import com.google.common.flogger.AbstractLogger;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.backend.LogData;
import com.google.common.flogger.backend.LoggerBackend;
import com.google.common.flogger.backend.TemplateContext;
import io.spbx.util.base.Unchecked;
import io.spbx.util.func.ThrowRunnable;
import io.spbx.util.reflect.BasicMembers.Fields;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Objects.requireNonNull;

public class FluentLoggingCapture implements BeforeEachCallback, AfterEachCallback {
    // Expose `String` levels so that the tests do not depend on Log4j directly.
    public static final String ALL   = Level.ALL.name();
    public static final String TRACE = Level.TRACE.name();
    public static final String DEBUG = Level.DEBUG.name();
    public static final String INFO  = Level.INFO.name();
    public static final String WARN  = Level.WARN.name();
    public static final String ERROR = Level.ERROR.name();
    public static final String FATAL = Level.FATAL.name();
    public static final String OFF   = Level.OFF.name();

    private final FluentLogger logger;
    private LoggerBackend backend;
    private List<LogData> logRecords;

    public FluentLoggingCapture(@NotNull Class<?> klass) {
        logger = Unchecked.Suppliers.runRethrow(() -> getFluentLogger(klass));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        backend = extractBackend(logger);
        injectBackend(logger, buildMock(backend));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        injectBackend(logger, requireNonNull(backend));
    }

    public <E extends Throwable> void withCustomLog4jLevel(@NotNull String newLevel,
                                                           @NotNull ThrowRunnable<E> runnable) throws E {
        // https://stackoverflow.com/questions/46736414/how-to-find-my-previously-set-log4j-level
        withCustomLog4jLevel(LogManager.getLogger(backend.getLoggerName()).getLevel(), Level.toLevel(newLevel), runnable);
    }

    public <E extends Throwable> void withCustomLog4jLevel(@NotNull String oldLevel,
                                                           @NotNull String newLevel,
                                                           @NotNull ThrowRunnable<E> runnable) throws E {
        withCustomLog4jLevel(Level.toLevel(oldLevel), Level.toLevel(newLevel), runnable);
    }

    <E extends Throwable> void withCustomLog4jLevel(@NotNull Level oldLevel,
                                                    @NotNull Level newLevel,
                                                    @NotNull ThrowRunnable<E> runnable) throws E {
        // Alternative recommended way:
        // https://stackoverflow.com/questions/23434252/programmatically-change-log-level-in-log4j2
        Configurator.setLevel(backend.getLoggerName(), newLevel);
        try {
            runnable.run();
        } finally {
            Configurator.setLevel(backend.getLoggerName(), oldLevel);
        }
    }

    public @NotNull List<LogData> logRecords() {
        return requireNonNull(logRecords);
    }

    public @NotNull List<LogData> logRecordsContaining(@NotNull String substr) {
        return logRecords().stream().filter(data -> formatMessage(data).contains(substr)).toList();
    }

    public @NotNull List<LogData> logRecordsMatching(@NotNull String regex) {
        Pattern pattern = Pattern.compile(regex);
        return logRecords().stream().filter(data -> pattern.matcher(formatMessage(data)).find()).toList();
    }

    public void assertNoRecords() {
        assertThat(logRecords).isEmpty();
    }

    private static @NotNull String formatMessage(@NotNull LogData data) {
        TemplateContext context = data.getTemplateContext();
        if (context != null) {
            String message = context.getMessage();
            Object[] arguments = data.getArguments();
            return message.formatted(arguments);
        } else {
            return String.valueOf(data.getLiteralArgument());
        }
    }

    private @NotNull LoggerBackend buildMock(@NotNull LoggerBackend instance) {
        logRecords = new ArrayList<>();
        LoggerBackend mock = Mockito.spy(instance);
        Mockito.doAnswer(invocation -> {
            logRecords.add(invocation.getArgument(0));
            return invocation.callRealMethod();
        }).when(mock).log(Mockito.any());
        return mock;
    }

    private static @NotNull LoggerBackend extractBackend(@NotNull FluentLogger logger) throws IllegalAccessException {
        Field field = Fields.of(AbstractLogger.class).getOrDie("backend");
        field.setAccessible(true);
        return (LoggerBackend) field.get(logger);
    }

    private static void injectBackend(@NotNull FluentLogger logger,
                                      @NotNull LoggerBackend backend) throws IllegalAccessException {
        Field field = Fields.of(AbstractLogger.class).getOrDie("backend");
        field.setAccessible(true);
        field.set(logger, backend);
    }

    private static @NotNull FluentLogger getFluentLogger(@NotNull Class<?> klass) throws IllegalAccessException {
        Field field = Fields.of(klass).getOrDie(it -> it.getType().equals(FluentLogger.class));
        field.setAccessible(true);
        return (FluentLogger) field.get(null);
    }
}
