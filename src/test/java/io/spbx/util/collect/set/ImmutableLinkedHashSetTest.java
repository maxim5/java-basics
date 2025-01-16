package io.spbx.util.collect.set;

import com.google.common.collect.Lists;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.testing.MoreTruth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.set.ImmutableLinkedHashSet.toImmutableLinkedHashSet;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class ImmutableLinkedHashSetTest {
    private static final Integer NULL = null;

    @Test
    public void set_of_simple_not_null_items() {
        assertSet(ImmutableLinkedHashSet.of()).isEmpty();
        assertSet(ImmutableLinkedHashSet.of(1)).containsExactly(1);
        assertSet(ImmutableLinkedHashSet.of(1, 2)).containsExactly(1, 2);
        assertSet(ImmutableLinkedHashSet.of(1, 2, 3)).containsExactly(1, 2, 3);
        assertSet(ImmutableLinkedHashSet.of(1, 2, 3, 4, 5)).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void set_of_simple_null_items() {
        assertSet(ImmutableLinkedHashSet.of(NULL)).containsExactly(NULL);
        assertSet(ImmutableLinkedHashSet.of(1, NULL)).containsExactly(1, null);
        assertSet(ImmutableLinkedHashSet.of(NULL, null)).containsExactly(NULL);
        assertSet(ImmutableLinkedHashSet.of(1, 2, NULL)).containsExactly(1, 2, NULL);
        assertSet(ImmutableLinkedHashSet.of(1, 2, 3, NULL)).containsExactly(1, 2, 3, NULL);
    }

    @Test
    public void copyOf_simple() {
        assertSet(ImmutableLinkedHashSet.copyOf(arrayOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
        assertSet(ImmutableLinkedHashSet.copyOf(listOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
        assertSet(ImmutableLinkedHashSet.copyOf(iterableOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
        assertSet(ImmutableLinkedHashSet.copyOf(iteratorOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void toImmutableLinkedHashSet_simple() {
        assertThat(streamOf().collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of());
        assertThat(streamOf(1).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(1));
        assertThat(streamOf(1, 2).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(1, 2));
        assertThat(streamOf(1, 2, 2).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(1, 2));
        assertThat(streamOf(1, 2, 3).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(1, 2, 3));
    }

    @Test
    public void toImmutableLinkedHashSet_nulls() {
        assertThat(streamOf(NULL).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(NULL));
        assertThat(streamOf(1, NULL).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(1, NULL));
        assertThat(streamOf(NULL, NULL).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(NULL));
        assertThat(streamOf(NULL, 1, NULL).collect(toImmutableLinkedHashSet())).isEqualTo(ImmutableLinkedHashSet.of(NULL, 1));
    }

    @CheckReturnValue
    private static <T> @NotNull ImmutableLinkedHashSetSubject<T> assertSet(@NotNull ImmutableLinkedHashSet<T> set) {
        return new ImmutableLinkedHashSetSubject<>(set);
    }

    private record ImmutableLinkedHashSetSubject<T>(@NotNull ImmutableLinkedHashSet<T> set) {
        public void isEmpty() {
            containsExactly();
        }

        public final @SafeVarargs void containsExactly(@Nullable T @NotNull... expected) {
            MoreTruth.assertThat(set).isImmutable();

            assertThat(set).hasSize(expected.length);
            assertThat(set).containsExactlyElementsIn(expected).inOrder();
            assertThat(Lists.newArrayList(set.iterator())).containsExactlyElementsIn(expected).inOrder();
            assertThat(set.isEmpty()).isEqualTo(expected.length == 0);
            for (T item : expected) {
                assertThat(set.contains(item)).isTrue();
            }
        }
    }
}
