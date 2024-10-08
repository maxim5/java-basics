package io.spbx.util.classpath;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class BasicClasspath {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public static @Nullable Class<?> classForNameOrNull(@NotNull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignore) {
            return null;
        } catch (Throwable throwable) {
            log.at(Level.WARNING).withCause(throwable).log("Class loading failed for name: `%s`", name);
            return null;
        }
    }

    public static boolean isInClasspath(@NotNull String name) {
        return classForNameOrNull(name) != null;
    }
}
