package io.spbx.util.collect.iter;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.tuple.IntObjPair;
import io.spbx.util.testing.MoreTruth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class EnumeratorTest {
    private static final @Nullable String NULL = null;

    @Test
    public void enumerator_wrap_simple_non_empty() {
        assertEnum(Enumerator.wrap(iteratorOf("foo", "bar", null, "baz")))
            .nextEqualsTo(0, "foo")
            .nextEqualsTo(1, "bar")
            .nextEqualsTo(2, null)
            .nextEqualsTo(3, "baz")
            .hasNoNext();
    }

    @Test
    public void enumerator_of_iterable() {
        assertEnum(Enumerator.<String>of(iterableOf())).hasNoNext();
        assertEnum(Enumerator.of(iterableOf("foo"))).nextEqualsTo(0, "foo").hasNoNext();
        assertEnum(Enumerator.of(iterableOf(NULL))).nextEqualsTo(0, NULL).hasNoNext();
    }

    @Test
    public void enumerator_of_list() {
        assertEnum(Enumerator.<String>of(listOf())).hasNoNext();
        assertEnum(Enumerator.of(listOf("foo"))).nextEqualsTo(0, "foo").hasNoNext();
        assertEnum(Enumerator.of(listOf(NULL))).nextEqualsTo(0, NULL).hasNoNext();
    }

    @Test
    public void enumerator_enumerate_iterator() {
        MoreTruth.assertThat(Enumerator.enumerate(iteratorOf())).isEmpty();
        MoreTruth.assertThat(Enumerator.enumerate(iteratorOf("foo"))).containsExactly(pairOf(0, "foo"));
        MoreTruth.assertThat(Enumerator.enumerate(iteratorOf(NULL))).containsExactly(pairOf(0, NULL));
        MoreTruth.assertThat(Enumerator.enumerate(iteratorOf("foo", "bar", null, "baz")))
            .containsExactly(pairOf(0, "foo"), pairOf(1, "bar"), pairOf(2, null), pairOf(3, "baz"));
    }

    @Test
    public void enumerator_enumerate_iterable() {
        assertThat(Enumerator.enumerate(iterableOf())).isEmpty();
        assertThat(Enumerator.enumerate(iterableOf("foo"))).containsExactly(pairOf(0, "foo"));
        assertThat(Enumerator.enumerate(iterableOf(NULL))).containsExactly(pairOf(0, NULL));
        assertThat(Enumerator.enumerate(iterableOf("foo", "bar", null, "baz")))
            .containsExactly(pairOf(0, "foo"), pairOf(1, "bar"), pairOf(2, null), pairOf(3, "baz"));
    }

    @Test
    public void enumerator_enumerate_array() {
        assertThat(Enumerator.enumerate(arrayOf())).isEmpty();
        assertThat(Enumerator.enumerate(arrayOf("foo"))).containsExactly(pairOf(0, "foo"));
        assertThat(Enumerator.enumerate(arrayOf(NULL))).containsExactly(pairOf(0, NULL));
        assertThat(Enumerator.enumerate(arrayOf("foo", "bar", null, "baz")))
            .containsExactly(pairOf(0, "foo"), pairOf(1, "bar"), pairOf(2, null), pairOf(3, "baz"));
    }

    @CheckReturnValue
    private static <T> @NotNull EnumeratorSubject<T> assertEnum(@NotNull Enumerator<T> enumerator) {
        return new EnumeratorSubject<>(enumerator);
    }

    @CanIgnoreReturnValue
    private record EnumeratorSubject<T>(@NotNull Enumerator<T> enumerator) {
        public @NotNull EnumeratorTest.EnumeratorSubject<T> hasNoNext() {
            assertThat(enumerator.hasNext()).isFalse();
            return this;
        }

        public @NotNull EnumeratorTest.EnumeratorSubject<T> nextEqualsTo(int index, @Nullable T expected) {
            assertThat(enumerator.hasNext()).isTrue();
            assertThat(enumerator.nextIndex()).isEqualTo(index);
            assertThat(enumerator.next()).isEqualTo(expected);
            assertThat(enumerator.currentIndex()).isEqualTo(index);
            return this;
        }
    }

    private static @NotNull IntObjPair<String> pairOf(int first, @Nullable String second) {
        return IntObjPair.of(first, second);
    }
}
