package io.spbx.util.base.str;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.func.CharPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@Stateless
@Pure
@CheckReturnValue
public class BasicStrings {
    /* Prefix and suffix */

    public static boolean startsWith(@NotNull String big, char small) {
        return !big.isEmpty() && big.charAt(0) == small;
    }

    public static boolean endsWith(@NotNull String big, char small) {
        return !big.isEmpty() && big.charAt(big.length() - 1) == small;
    }

    public static @NotNull String removePrefix(@NotNull String big, @NotNull String small) {
        if (big.startsWith(small)) {
            return big.substring(small.length());
        }
        return big;
    }

    public static @NotNull String removePrefix(@NotNull String big, char small) {
        if (startsWith(big, small)) {
            return big.substring(1);
        }
        return big;
    }

    public static @NotNull String removeSuffix(@NotNull String big, @NotNull String small) {
        if (big.endsWith(small)) {
            return big.substring(0, big.length() - small.length());
        }
        return big;
    }

    public static @NotNull String removeSuffix(@NotNull String big, char small) {
        if (endsWith(big, small)) {
            return big.substring(0, big.length() - 1);
        }
        return big;
    }

    public static @NotNull String ensurePrefix(@NotNull String big, @NotNull String small) {
        if (big.startsWith(small)) {
            return big;
        }
        return small + big;
    }

    public static @NotNull String ensurePrefix(@NotNull String big, char small) {
        if (startsWith(big, small)) {
            return big;
        }
        return small + big;
    }

    public static @NotNull String ensureSuffix(@NotNull String big, @NotNull String small) {
        if (big.endsWith(small)) {
            return big;
        }
        return big + small;
    }

    public static @NotNull String ensureSuffix(@NotNull String big, char small) {
        if (endsWith(big, small)) {
            return big;
        }
        return big + small;
    }

    public static @NotNull String stripLeft(@NotNull String str, @NotNull CharPredicate predicate) {
        for (int i = 0, len = str.length(); i < len; i++) {
            if (!predicate.test(str.charAt(i))) {
                return str.substring(i);
            }
        }
        return "";
    }

    public static @NotNull String stripRight(@NotNull String str, @NotNull CharPredicate predicate) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (!predicate.test(str.charAt(i))) {
                return str.substring(0, i + 1);
            }
        }
        return "";
    }

    public static @NotNull String strip(@NotNull String str, @NotNull CharPredicate predicate) {
        return stripLeft(stripRight(str, predicate), predicate);
    }

    /* Null or empty */

    public static @NotNull String nonNull(@Nullable String str) {
        return str == null ? "" : str;
    }

    public static @NotNull CharArray nonNull(@Nullable CharArray str) {
        return str == null ? CharArray.EMPTY : str;
    }

    public static @NotNull CharSequence nonNull(@Nullable CharSequence str) {
        return str == null ? "" : str;
    }

    public static @NotNull String toStringOrEmpty(@Nullable Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static <T> @NotNull String toStringOrEmpty(@Nullable T obj, @NotNull Function<T, String> toString) {
        return obj == null ? "" : toString.apply(obj);
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(@Nullable CharSequence str) {
        return !isEmpty(str);
    }

    public static @NotNull String firstNotEmpty(@Nullable String first, @Nullable String second) {
        if (isEmpty(first)) {
            assert !isEmpty(second) : "Both input strings are null or empty";
            return second;
        }
        return first;
    }

    public static @NotNull Optional<String> ofNonEmpty(@Nullable String str) {
        return isEmpty(str) ? Optional.empty() : Optional.of(str);
    }

    /* Char sequence */

    public static @Nullable CharSequence intern(@Nullable CharSequence sequence) {
        return sequence instanceof String str ? str.intern() : sequence;
    }

    /* Equals */

    public static boolean contentEquals(@NotNull CharSequence seq1, @NotNull CharSequence seq2) {
        if (seq1 instanceof String str)
            return str.contentEquals(seq2);
        if (seq2 instanceof String str)
            return str.contentEquals(seq1);

        if (seq1 == seq2)
            return true;
        if (seq1.length() != seq2.length())
            return false;
        for (int i = 0, len = seq1.length(); i < len; i++) {
            if (seq1.charAt(i) != seq2.charAt(i))
                return false;
        }
        return true;
    }

    public static boolean contentEqualsIgnoreCase(@NotNull CharSequence str1, @NotNull CharSequence str2) {
        if (str1 == str2)
            return true;
        if (str1.length() != str2.length())
            return false;
        for (int i = 0, len = str1.length(); i < len; ++i) {
            if (Character.toLowerCase(str1.charAt(i)) != Character.toLowerCase(str2.charAt(i)))
                return false;
        }
        return true;
    }

    /* Hash code */

    public static int hashCode(@NotNull CharSequence str) {
        return BaseCharBuf.hashCode(str, str.length(), CharSequence::charAt);
    }

    public static int hashCodeIgnoreCase(@NotNull CharSequence str) {
        return BaseCharBuf.hashCode(str, str.length(), (seq, index) -> Character.toLowerCase(seq.charAt(index)));
    }
}
