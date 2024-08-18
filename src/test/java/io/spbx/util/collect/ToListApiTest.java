package io.spbx.util.collect;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertCollections.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.listOf;

public class ToListApiTest {
    @ParameterizedTest @EnumSource(InputListCase.class)
    public void to_list_api(InputListCase inputListCase) {
        List<Integer> items = inputListCase.items();
        assertThat(ToListApi.of(items).toArrayList()).isEqualToExactly(new ArrayList<>(items));
        assertThat(ToListApi.of(items).toList(() -> new LinkedList<>())).isEqualToExactly(new LinkedList<>(items));
        assertThat(ToListApi.of(items).toFixedSizeList()).isEqualToExactly(items);
        assertThat(ToListApi.of(items).toNativeArray()).asList().isEqualTo(items);
        assertThat(ToListApi.of(items).toNativeArray(Integer[]::new)).asList().isEqualTo(items);
        assertThat(ToListApi.of(items).toBasicsImmutableArrayList()).isEqualToExactly(ImmutableArrayList.copyOf(items));
        assertThat(ToListApi.of(items).toBasicsMutableArray()).isEqualTo(items);
        assertThat(ToListApi.of(items).toBasicsImmutableArray()).isEqualTo(items);
        assertThat(ToListApi.of(items).toBasicsTuple()).isEqualTo(Tuple.from(items));

        if (!inputListCase.hasNulls()) {
            assertThat(ToListApi.of(items).toList()).isEqualTo(List.copyOf(items));
            assertThat(ToListApi.of(items).toGuavaImmutableList()).isEqualToExactly(ImmutableList.copyOf(items));
        } else {
            assertFailure(() -> ToListApi.of(items).toList()).throwsAssertion().hasMessageContains("toList");
            assertFailure(() -> ToListApi.of(items).toGuavaImmutableList()).throwsAssertion().hasMessageContains("Guava");
        }
    }

    public enum InputListCase {
        EMPTY,
        ONE_ELEMENT(1),
        TWO_ELEMENTS(1, 2),
        MANY_ELEMENTS(1, 2, 3),
        NULLS(null, null);

        private final List<Integer> items;

        InputListCase(@Nullable Integer ... items) {
            this.items = listOf(items);
        }

        public boolean hasNulls() {
            return this == NULLS;
        }

        public @NotNull List<Integer> items() {
            return items;
        }
    }
}
