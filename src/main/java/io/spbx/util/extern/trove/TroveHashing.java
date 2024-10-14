package io.spbx.util.extern.trove;

import gnu.trove.strategy.HashingStrategy;
import io.spbx.util.base.BasicStrings;
import io.spbx.util.rt.RuntimeRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 *
 * @link <a href="https://bitbucket.org/trove4j/trove/src/master/">Original Trove4J</a>
 * @link <a href="https://github.com/palantir/trove">Trove Fork by Palantir</a>
 */
public class TroveHashing {
    static {
        RuntimeRequirement.verify("gnu.trove.strategy.HashingStrategy");
    }

    public static class DefaultEquivalence<T> implements HashingStrategy<T> {
        public static <T> @NotNull HashingStrategy<T> instance() {
            return new DefaultEquivalence<>();
        }
        @Override public int computeHashCode(@NotNull T t) {
            return t.hashCode();
        }
        @Override public boolean equals(@Nullable T t1, @Nullable T t2) {
            return Objects.equals(t1, t2);
        }
    }

    public static final HashingStrategy<String> STRING_IGNORE_CASE_EQUIVALENCE = new StringIgnoreCaseEquivalence();
    public static class StringIgnoreCaseEquivalence implements HashingStrategy<String> {
        @Override public int computeHashCode(@NotNull String str) {
            return BasicStrings.hashCodeIgnoreCase(str);
        }
        @Override public boolean equals(@NotNull String s1, @NotNull String s2) {
            return s1.equalsIgnoreCase(s2);
        }
    }

    public static final HashingStrategy<String> STRING_IGNORE_CASE_NULL_EQUIVALENCE = new StringIgnoreCaseNullableEquivalence();
    public static class StringIgnoreCaseNullableEquivalence extends StringIgnoreCaseEquivalence {
        @Override public boolean equals(@Nullable String s1, @Nullable String s2) {
            return s1 == null || s2 == null ? s1 == s2 : s1.equalsIgnoreCase(s2);
        }
    }

    public static final HashingStrategy<CharSequence> CONTENT_EQUIVALENCE = new ContentEquivalence();
    public static class ContentEquivalence implements HashingStrategy<CharSequence> {
        @Override public int computeHashCode(@NotNull CharSequence sequence) {
            return BasicStrings.hashCode(sequence);
        }
        @Override public boolean equals(@NotNull CharSequence seq1, @NotNull CharSequence seq2) {
            return BasicStrings.contentEquals(seq1, seq2);
        }
    }

    public static final HashingStrategy<CharSequence> CONTENT_NULL_EQUIVALENCE = new ContentNullableEquivalence();
    public static class ContentNullableEquivalence extends ContentEquivalence {
        @Override public boolean equals(@Nullable CharSequence seq1, @Nullable CharSequence seq2) {
            return seq1 == null || seq2 == null ? seq1 == seq2 : BasicStrings.contentEquals(seq1, seq2);
        }
    }

    public static final HashingStrategy<CharSequence> CONTENT_IGNORE_CASE_EQUIVALENCE = new ContentIgnoreCaseEquivalence();
    public static class ContentIgnoreCaseEquivalence implements HashingStrategy<CharSequence> {
        @Override public int computeHashCode(@NotNull CharSequence sequence) {
            return BasicStrings.hashCodeIgnoreCase(sequence);
        }
        @Override public boolean equals(@NotNull CharSequence seq1, @NotNull CharSequence seq2) {
            return BasicStrings.contentEqualsIgnoreCase(seq1, seq2);
        }
    }

    public static final HashingStrategy<CharSequence> CONTENT_IGNORE_CASE_NULL_EQUIVALENCE = new ContentIgnoreCaseNullableEquivalence();
    public static class ContentIgnoreCaseNullableEquivalence extends ContentIgnoreCaseEquivalence {
        @Override public boolean equals(@Nullable CharSequence seq1, @Nullable CharSequence seq2) {
            return seq1 == null || seq2 == null ? seq1 == seq2 : BasicStrings.contentEqualsIgnoreCase(seq1, seq2);
        }
    }
}
