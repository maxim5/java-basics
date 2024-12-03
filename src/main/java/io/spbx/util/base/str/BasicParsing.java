package io.spbx.util.base.str;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.func.DoubleSupplier;
import io.spbx.util.func.IntSupplier;
import io.spbx.util.func.LongSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Stateless
@Pure
@CheckReturnValue
public class BasicParsing {
    public static final int DECIMAL = 10;
    public static final int HEXADECIMAL = 16;
    public static final int OCTAL = 8;
    public static final int BINARY = 2;

    /* Integer */

    public static int parseInt(byte @NotNull[] bytes) {
        return parseInt(bytes, 10);
    }

    public static int parseInt(byte @NotNull[] bytes, int radix) {
        return parseInt(bytes, 0, bytes.length, radix);
    }

    public static int parseInt(byte @NotNull[] bytes, int fromIndex, int length, int radix) {
        return Integer.parseInt(AsciiByteArray.wrap(bytes), fromIndex, length, radix);
    }

    public static int parseInt(@NotNull CharSequence val) {
        return parseInt(val, DECIMAL);
    }

    public static int parseInt(@NotNull CharSequence val, int radix) {
        return Integer.parseInt(val, 0, val.length(), radix);
    }

    public static int parseIntSafe(@Nullable CharSequence val) {
        return parseIntSafe(val, DECIMAL, 0);
    }

    public static int parseIntSafe(@Nullable CharSequence val, int def) {
        return parseIntSafe(val, DECIMAL, def);
    }

    public static int parseIntSafe(@Nullable CharSequence val, int radix, int def) {
        return val != null ? parseIntSafe(() -> parseInt(val, radix), def)  : def;
    }

    public static int parseIntSafe(@NotNull IntSupplier parser, int def) {
        try {
            return parser.getAsInt();
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    public static @Nullable Integer parseIntegerOrNull(@Nullable CharSequence val) {
        return parseIntegerOrNull(val, DECIMAL, null);
    }

    public static @Nullable Integer parseIntegerOrNull(@Nullable CharSequence val, @Nullable Integer def) {
        return parseIntegerOrNull(val, DECIMAL, def);
    }

    public static @Nullable Integer parseIntegerOrNull(@Nullable CharSequence val, int radix, @Nullable Integer def) {
        return val != null ? catchNumberFormatException(() -> parseInt(val, radix), def) : def;
    }

    public static boolean isValidInteger(@Nullable CharSequence val) {
        return isValidInteger(val, DECIMAL);
    }

    public static boolean isValidInteger(@Nullable CharSequence val, int radix) {
        return val != null && isNumberFormatExceptionNotThrown(() -> parseInt(val, radix));
    }

    /* Long */

    public static long parseLong(byte @NotNull[] bytes) {
        return parseLong(bytes, DECIMAL);
    }

    public static long parseLong(byte @NotNull[] bytes, int radix) {
        return parseLong(bytes, 0, bytes.length, radix);
    }

    public static long parseLong(byte @NotNull[] bytes, int fromIndex, int length, int radix) {
        return Long.parseLong(AsciiByteArray.wrap(bytes), fromIndex, length, radix);
    }

    public static long parseLong(@NotNull CharSequence val) {
        return parseLong(val, DECIMAL);
    }

    public static long parseLong(@NotNull CharSequence val, int radix) {
        return Long.parseLong(val, 0, val.length(), radix);
    }

    public static long parseLongSafe(@Nullable CharSequence val) {
        return parseLongSafe(val, DECIMAL, 0);
    }

    public static long parseLongSafe(@Nullable CharSequence val, long def) {
        return parseLongSafe(val, DECIMAL, def);
    }

    public static long parseLongSafe(@Nullable CharSequence val, int radix, long def) {
        return val != null ? parseLongSafe(() -> parseLong(val, radix), def) : def;
    }

    public static long parseLongSafe(@NotNull LongSupplier parser, long def) {
        try {
            return parser.getAsLong();
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    public static @Nullable Long parseLongOrNull(@Nullable CharSequence val) {
        return parseLongOrNull(val, DECIMAL, null);
    }

    public static @Nullable Long parseLongOrNull(@Nullable CharSequence val, @Nullable Long def) {
        return parseLongOrNull(val, DECIMAL, def);
    }

    public static @Nullable Long parseLongOrNull(@Nullable CharSequence val, int radix, @Nullable Long def) {
        return val != null ? catchNumberFormatException(() -> parseLong(val, radix), def) : def;
    }

    public static boolean isValidLong(@Nullable CharSequence val) {
        return isValidLong(val, DECIMAL);
    }

    public static boolean isValidLong(@Nullable CharSequence val, int radix) {
        return val != null && isNumberFormatExceptionNotThrown(() -> Long.parseLong(val, 0, val.length(), radix));
    }

    /* Byte */

    public static byte parseByteSafe(@Nullable String val, byte def) {
        try {
            return val != null ? Byte.parseByte(val, DECIMAL) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    public static byte parseByteSafe(@Nullable String val) {
        return parseByteSafe(val, (byte) 0);
    }

    public static boolean isValidByte(@Nullable String val) {
        return isValidLong(val, DECIMAL);
    }

    public static boolean isValidByte(@Nullable String val, int radix) {
        return val != null && isNumberFormatExceptionNotThrown(() -> Byte.parseByte(val, radix));
    }

    /* Char */

    public static char parseCharSafe(@Nullable String val, char def) {
        return val != null && val.length() == 1 ? val.charAt(0) : def;
    }

    public static char parseCharSafe(@Nullable String val) {
        return parseCharSafe(val, (char) 0);
    }

    public static boolean isValidChar(@Nullable CharSequence val) {
        return val != null && val.length() == 1;
    }

    /* Boolean */

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

    public static boolean isValidBoolean(@Nullable CharSequence val) {
        return val != null && (isTrue(val) || isFalse(val));
    }

    /* Double */

    public static double parseDoubleSafe(@Nullable String val, double def) {
        try {
            return val != null ? Double.parseDouble(val) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    public static double parseDoubleSafe(@Nullable String val) {
        return parseDoubleSafe(val, 0.0);
    }

    public static boolean isValidDouble(@Nullable String val) {
        return val != null && isNumberFormatExceptionNotThrown(() -> Double.parseDouble(val));
    }

    /* Float */

    public static float parseFloatSafe(@Nullable String val, float def) {
        try {
            return val != null ? Float.parseFloat(val) : def;
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    public static float parseFloatSafe(@Nullable String val) {
        return parseFloatSafe(val, 0.0f);
    }

    public static boolean isValidFloat(@Nullable String val) {
        return val != null && isNumberFormatExceptionNotThrown(() -> Float.parseFloat(val));
    }

    /* Implementation details */

    private static <T> @Nullable T catchNumberFormatException(@NotNull Supplier<T> supplier, @Nullable T def) {
        try {
            return supplier.get();
        } catch (NumberFormatException ignore) {
            return def;
        }
    }

    private static boolean isNumberFormatExceptionNotThrown(@NotNull LongSupplier runnable) {
        try {
            runnable.get();
            return true;
        } catch (NumberFormatException ignore) {
            return false;
        }
    }

    private static boolean isNumberFormatExceptionNotThrown(@NotNull DoubleSupplier runnable) {
        try {
            runnable.get();
            return true;
        } catch (NumberFormatException ignore) {
            return false;
        }
    }
}
