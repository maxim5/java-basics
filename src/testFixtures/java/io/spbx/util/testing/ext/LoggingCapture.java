package io.spbx.util.testing.ext;

import com.google.common.flogger.AbstractLogger;
import io.spbx.util.logging.Logger;
import io.spbx.util.reflect.BasicMembers.FieldValue;
import io.spbx.util.reflect.BasicMembers.Fields;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import static io.spbx.util.base.BasicExceptions.InternalErrors.assureNonNull;
import static io.spbx.util.reflect.BasicMembers.hasType;

public class LoggingCapture extends FluentLoggingCapture {
    // See Logger.OptimizedLogger#getMinLoggableLevel() for details.
    private static final int ALL = 0;
    private static final int NO_VALUE = Integer.MIN_VALUE;

    protected LoggingCapture(@NotNull AbstractLogger<?> logger) {
        super(logger);
    }

    public static @NotNull LoggingCapture of(@NotNull Class<?> klass) {
        AbstractLogger<?> logger = getFlogger(klass);
        return new LoggingCapture(logger);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        super.beforeEach(context);
        injectThresholdValueToCache(logger, ALL);       // Makes sure no messages are blocked by the Flogger instance
    }

    @Override
    public void afterEach(ExtensionContext context) {
        super.afterEach(context);
        injectThresholdValueToCache(logger, NO_VALUE);  // Resets the cache
    }

    private static @NotNull AbstractLogger<?> getFlogger(@NotNull Class<?> klass) {
        Field field = Fields.of(klass).getOrDie(it -> hasType(it, Logger.class), () -> "No Logger found in: " + klass);
        Logger logger = (Logger) FieldValue.of(field).get(null);
        return assureNonNull(getFloggerOrNull(Logger.class, logger), "No Flogger found in `%s`", logger);
    }

    private static void injectThresholdValueToCache(@NotNull AbstractLogger<?> logger, int value) {
        Field field = Fields.of(logger.getClass()).getOrDie(it -> hasType(it, AtomicInteger.class));
        AtomicInteger cache = (AtomicInteger) FieldValue.of(field).getOrDie(logger);
        cache.set(value);
    }
}
