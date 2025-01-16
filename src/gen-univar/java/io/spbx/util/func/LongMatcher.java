package io.spbx.util.func;

import com.carrotsearch.hppc.LongLookupContainer;
import com.carrotsearch.hppc.LongHashSet;
import io.spbx.util.base.tuple.LongRange;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.stream.LongStream;

@FunctionalInterface
@Generated(value = "$Type$Matcher.java", date = "2025-01-14T10:07:33.470121100Z")
public interface LongMatcher extends LongPredicate, Allowed<Long> {
    static @NotNull LongMatcher all() {
        return val -> true;
    }

    static @NotNull LongMatcher none() {
        return val -> false;
    }

    static @NotNull LongMatcher of() {
        return none();
    }

    static @NotNull LongMatcher of(long value) {
        return val -> val == value;
    }

    static @NotNull LongMatcher of(long value1, long value2) {
        return value1 == value2 ? of(value1) : val -> val == value1 || val == value2;
    }

    static @NotNull LongMatcher of(long... values) {
        return values.length == 0 ? of() :
            values.length == 1 ? of(values[0]) :
            values.length == 2 ? of(values[0], values[1]) :
            of(LongHashSet.from(values));
    }

    static @NotNull LongMatcher of(@NotNull LongLookupContainer container) {
        return container::contains;
    }

    static @NotNull LongMatcher of(@NotNull LongStream stream) {
        return of(stream.toArray());
    }

    static @NotNull LongMatcher wrap(@NotNull java.util.function.LongPredicate predicate) {
        return predicate instanceof LongMatcher matcher ? matcher : predicate::test;
    }

    static @NotNull LongMatcher rangeOf(@NotNull LongRange range) {
        return range::contains;
    }

    public static @NotNull LongMatcher and(@NotNull LongMatcher m1, @NotNull LongMatcher m2) {
        return val -> m1.test(val) && m2.test(val);
    }

    public static @NotNull LongMatcher or(@NotNull LongMatcher m1, @NotNull LongMatcher m2) {
        return val -> m1.test(val) || m2.test(val);
    }
}
