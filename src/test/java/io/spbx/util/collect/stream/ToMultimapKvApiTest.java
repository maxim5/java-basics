package io.spbx.util.collect.stream;

import io.spbx.util.collect.stream.TestingMapCollectors.InputMapCase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;

@Tag("fast")
@SuppressWarnings("Convert2MethodRef")
public class ToMultimapKvApiTest {
    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_guava_multimaps_accepting_nulls(InputMapCase input) {
        var api = ToMultimapKvApi.of(input.pairs());
        assertThat(api.toGuavaMultimap()).containsExactlyEntriesIn(input.multimap());
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_guava_multimaps_not_nulls(InputMapCase input) {
        var api = ToMultimapKvApi.of(input.pairs());
        if (input.hasNulls()) {
            assertFailure(() -> api.toGuavaImmutableListMultimap()).throwsAssertion().hasMessageContains("ListMultimap");
            assertFailure(() -> api.toGuavaImmutableSetMultimap()).throwsAssertion().hasMessageContains("SetMultimap");
        } else {
            assertThat(api.toGuavaImmutableListMultimap()).containsExactlyEntriesIn(input.multimap());
            assertThat(api.toGuavaImmutableSetMultimap()).containsExactlyEntriesIn(input.multimap());
        }
    }
}
