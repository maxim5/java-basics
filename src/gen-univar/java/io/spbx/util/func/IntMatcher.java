package io.spbx.util.func;

import com.carrotsearch.hppc.IntLookupContainer;
import com.carrotsearch.hppc.IntHashSet;
import io.spbx.util.base.tuple.IntRange;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.stream.IntStream;

@FunctionalInterface
@Generated(value = "$Type$Matcher.java", date = "2025-01-14T10:07:33.470121100Z")
public interface IntMatcher extends IntPredicate, Allowed<Integer> {
    static @NotNull IntMatcher all() {
        return val -> true;
    }

    static @NotNull IntMatcher none() {
        return val -> false;
    }

    static @NotNull IntMatcher of() {
        return none();
    }

    static @NotNull IntMatcher of(int value) {
        return val -> val == value;
    }

    static @NotNull IntMatcher of(int value1, int value2) {
        return value1 == value2 ? of(value1) : val -> val == value1 || val == value2;
    }

    static @NotNull IntMatcher of(int... values) {
        return values.length == 0 ? of() :
            values.length == 1 ? of(values[0]) :
            values.length == 2 ? of(values[0], values[1]) :
            of(IntHashSet.from(values));
    }

    static @NotNull IntMatcher of(@NotNull IntLookupContainer container) {
        return container::contains;
    }

    static @NotNull IntMatcher of(@NotNull IntStream stream) {
        return of(stream.toArray());
    }

    static @NotNull IntMatcher wrap(@NotNull java.util.function.IntPredicate predicate) {
        return predicate instanceof IntMatcher matcher ? matcher : predicate::test;
    }

    static @NotNull IntMatcher rangeOf(@NotNull IntRange range) {
        return range::contains;
    }

    IntMatcher ASCII_LETTERS = v -> ('a' <= v && v <= 'z') || ('A' <= v && v <= 'Z');
    IntMatcher ASCII_UPPERCASE = v -> 'A' <= v && v <= 'Z';
    IntMatcher ASCII_LOWERCASE = v -> 'a' <= v && v <= 'z';
    IntMatcher DIGITS = v -> '0' <= v && v <= '9';
    IntMatcher HEX = v -> ('0' <= v && v <= '9') || ('a' <= v && v <= 'f') || ('A' <= v && v <= 'F');
    IntMatcher ALPHA_NUM = v -> ('0' <= v && v <= '9') || ('a' <= v && v <= 'z') || ('A' <= v && v <= 'Z') || v == '_';
    IntMatcher BASE64_URL = v -> ('0' <= v && v <= '9') || ('a' <= v && v <= 'z') || ('A' <= v && v <= 'Z') || v == '_' || v == '-';

    static @NotNull IntMatcher charsOf(@NotNull CharSequence str) {
        return of(str.chars());
    }

    public static @NotNull IntMatcher and(@NotNull IntMatcher m1, @NotNull IntMatcher m2) {
        return val -> m1.test(val) && m2.test(val);
    }

    public static @NotNull IntMatcher or(@NotNull IntMatcher m1, @NotNull IntMatcher m2) {
        return val -> m1.test(val) || m2.test(val);
    }
}
