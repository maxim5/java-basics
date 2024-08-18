package io.spbx.util.func;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class Chains {
    /* Functions */

    public static <T, R> @NotNull Function<T, R> constant(R value) {
        return t -> value;
    }

    public static <A, B, C> @NotNull Function<A, C> chain(@NotNull Function<A, B> f1, @NotNull Function<B, C> f2) {
        return a -> f2.apply(f1.apply(a));
    }

    public static <T, R> @NotNull Function<T, R> chain(@NotNull Predicate<T> p, @NotNull Function<Boolean, R> f) {
        return t -> f.apply(p.test(t));
    }

    public static <T, R> @NotNull Function<T, R> nonNullify(@NotNull Function<T, R> f, @NotNull R def) {
        return t -> t != null ? f.apply(t) : def;
    }

    public static <T, R> @NotNull Function<T, R> bypassNull(@NotNull Function<T, R> f) {
        return t -> t != null ? f.apply(t) : null;
    }

    /* Consumers */

    public static <T> @NotNull Consumer<T> chain(@NotNull Predicate<T> p, @NotNull Consumer<T> c) {
        return t -> {
            if (p.test(t))
                c.accept(t);
        };
    }

    public static <T> @NotNull Consumer<T> fanOut(@NotNull Consumer<T> first, @NotNull Consumer<T> second) {
        return first.andThen(second);
    }

    public static <T> @NotNull Consumer<T> fanOut(@NotNull Consumer<T> first,
                                                  @NotNull Consumer<T> second,
                                                  @NotNull Consumer<T> third) {
        return first.andThen(second).andThen(third);
    }

    /* Predicates */

    public static class Predicates {
        public static <T> @NotNull Predicate<T> constant(boolean value) {
            return t -> value;
        }

        public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> p1, @NotNull Predicate<T> p2) {
            return p1.and(p2);
        }

        public static <T> @NotNull Predicate<T> and(@NotNull Predicate<T> p1, boolean value) {
            return p1.and(constant(value));
        }

        public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> p1, @NotNull Predicate<T> p2) {
            return p1.or(p2);
        }

        public static <T> @NotNull Predicate<T> or(@NotNull Predicate<T> p1, boolean value) {
            return p1.or(constant(value));
        }

        public static <T> @NotNull Predicate<T> chain(@NotNull Consumer<T> c, @NotNull Predicate<T> p) {
            return t -> {
                c.accept(t);
                return p.test(t);
            };
        }
    }

    /* Int functions and consumers */

    public static class Ints {
        public static <R> @NotNull IntFunction<R> constant(R value) {
            return t -> value;
        }

        public static <T, R> @NotNull IntFunction<R> chain(@NotNull IntFunction<T> f1, @NotNull Function<T, R> f2) {
            return i -> f2.apply(f1.apply(i));
        }
    }

    /* Int consumers */

    public static class IntConsumers {
        public static @NotNull IntConsumer chain(@NotNull IntPredicate p, @NotNull IntConsumer c) {
            return t -> {
                if (p.test(t))
                    c.accept(t);
            };
        }
    }

    /* Int predicates */

    public static class IntPredicates {
        public static final IntPredicate TRUE = t -> true;
        public static final IntPredicate FALSE = t -> false;

        public static @NotNull IntPredicate constant(boolean value) {
            return t -> value;
        }

        public static @NotNull IntPredicate chain(@NotNull IntConsumer c, @NotNull IntPredicate p) {
            return t -> {
                c.accept(t);
                return p.test(t);
            };
        }
    }

    /* Long functions and consumers */

    public static class Longs {
        public static <R> @NotNull LongFunction<R> constant(R value) {
            return t -> value;
        }

        public static <T, R> @NotNull LongFunction<R> chain(@NotNull LongFunction<T> f1, @NotNull Function<T, R> f2) {
            return i -> f2.apply(f1.apply(i));
        }
    }

    /* Long consumers */

    public static class LongConsumers {
        public static @NotNull LongConsumer chain(@NotNull LongPredicate p, @NotNull LongConsumer c) {
            return t -> {
                if (p.test(t))
                    c.accept(t);
            };
        }
    }

    /* Long predicates */

    public static class LongPredicates {
        public static final LongPredicate TRUE = t -> true;
        public static final LongPredicate FALSE = t -> false;

        public static @NotNull LongPredicate constant(boolean value) {
            return t -> value;
        }

        public static @NotNull LongPredicate chain(@NotNull LongConsumer c, @NotNull LongPredicate p) {
            return t -> {
                c.accept(t);
                return p.test(t);
            };
        }
    }
}
