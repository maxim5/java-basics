package io.spbx.util.collect.stream;

import io.spbx.util.collect.stream.TestingMapCollectors.InputMapCase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.stream.BasicCollectors.MapMergers.ignoreDuplicates;
import static io.spbx.util.collect.stream.TestingMapCollectors.DUP_KEY_ERR;
import static io.spbx.util.collect.stream.TestingMapCollectors.DUP_KEY_ERR_GUAVA;
import static io.spbx.util.collect.stream.TestingMapCollectors.Merge.IGNORE;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.AssertStreamer.assertStream;

@Tag("fast")
@SuppressWarnings("Convert2MethodRef")
public class ToMapKvApiTest {
    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_standard_maps_accepting_nulls(InputMapCase input) {
        var api = ToMapKvApi.of(input.pairs());
        if (!input.hasDuplicateKeys()) {
            assertThat(api.toMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toHashMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toLinkedHashMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toOrderedMap()).containsExactlyEntriesIn(input.map());
        } else {
            assertFailure(() -> api.toMap()).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toHashMap()).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toLinkedHashMap()).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toOrderedMap()).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
        }
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_standard_maps_not_accepting_nulls(InputMapCase input) {
        var api = ToMapKvApi.of(input.pairs());
        if (input.hasNulls()) {
            assertFailure(() -> api.toConcurrentHashMap()).throwsAssertion().hasMessageContains("toConcurrentHashMap");
            assertFailure(() -> api.toTreeMap()).throwsAssertion().hasMessageContains("toTreeMap");
            assertFailure(() -> api.toTreeMap(Integer::compare)).throwsAssertion().hasMessageContains("toTreeMap");
            assertFailure(() -> api.toGuavaImmutableMap()).throwsAssertion().hasMessageContains("toGuavaImmutableMap");
            assertFailure(() -> api.toGuavaImmutableMap(ignoreDuplicates())).throwsAssertion().hasMessageContains("Guava");
        } else if (input.hasDuplicateKeys()) {
            assertFailure(() -> api.toConcurrentHashMap()).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toTreeMap()).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toTreeMap(Integer::compare)).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toGuavaImmutableMap()).throwsIllegalArgument().hasMessageMatching(DUP_KEY_ERR_GUAVA);
            assertThat(api.toGuavaImmutableMap(ignoreDuplicates()));
        } else {
            assertThat(api.toConcurrentHashMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toConcurrentMap(() -> new ConcurrentHashMap<>())).containsExactlyEntriesIn(input.map());
            assertThat(api.toTreeMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toTreeMap(Integer::compareUnsigned)).containsExactlyEntriesIn(input.map());
            assertThat(api.toGuavaImmutableMap()).containsExactlyEntriesIn(input.map());
            assertThat(api.toGuavaImmutableMap(ignoreDuplicates())).containsExactlyEntriesIn(input.map());
        }
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_maps_ad_hoc_accept_anything(InputMapCase input) {
        var api = ToMapKvApi.of(input.pairs());
        assertThat(api.toMap(ignoreDuplicates(), () -> new HashMap<>())).containsExactlyEntriesIn(input.map(IGNORE));
        assertThat(api.toMapIgnoreDuplicateKeys()).containsExactlyEntriesIn(input.map(IGNORE));
        assertThat(api.toMapIgnoreDuplicateKeys(() -> new HashMap<>())).containsExactlyEntriesIn(input.map(IGNORE));
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void bimaps_simple(InputMapCase input) {
        var api = ToMapKvApi.of(input.pairs());
        assertStream(api.bimaps()).containsExactlyElementsInOrder(input.pairs());
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void multimaps_simple(InputMapCase input) {
        var api = ToMapKvApi.of(input.pairs());
        assertStream(api.multimaps()).containsExactlyElementsInOrder(input.pairs());
    }
}
