package io.spbx.util.testing.ext;

import com.google.common.flogger.AbstractLogger;
import com.google.common.flogger.backend.LogData;
import com.google.common.flogger.backend.LoggerBackend;
import com.google.common.flogger.backend.TemplateContext;
import io.spbx.util.func.ThrowRunnable;
import io.spbx.util.reflect.BasicMembers.FieldValue;
import io.spbx.util.reflect.BasicMembers.Fields;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.error.BasicExceptions.InternalErrors.assureNonNull;
import static io.spbx.util.reflect.BasicMembers.hasType;

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

    protected final AbstractLogger<?> logger;
    private LoggerBackend backend;
    private List<LogData> logRecords;

    protected FluentLoggingCapture(@NotNull AbstractLogger<?> logger) {
        this.logger = logger;
    }

    public static @NotNull FluentLoggingCapture of(@NotNull Class<?> klass) {
        AbstractLogger<?> logger = getFlogger(klass);
        return new FluentLoggingCapture(logger);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        backend = extractBackend(logger);
        injectBackend(logger, buildMock(backend));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        injectBackend(logger, assureNonNull(backend));
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
        return assureNonNull(logRecords);
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

    private static @NotNull LoggerBackend extractBackend(@NotNull AbstractLogger<?> logger) {
        Field field = Fields.of(AbstractLogger.class).getOrDie("backend");
        return (LoggerBackend) FieldValue.of(field).getOrDie(logger);
    }

    private static void injectBackend(@NotNull AbstractLogger<?> logger, @NotNull LoggerBackend backend) {
        Field field = Fields.of(AbstractLogger.class).getOrDie("backend");
        FieldValue.of(field).set(logger, backend);
    }

    private static <T> @NotNull AbstractLogger<?> getFlogger(@NotNull Class<T> klass) {
        return assureNonNull(getFloggerOrNull(klass, null), "No Flogger found in `%s`", klass);
    }

    protected static <T> @Nullable AbstractLogger<?> getFloggerOrNull(@NotNull Class<T> klass, @Nullable T instance) {
        Field field = Fields.of(klass).find(it -> hasType(it, AbstractLogger.class));
        return (AbstractLogger<?>) FieldValue.of(field).get(instance);
    }
}
