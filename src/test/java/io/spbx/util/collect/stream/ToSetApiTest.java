package io.spbx.util.collect.stream;

import com.google.common.collect.ImmutableSet;
import io.spbx.util.collect.set.ImmutableLinkedHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

import static io.spbx.util.testing.AssertCollections.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.setOf;

@Tag("fast")
public class ToSetApiTest {
    @ParameterizedTest @EnumSource(InputSetCase.class)
    public void to_set_api(InputSetCase inputSetCase) {
        Set<Integer> items = inputSetCase.items();
        assertThat(ToSetApi.of(items).toSet()).isEqualToExactly(new HashSet<>(items));
        assertThat(ToSetApi.of(items).toHashSet()).isEqualToExactly(new HashSet<>(items));
        assertThat(ToSetApi.of(items).toLinkedHashSet()).isEqualToExactly(new LinkedHashSet<>(items));
        assertThat(ToSetApi.of(items).toSet(CopyOnWriteArraySet::new)).isEqualToExactly(new CopyOnWriteArraySet<>(items));
        assertThat(ToSetApi.of(items).toBasicsImmutableLinkedHashSet()).isEqualToExactly(ImmutableLinkedHashSet.copyOf(items));

        if (!inputSetCase.hasNulls()) {
            assertThat(ToSetApi.of(items).toTreeSet()).isEqualToExactly(new TreeSet<>(items));
            assertThat(ToSetApi.of(items).toGuavaImmutableSet()).isEqualToExactly(ImmutableSet.copyOf(items));
        } else {
            assertFailure(() -> ToSetApi.of(items).toTreeSet()).throwsAssertion().hasMessageContains("toTreeSet");
            assertFailure(() -> ToSetApi.of(items).toGuavaImmutableSet()).throwsAssertion().hasMessageContains("Guava");
        }
    }

    public enum InputSetCase {
        EMPTY,
        ONE_ELEMENT(1),
        TWO_ELEMENTS(1, 2),
        MANY_ELEMENTS(1, 2, 3),
        NULLS(null, null);

        private final Set<Integer> items;

        InputSetCase(@Nullable Integer... items) {
            this.items = setOf(items);
        }

        public boolean hasNulls() {
            return this == NULLS;
        }

        public @NotNull Set<Integer> items() {
            return items;
        }
    }
}
