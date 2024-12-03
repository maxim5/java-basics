package io.spbx.util.classpath;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Stateless
public class BasicClasspath {
    private static final Logger log = Logger.forEnclosingClass();

    public static @Nullable Class<?> classForNameOrNull(@NotNull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignore) {
            return null;
        } catch (Throwable throwable) {
            log.warn().withCause(throwable).log("Class loading failed for name: `%s`", name);
            return null;
        }
    }

    public static boolean isInClasspath(@NotNull String name) {
        return classForNameOrNull(name) != null;
    }
}
