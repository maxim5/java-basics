package io.spbx.util.collect;

import io.spbx.util.base.Maybe;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class LongSizeTest {
    @ParameterizedTest @EnumSource(Scenario.class)
    public void zero_size(Scenario scenario) {
        LongImpl sizable = new LongImpl(scenario, 0);

        assertThat(sizable.size()).isEqualTo(0);
        assertThat(sizable.exactIntSize()).isEqualTo(0);
        assertThat(sizable.saturatedIntSize()).isEqualTo(0);
        assertThat(sizable.isEmpty()).isTrue();
        assertThat(sizable.isNotEmpty()).isFalse();

        assertThat(sizable.size(QueryOption.FORCE_EXACT)).isEqualTo(0);
        assertThat(sizable.exactIntSize(QueryOption.FORCE_EXACT)).isEqualTo(0);
        assertThat(sizable.saturatedIntSize(QueryOption.FORCE_EXACT)).isEqualTo(0);
        assertThat(sizable.isEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.TRUE);
        assertThat(sizable.isNotEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.FALSE);

        assertThat(sizable.size(QueryOption.ONLY_IF_CACHED)).isAnyOf(0L, -1L);
        assertThat(sizable.exactIntSize(QueryOption.ONLY_IF_CACHED)).isAnyOf(0, -1);
        assertThat(sizable.saturatedIntSize(QueryOption.ONLY_IF_CACHED)).isAnyOf(0, -1);
        assertThat(sizable.isEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.TRUE, Maybe.UNKNOWN);
        assertThat(sizable.isNotEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.FALSE, Maybe.UNKNOWN);
    }

    @ParameterizedTest @EnumSource(Scenario.class)
    public void int32_size(Scenario scenario) {
        long size = Integer.MAX_VALUE;
        LongImpl sizable = new LongImpl(scenario, size);

        assertThat(sizable.size()).isEqualTo(size);
        assertThat(sizable.exactIntSize()).isEqualTo(size);
        assertThat(sizable.saturatedIntSize()).isEqualTo(size);
        assertThat(sizable.isEmpty()).isFalse();
        assertThat(sizable.isNotEmpty()).isTrue();

        assertThat(sizable.size(QueryOption.FORCE_EXACT)).isEqualTo(size);
        assertThat(sizable.exactIntSize(QueryOption.FORCE_EXACT)).isEqualTo(size);
        assertThat(sizable.saturatedIntSize(QueryOption.FORCE_EXACT)).isEqualTo(size);
        assertThat(sizable.isEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.FALSE);
        assertThat(sizable.isNotEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.TRUE);

        assertThat(sizable.size(QueryOption.ONLY_IF_CACHED)).isAnyOf(size, -1L);
        assertThat(sizable.exactIntSize(QueryOption.FORCE_EXACT)).isAnyOf((int) size, -1);
        assertThat(sizable.saturatedIntSize(QueryOption.FORCE_EXACT)).isAnyOf((int) size, -1);
        assertThat(sizable.isEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.FALSE, Maybe.UNKNOWN);
        assertThat(sizable.isNotEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.TRUE, Maybe.UNKNOWN);
    }

    @ParameterizedTest @EnumSource(Scenario.class)
    public void int64_size(Scenario scenario) {
        long size = 1L + Integer.MAX_VALUE;
        int intSize = Integer.MAX_VALUE;
        LongImpl sizable = new LongImpl(scenario, size);

        assertThat(sizable.size()).isEqualTo(size);
        assertThrows(AssertionError.class, () -> sizable.exactIntSize());
        assertThat(sizable.saturatedIntSize()).isEqualTo(intSize);
        assertThat(sizable.isEmpty()).isFalse();
        assertThat(sizable.isNotEmpty()).isTrue();

        assertThat(sizable.size(QueryOption.FORCE_EXACT)).isEqualTo(size);
        assertThrows(AssertionError.class, () -> sizable.exactIntSize(QueryOption.FORCE_EXACT));
        assertThat(sizable.saturatedIntSize(QueryOption.FORCE_EXACT)).isEqualTo(intSize);
        assertThat(sizable.isEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.FALSE);
        assertThat(sizable.isNotEmpty(QueryOption.FORCE_EXACT)).isEqualTo(Maybe.TRUE);

        assertThat(sizable.size(QueryOption.ONLY_IF_CACHED)).isAnyOf(size, -1L);
        // assertThrows(AssertionError.class, () -> sizable.exactIntSize(QueryOption.ONLY_IF_CACHED));
        assertThat(sizable.saturatedIntSize(QueryOption.FORCE_EXACT)).isAnyOf(intSize, -1);
        assertThat(sizable.isEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.FALSE, Maybe.UNKNOWN);
        assertThat(sizable.isNotEmpty(QueryOption.ONLY_IF_CACHED)).isAnyOf(Maybe.TRUE, Maybe.UNKNOWN);
    }

    private record LongImpl(@NotNull Scenario scenario, long val) implements LongSize.DistributedLongSize {
        @Override public long size(@NotNull QueryOption option) {
            return switch (option) {
                case ONLY_IF_CACHED -> switch (scenario) {
                    case LONG_ALL_IN_MEMORY -> val;
                    case LONG_FETCHING -> -1;
                };
                case FORCE_EXACT -> val;
            };
        }
    }

    private enum Scenario {
        LONG_ALL_IN_MEMORY,
        LONG_FETCHING,
    }
}
