package io.spbx.util.collect;

import io.spbx.util.base.Maybe;
import io.spbx.util.collect.IntSize.DistributedIntSize;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class IntSizeTest {
    @ParameterizedTest @EnumSource(Scenario.class)
    public void zero_size(Scenario scenario) {
        IntSizeImpl sizable = new IntSizeImpl(scenario, 0);

        assertThat(sizable.size()).isEqualTo(0);
        assertThat(sizable.isEmpty()).isTrue();
        assertThat(sizable.isNotEmpty()).isFalse();

        assertThat(sizable.size(QueryOption.FORCE_EXACT)).isEqualTo(0);
        assertThat(sizable.isEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.TRUE);
        assertThat(sizable.isNotEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.FALSE);

        assertThat(sizable.size(QueryOption.ONLY_IF_CACHED)).isAnyOf(0, -1);
        assertThat(sizable.isEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.TRUE, Maybe.UNKNOWN);
        assertThat(sizable.isNotEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.FALSE, Maybe.UNKNOWN);
    }

    @ParameterizedTest @EnumSource(Scenario.class)
    public void int32_size(Scenario scenario) {
        int size = Integer.MAX_VALUE;
        IntSizeImpl sizable = new IntSizeImpl(scenario, size);

        assertThat(sizable.size()).isEqualTo(size);
        assertThat(sizable.isEmpty()).isFalse();
        assertThat(sizable.isNotEmpty()).isTrue();

        assertThat(sizable.size(QueryOption.FORCE_EXACT)).isEqualTo(size);
        assertThat(sizable.isEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.FALSE);
        assertThat(sizable.isNotEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.TRUE);

        assertThat(sizable.size(QueryOption.ONLY_IF_CACHED)).isAnyOf(size, -1);
        assertThat(sizable.isEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.FALSE, Maybe.UNKNOWN);
        assertThat(sizable.isNotEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.TRUE, Maybe.UNKNOWN);
    }

    private record IntSizeImpl(@NotNull Scenario scenario, int val) implements DistributedIntSize {
        @Override public int size(@NotNull QueryOption option) {
            return switch (option) {
                case ONLY_IF_CACHED -> switch (scenario) {
                    case INT_ALL_IN_MEMORY -> val;
                    case INT_FETCHING -> -1;
                };
                case FORCE_EXACT -> val;
            };
        }
    }

    private enum Scenario {
        INT_ALL_IN_MEMORY,
        INT_FETCHING,
    }
}
