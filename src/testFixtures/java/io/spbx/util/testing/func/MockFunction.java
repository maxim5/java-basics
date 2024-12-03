package io.spbx.util.testing.func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static org.junit.jupiter.api.Assertions.fail;

public class MockFunction {
    public static <T, R> @NotNull Failing<T, R> failing() {
        return new Failing<>();
    }

    public static @SafeVarargs <T, R> @NotNull Returning<T, R> returning(@Nullable R @NotNull ... returns) {
        return new Returning<>(returns);
    }

    public static <T, R> @NotNull Applying<T, R> applying(@NotNull Function<T, R> func) {
        return new Applying<>(func);
    }

    public static class Failing<T, R> extends UniversalFunction<T, R> {
        @Override protected <A, B> A applyAny(B value) {
            return fail("Must not be called, but called with " + value);
        }
    }

    public static class Returning<T, R> extends UniversalFunction<T, R> {
        private final R[] returns;
        private int pos;

        Returning(@Nullable R @NotNull[] returns) {
            this.returns = returns;
        }

        @Override protected <A, B> A applyAny(B value) {
            assert pos < returns.length :
                "MockFunction is called too many times: %d, returns=%s".formatted(pos + 1, Arrays.toString(returns));
            return castAny(returns[pos++]);
        }

        public int timesCalled() {
            return pos;
        }
    }

    public static class Applying<T, R> extends UniversalFunction<T, R> {
        private final Function<T, R> func;
        private final List<T> args = new ArrayList<>();

        Applying(@NotNull Function<T, R> func) {
            this.func = func;
        }

        @Override protected <A, B> A applyAny(B value) {
            args.add(castAny(value));
            return castAny(func.apply(castAny(value)));
        }

        public @NotNull List<T> argsCalled() {
            return args;
        }

        public int timesCalled() {
            return args.size();
        }
    }

    private static abstract class UniversalFunction<T, R> implements
            Function<T, R>, IntFunction<R>, ToIntFunction<T>,
            LongFunction<R>, ToLongFunction<T>,
            DoubleFunction<R>, ToDoubleFunction<T>,
            Predicate<T> {
        protected abstract <A, B> A applyAny(B value);

        @Override public R apply(T value) {
            return applyAny(value);
        }
        @Override public R apply(int value) {
            return applyAny(value);
        }
        @Override public R apply(long value) {
            return applyAny(value);
        }
        @Override public int applyAsInt(T value) {
            return applyAny(value);
        }
        @Override public long applyAsLong(T value) {
            return applyAny(value);
        }
        @Override public R apply(double value) {
            return applyAny(value);
        }
        @Override public double applyAsDouble(T value) {
            return applyAny(value);
        }
        @Override public boolean test(T value) {
            return applyAny(value);
        }
    }
}
