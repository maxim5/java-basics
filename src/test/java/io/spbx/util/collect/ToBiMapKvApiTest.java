package io.spbx.util.collect;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.TestingMapCollectors.*;
import static io.spbx.util.collect.TestingMapCollectors.Merge.OVERWRITE;
import static io.spbx.util.testing.AssertFailure.assertFailure;

@SuppressWarnings("Convert2MethodRef")
public class ToBiMapKvApiTest {
    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_guava_bimaps_accepting_nulls(InputMapCase input) {
        var api = ToBiMapKvApi.of(input.pairs());
        if (input.hasDuplicateValues()) {
            assertFailure(() -> api.toGuavaBiMap()).throwsIllegalArgument().hasMessageMatching(DUP_VAL_ERR_BIMAP);
        } else if (input.hasDuplicateKeys()) {
            assertFailure(() -> api.toGuavaBiMap()).throwsIllegalArgument().hasMessageMatching(DUP_KEY_ERR_BIMAP);
        } else {
            assertThat(api.toGuavaBiMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toGuavaBiMapOverwriteDuplicates()).containsExactlyEntriesIn(input.map(OVERWRITE));
        }
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_guava_bimaps_not_accepting_nulls(InputMapCase input) {
        var api = ToBiMapKvApi.of(input.pairs());
        if (input.hasNulls()) {
            assertFailure(() -> api.toGuavaImmutableBiMap()).throwsAssertion().hasMessageContains("toGuavaImmutableBiMap");
        } else if (input.hasDuplicateKeys()) {
            assertFailure(() -> api.toGuavaImmutableBiMap()).throwsIllegalArgument().hasMessageMatching(DUP_KEY_ERR_GUAVA);
        } else if (input.hasDuplicateValues()) {
            assertFailure(() -> api.toGuavaImmutableBiMap()).throwsIllegalArgument().hasMessageMatching(DUP_VAL_ERR_GUAVA);
        } else {
            assertThat(api.toGuavaImmutableBiMap()).containsExactlyEntriesIn(input.map());
        }
    }
}
