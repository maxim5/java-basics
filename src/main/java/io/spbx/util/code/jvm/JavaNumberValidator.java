package io.spbx.util.code.jvm;

import io.spbx.util.base.str.Regex;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class JavaNumberValidator {
    // https://stackoverflow.com/questions/2811031/decimal-or-numeric-values-in-regular-expression-validation
    // https://stackoverflow.com/questions/9221362/regular-expression-for-a-hexadecimal-number
    private static final Pattern UNSIGNED_DEC_INTEGER_PATTERN = Pattern.compile("0|[1-9][0-9_]*");
    private static final Pattern UNSIGNED_OCT_INTEGER_PATTERN = Pattern.compile("0[0-9_]*");
    private static final Pattern UNSIGNED_DEC_OCT_INTEGER_PATTERN = Pattern.compile("[0-9][0-9_]*");
    private static final Pattern UNSIGNED_HEX_INTEGER_PATTERN = Pattern.compile("0x[0-9a-f][0-9a-f_]+", Pattern.CASE_INSENSITIVE);

    private static final Pattern UNSIGNED_DEC_LONG_PATTERN = Pattern.compile("0|[1-9][0-9_]*l?", Pattern.CASE_INSENSITIVE);
    private static final Pattern UNSIGNED_OCT_LONG_PATTERN = Pattern.compile("0[0-9_]*l?", Pattern.CASE_INSENSITIVE);
    private static final Pattern UNSIGNED_DEC_OCT_LONG_PATTERN = Pattern.compile("[0-9][0-9_]*l?", Pattern.CASE_INSENSITIVE);
    private static final Pattern UNSIGNED_HEX_LONG_PATTERN = Pattern.compile("0x[0-9a-f][0-9a-f_]+l?", Pattern.CASE_INSENSITIVE);

    public static boolean isValidJavaUnsignedIntegerLiteral(@NotNull String num) {
        return Regex.on(num).matches(UNSIGNED_DEC_OCT_INTEGER_PATTERN) ||
               Regex.on(num).matches(UNSIGNED_HEX_INTEGER_PATTERN);
    }

    public static boolean isValidJavaUnsignedLongLiteral(@NotNull String num) {
        return Regex.on(num).matches(UNSIGNED_DEC_OCT_LONG_PATTERN) ||
               Regex.on(num).matches(UNSIGNED_HEX_LONG_PATTERN);
    }

    public static boolean isValidJavaUnsignedIntLiteral(@NotNull String num) {
        return isValidJavaUnsignedIntegerLiteral(num) || isValidJavaUnsignedLongLiteral(num);
    }
}
