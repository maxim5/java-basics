package io.spbx.util.base;

import io.spbx.util.testing.AssertFailure;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;

public class AssertTuples {
    @CheckReturnValue
    public static <U, V> @NotNull PairSubject<U, V> assertPair(@NotNull Pair<U, V> pair) {
        return new PairSubject<>(pair);
    }

    @CheckReturnValue
    public static <U, V, W> @NotNull TripleSubject<U, V, W> assertTriple(@NotNull Triple<U, V, W> triple) {
        return new TripleSubject<>(triple);
    }

    @CheckReturnValue
    public static @NotNull TupleSubject assertTuple(@NotNull Tuple tuple) {
        return new TupleSubject(tuple);
    }

    @CheckReturnValue
    public static <U, V> @NotNull OneOfSubject<U, V> assertOneOf(@NotNull OneOf<U, V> oneOf) {
        return new OneOfSubject<>(oneOf);
    }

    public record PairSubject<U, V>(@NotNull Pair<U, V> pair) {
        public void holds(@Nullable Object first, @Nullable Object second) {
            assertThat(pair.first()).isEqualTo(first);
            assertThat(pair.getKey()).isEqualTo(first);
            assertThat(pair.second()).isEqualTo(second);
            assertThat(pair.getValue()).isEqualTo(second);

            assertThat(pair).isEqualTo(Pair.of(first, second));
            assertThat(pair.hashCode()).isEqualTo(Pair.of(first, second).hashCode());
            assertThat(pair.toString()).isEqualTo(Pair.of(first, second).toString());
            if (first == null && second == null) {
                assertThat(pair).isSameInstanceAs(Pair.empty());
            }
        }
    }

    public record TripleSubject<U, V, W>(@NotNull Triple<U, V, W> triple) {
        public void holds(@Nullable Object first, @Nullable Object second, @Nullable Object third) {
            assertThat(triple.first()).isEqualTo(first);
            assertThat(triple.second()).isEqualTo(second);
            assertThat(triple.third()).isEqualTo(third);

            assertThat(triple).isEqualTo(Triple.of(first, second, third));
            assertThat(triple.hashCode()).isEqualTo(Triple.of(first, second, third).hashCode());
            assertThat(triple.toString()).isEqualTo(Triple.of(first, second, third).toString());
            if (first == null && second == null && third == null) {
                assertThat(triple).isSameInstanceAs(Triple.empty());
            }
        }
    }

    public record TupleSubject(@NotNull Tuple tuple) {
        public void holds(@Nullable Object @NotNull ... objects) {
            assertThat(tuple.length()).isEqualTo(objects.length);
            assertThat(tuple).containsExactlyElementsIn(objects);
            assertThat(tuple.toList()).containsExactlyElementsIn(objects);
            assertThat(tuple.toArray()).asList().containsExactlyElementsIn(objects);
            assertThat(tuple.stream()).containsExactlyElementsIn(Arrays.asList(objects));
            if (objects.length > 0) {
                assertThat((Object) tuple.first()).isEqualTo(objects[0]);
                assertThat((Object) tuple.last()).isEqualTo(objects[objects.length - 1]);
                for (int i = 0; i < objects.length; i++) {
                    assertThat((Object) tuple.at(i)).isEqualTo(objects[i]);
                }
                for (int i = 1; i <= objects.length; i++) {
                    assertThat((Object) tuple.at(-i)).isEqualTo(objects[objects.length - i]);
                }
            } else {
                assertFailure(() -> tuple.first()).throwsAssertion();
                assertFailure(() -> tuple.last()).throwsAssertion();
                assertFailure(() -> tuple.at(objects.length)).throwsAssertion();
                assertFailure(() -> tuple.at(-objects.length-1)).throwsAssertion();
            }
        }
    }

    public record OneOfSubject<U, V>(@NotNull OneOf<U, V> oneOf) {
        public void holds(@Nullable Object first, @Nullable Object second) {
            assertThat(oneOf.hasFirst()).isEqualTo(first != null);
            assertThat(oneOf.hasSecond()).isEqualTo(second != null);
            assertThat(oneOf.first()).isEqualTo(first);
            assertThat(oneOf.second()).isEqualTo(second);
            assertThat(oneOf.getCase()).isEqualTo(first != null ? OneOf.Which.FIRST : OneOf.Which.SECOND);

            assertThat(oneOf).isEqualTo(OneOf.of(first, second));
            assertThat(oneOf.hashCode()).isEqualTo(OneOf.of(first, second).hashCode());
            assertThat(oneOf.toString()).isEqualTo(OneOf.of(first, second).toString());
        }

        public void holdsFirst(@NotNull Object first) {
            holds(first, null);
        }

        public void holdsSecond(@NotNull Object second) {
            holds(null, second);
        }
    }
}
