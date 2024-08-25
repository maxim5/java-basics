package io.spbx.util.base;

import io.spbx.util.array.CharArray;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BasicParsing {
    @Pure
    public static int parseIntSafe(@Nullable CharSequence val, int def) {
        try {
            return val != null ? Integer.parseInt(val, 0, val.length(), 10) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    @Pure
    public static int parseIntSafe(@Nullable CharSequence val) {
        return parseIntSafe(val, 0);
    }

    @Pure
    public static long parseLongSafe(@Nullable CharSequence val, long def) {
        try {
            return val != null ? Long.parseLong(val, 0, val.length(), 10) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    @Pure
    public static long parseLongSafe(@Nullable CharSequence val) {
        return parseLongSafe(val, 0);
    }

    @Pure
    public static byte parseByteSafe(@Nullable String val, byte def) {
        try {
            return val != null ? Byte.parseByte(val, 10) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    @Pure
    public static byte parseByteSafe(@Nullable String val) {
        return parseByteSafe(val, (byte) 0);
    }

    public static boolean parseBoolSafe(@Nullable String val, boolean def) {
        if (val != null) {
            if ("true".equalsIgnoreCase(val)) {
                return true;
            }
            if ("false".equalsIgnoreCase(val)) {
                return false;
            }
        }
        return def;
    }

    public static boolean parseBoolSafe(@Nullable String val) {
        return "true".equalsIgnoreCase(val);
    }

    private static final CharArray TRUE = CharArray.of("true");
    private static final CharArray FALSE = CharArray.of("false");

    public static boolean parseBoolSafe(@Nullable CharSequence val, boolean def) {
        if (val != null) {
            if (isTrue(val)) {
                return true;
            }
            if (isFalse(val)) {
                return false;
            }
        }
        return def;
    }

    public static boolean parseBoolSafe(@Nullable CharSequence val) {
        return val != null && isTrue(val);
    }

    public static boolean isTrue(@NotNull CharSequence val) {
        return TRUE.contentEqualsIgnoreCase(val);
    }

    public static boolean isFalse(@NotNull CharSequence val) {
        return FALSE.contentEqualsIgnoreCase(val);
    }

    @Pure
    public static double parseDoubleSafe(@Nullable String val, double def) {
        try {
            return val != null ? Double.parseDouble(val) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    @Pure
    public static double parseDoubleSafe(@Nullable String val) {
        return parseDoubleSafe(val, 0.0);
    }

    @Pure
    public static float parseFloatSafe(@Nullable String val, float def) {
        try {
            return val != null ? Float.parseFloat(val) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    @Pure
    public static float parseFloatSafe(@Nullable String val) {
        return parseFloatSafe(val, 0.0f);
    }
}
