package io.spbx.util.logging;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;
import java.util.logging.Level;

// Extended Log4j-like levels for convenient logging.
public class Levels {
    // See com.google.common.flogger.backend.log4j2.Log4j2LogEventUtil#toLog4jLevel
    public static final Level OFF = Level.OFF;
    public static final Level FATAL = newLevel("FATAL", Level.SEVERE.intValue() + 2);
    public static final Level ERROR = newLevel("ERROR", Level.SEVERE.intValue() + 1);
    public static final Level WARN = Level.WARNING;
    public static final Level INFO = Level.INFO;
    public static final Level DEBUG = newLevel("DEBUG", Level.FINE.intValue() + 1);
    public static final Level TRACE = newLevel("TRACE", Level.FINER.intValue() + 1);
    public static final Level ULTRA = newLevel("ULTRA", Level.FINEST.intValue() + 1);
    public static final Level ALL = Level.ALL;

    private static @NotNull Level newLevel(@NotNull String name, int level) {
        return new LevelImpl(name, level);
    }

    static int binarySearchMinLoggableLevel(@NotNull IntPredicate isLoggable) {
        return binarySearchMinLoggableLevel(0, FATAL.intValue(), isLoggable);
    }

    static int binarySearchMinLoggableLevel(int low, int high, @NotNull IntPredicate isLoggable) {
        assert low <= high : "Invalid arguments: low=%s high=%s".formatted(low, high);
        while (high - low > 1) {
            int mid = (high + low) >> 1;
            if (isLoggable.test(mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return high == low || !isLoggable.test(low) ? high : low;
    }

    static final class LevelImpl extends Level {
        private static final String bundle = "io.spbx.util.logging";

        LevelImpl(@NotNull String name, int value) {
            super(name, value, bundle);
        }
    }
}
