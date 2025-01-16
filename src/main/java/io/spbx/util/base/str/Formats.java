package io.spbx.util.base.str;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class Formats {
    public static @NotNull String toHumanReadable(int n) {
        return toHumanReadable(n, ',');
    }

    public static @NotNull String toHumanReadable(int n, char sep) {
        return n >= 0 ?
            groupFromRight(String.valueOf(n), 3, sep) :
            groupFromRight(String.valueOf(-n), 3, sep, len -> new StringBuilder(len + 1).append('-'));
    }

    public static @NotNull String toHumanReadable(long n) {
        return toHumanReadable(n, ',');
    }

    public static @NotNull String toHumanReadable(long n, char sep) {
        return n >= 0 ?
            groupFromRight(String.valueOf(n), 3, sep) :
            groupFromRight(String.valueOf(-n), 3, sep, len -> new StringBuilder(len + 1).append('-'));
    }

    public static @NotNull String groupFromLeft(@NotNull CharSequence str, int groupSize, char sep) {
        assert groupSize > 0 : "Group size must be positive: " + groupSize;
        int length = str.length();
        int groupNum = length / groupSize;
        StringBuilder builder = new StringBuilder(length + groupNum + 1);
        for (int i = 0; i < length; i += groupSize) {
            builder.append(str, i, Math.min(i + groupSize, length)).append(sep);
        }
        return builder.substring(0, length > 0 ? builder.length() - 1 : 0);
    }

    public static @NotNull String groupFromRight(@NotNull CharSequence str, int groupSize, char sep) {
        return groupFromRight(str, groupSize, sep, StringBuilder::new);
    }

    private static @NotNull String groupFromRight(@NotNull CharSequence str, int groupSize, char sep,
                                                  @NotNull IntFunction<StringBuilder> creator) {
        assert groupSize > 0 : "Group size must be positive: " + groupSize;
        int length = str.length();
        int groupNum = length / groupSize;
        int remainder = length - groupNum * groupSize;
        StringBuilder builder = creator.apply(length + groupNum + 1);   // `creator` allows to prepend custom prefix
        if (remainder > 0) {
            builder.append(str, 0, remainder).append(sep);
        }
        for (int i = remainder; i < length; i += groupSize) {
            builder.append(str, i, Math.min(i + groupSize, length)).append(sep);
        }
        return builder.substring(0, length > 0 ? builder.length() - 1 : 0);
    }
}
