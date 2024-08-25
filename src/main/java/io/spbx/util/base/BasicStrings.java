package io.spbx.util.base;

import io.spbx.util.array.CharArray;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class BasicStrings {
    @Pure
    public static @NotNull String removePrefix(@NotNull String big, @NotNull String small) {
        if (big.startsWith(small)) {
            return big.substring(small.length());
        }
        return big;
    }

    @Pure
    public static @NotNull String removeSuffix(@NotNull String big, @NotNull String small) {
        if (big.endsWith(small)) {
            return big.substring(0, big.length() - small.length());
        }
        return big;
    }

    @Pure
    public static @NotNull String ensurePrefix(@NotNull String big, @NotNull String small) {
        if (big.startsWith(small)) {
            return big;
        }
        return small + big;
    }

    @Pure
    public static @NotNull String ensureSuffix(@NotNull String big, @NotNull String small) {
        if (big.endsWith(small)) {
            return big;
        }
        return big + small;
    }

    @Pure
    public static @NotNull String nonNull(@Nullable String str) {
        return str == null ? "" : str;
    }

    @Pure
    public static @NotNull CharArray nonNull(@Nullable CharArray str) {
        return str == null ? CharArray.EMPTY : str;
    }

    @Pure
    public static @NotNull CharSequence nonNull(@Nullable CharSequence str) {
        return str == null ? "" : str;
    }

    @Pure
    public static @NotNull String toStringOrEmpty(@Nullable Object obj) {
        return obj == null ? "" : obj.toString();
    }

    @Pure
    public static <T> @NotNull String toStringOrEmpty(@Nullable T obj, @NotNull Function<T, String> toString) {
        return obj == null ? "" : toString.apply(obj);
    }

    @Pure
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.isEmpty();
    }

    @Pure
    public static boolean isNotEmpty(@Nullable CharSequence str) {
        return !isEmpty(str);
    }

    @Pure
    public static @NotNull String firstNotEmpty(@Nullable String first, @Nullable String second) {
        if (isEmpty(first)) {
            assert !isEmpty(second) : "Both input strings are null or empty";
            return second;
        }
        return first;
    }

    @Pure
    public static @NotNull Optional<String> ofNonEmpty(@Nullable String str) {
        return isEmpty(str) ? Optional.empty() : Optional.of(str);
    }

    @Pure
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

    // Based on the shift-add-xor class of string hashing functions
    // cf. Ramakrishna and Zobel,
    //     "Performance in Practice of String Hashing Functions"
    //
    // Values left=5, right=2 work well for ASCII inputs.
    private static final int HASH_SEED = 31;
    private static final int HASH_LEFT = 5;
    private static final int HASH_RIGHT = 2;

    public static int hashCode(char[] chars, int seed, int l, int r) {
        int hash = seed;
        for (char ch : chars) {
            hash = hash ^ ((hash << l) + (hash >> r) + ch);
        }
        return hash;
    }

    public static int hashCode(char[] chars) {
        return hashCode(chars, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static int hashCode(@NotNull CharSequence str, @NotNull CharAtFunction<CharSequence> charAt, int seed, int l, int r) {
        int hash = seed;
        for (int i = 0, len = str.length(); i < len; i++) {
            hash = hash ^ ((hash << l) + (hash >> r) + charAt.charAt(str, i));
        }
        return hash;
    }

    public static int hashCode(@NotNull CharSequence str) {
        return hashCode(str, CharSequence::charAt, HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public static int hashCodeIgnoreCase(@NotNull CharSequence str) {
        return hashCode(str, (seq, index) -> Character.toLowerCase(seq.charAt(index)), HASH_SEED, HASH_LEFT, HASH_RIGHT);
    }

    public interface CharAtFunction<T> {
        int charAt(@NotNull T instance, int index);
    }
}
