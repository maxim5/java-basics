package io.spbx.util.collect.list;

import com.google.common.collect.Lists;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.testing.MoreTruth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.list.ImmutableArrayList.toImmutableArrayList;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class ImmutableArrayListTest {
    private static final Integer NULL = null;

    @Test
    public void arrayList_of_simple_not_null_items() {
        assertArrayList(ImmutableArrayList.of()).isEmpty();
        assertArrayList(ImmutableArrayList.of(1)).containsExactly(1);
        assertArrayList(ImmutableArrayList.of(1, 2)).containsExactly(1, 2);
        assertArrayList(ImmutableArrayList.of(1, 2, 3)).containsExactly(1, 2, 3);
        assertArrayList(ImmutableArrayList.of(1, 2, 3, 4, 5, 6)).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    public void arrayList_of_simple_null_items() {
        assertArrayList(ImmutableArrayList.of(NULL)).containsExactly(NULL);
        assertArrayList(ImmutableArrayList.of(1, NULL)).containsExactly(1, NULL);
        assertArrayList(ImmutableArrayList.of(NULL, NULL)).containsExactly(NULL, NULL);
        assertArrayList(ImmutableArrayList.of(1, 2, NULL)).containsExactly(1, 2, NULL);
        assertArrayList(ImmutableArrayList.of(1, 2, 3, NULL)).containsExactly(1, 2, 3, NULL);
    }

    @Test
    public void copyOf_simple() {
        assertArrayList(ImmutableArrayList.copyOf(arrayOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
        assertArrayList(ImmutableArrayList.copyOf(listOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
        assertArrayList(ImmutableArrayList.copyOf(iterableOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
        assertArrayList(ImmutableArrayList.copyOf(iteratorOf(1, 2, 3, 4))).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void subList_simple() {
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(0, 0)).isEmpty();
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(0, 1)).containsExactly(1);
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(1, 1)).isEmpty();
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(1, 2)).containsExactly(2);
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(0, 2)).containsExactly(1, 2);
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(2, 3)).containsExactly(3);
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(3, 3)).isEmpty();
        assertArrayList(ImmutableArrayList.of(1, 2, 3).subList(0, 3)).containsExactly(1, 2, 3);
    }

    @Test
    public void toImmutableArrayList_simple() {
        assertThat(streamOf().collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of());
        assertThat(streamOf(1).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(1));
        assertThat(streamOf(1, 2).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(1, 2));
        assertThat(streamOf(1, 2, 3).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(1, 2, 3));
    }

    @Test
    public void toImmutableArrayList_nulls() {
        assertThat(streamOf(NULL).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(NULL));
        assertThat(streamOf(1, NULL).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(1, NULL));
        assertThat(streamOf(NULL, NULL).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(NULL, NULL));
        assertThat(streamOf(NULL, 1, NULL).collect(toImmutableArrayList())).isEqualTo(ImmutableArrayList.of(NULL, 1, NULL));
    }

    @CheckReturnValue
    private static <T> @NotNull ImmutableArrayListSubject<T> assertArrayList(@NotNull ImmutableArrayList<T> list) {
        return new ImmutableArrayListSubject<>(list);
    }

    private record ImmutableArrayListSubject<T>(@NotNull ImmutableArrayList<T> list) {
        public void isEmpty() {
            containsExactly();
        }

        public final @SafeVarargs void containsExactly(@Nullable T @NotNull... expected) {
            MoreTruth.assertThat(list).isImmutable();

            assertThat(list).hasSize(expected.length);
            assertThat(list).containsExactlyElementsIn(expected).inOrder();
            assertThat(Lists.newArrayList(list.iterator())).containsExactlyElementsIn(expected).inOrder();
            assertThat(Lists.newArrayList(list.listIterator())).containsExactlyElementsIn(expected).inOrder();
            assertThat(list.isEmpty()).isEqualTo(expected.length == 0);

            List<T> expectedList = listOf(expected);
            for (int i = 0; i < expected.length; i++) {
                T item = expected[i];
                assertThat(list.get(i)).isEqualTo(item);
                assertThat(list.indexOf(item)).isEqualTo(expectedList.indexOf(item));
                assertThat(list.lastIndexOf(item)).isEqualTo(expectedList.lastIndexOf(item));
                assertThat(list.contains(item)).isTrue();
                assertThat(list.subList(i, i + 1)).containsExactly(item);
            }
            assertThat(list.subList(0, expected.length)).containsExactlyElementsIn(expected).inOrder();
        }
    }
}
