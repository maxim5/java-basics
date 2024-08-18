package io.spbx.util.collect;

import io.spbx.util.base.Pair;
import io.spbx.util.collect.TestingMapCollectors.InputMapCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.function.Function;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertStreamer.assertStream;
import static io.spbx.util.collect.TestingMapCollectors.DUP_KEY_ERR;
import static io.spbx.util.collect.TestingMapCollectors.DUP_KEY_ERR_GUAVA;
import static io.spbx.util.testing.AssertFailure.assertFailure;

public class ToMapApiTest {
    private static final Function<Pair<Integer, Integer>, Integer> first = Pair::first;
    private static final Function<Pair<Integer, Integer>, Integer> second = Pair::second;

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_map(InputMapCase input) {
        var api = ToMapApi.of(input.pairs());
        if (!input.hasDuplicateKeys()) {
            assertThat(api.toMap(first, second)).containsExactlyEntriesIn(input.map());
            assertThat(api.toMapBy(first)).containsExactlyEntriesIn(input.mapToPairs());
        } else {
            assertFailure(() -> api.toMap(first, second)).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
            assertFailure(() -> api.toMapBy(first)).throwsIllegalState().hasMessageMatching(DUP_KEY_ERR);
        }
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void to_guava_immutable_map(InputMapCase input) {
        var api = ToMapApi.of(input.pairs());
        if (input.hasNulls()) {
            assertFailure(() -> api.toGuavaImmutableMap(first, second)).throwsAssertion().hasMessageContains("Guava");
            // assertFailure(() -> api.toGuavaImmutableMapBy(first)).throwsAssertion().hasMessageContains("Guava");
        } else if (input.hasDuplicateKeys()) {
            assertFailure(() -> api.toGuavaImmutableMap(first, second)).throwsIllegalArgument().hasMessageMatching(DUP_KEY_ERR_GUAVA);
            assertFailure(() -> api.toGuavaImmutableMapBy(first)).throwsIllegalArgument().hasMessageMatching(DUP_KEY_ERR_GUAVA);
        } else {
            assertThat(api.toGuavaImmutableMap(first, second)).containsExactlyEntriesIn(input.map());
            assertThat(api.toGuavaImmutableMapBy(first)).containsExactlyEntriesIn(input.mapToPairs());
        }
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void maps_simple(InputMapCase input) {
        var api = ToMapApi.of(input.pairs());
        assertStream(api.maps(first, second)).containsExactlyElementsInOrder(input.pairs());
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void bimaps_simple(InputMapCase input) {
        var api = ToMapApi.of(input.pairs());
        assertStream(api.bimaps(first, second)).containsExactlyElementsInOrder(input.pairs());
    }

    @ParameterizedTest @EnumSource(InputMapCase.class)
    public void multimaps_simple(InputMapCase input) {
        var api = ToMapApi.of(input.pairs());
        assertStream(api.multimaps(first, second)).containsExactlyElementsInOrder(input.pairs());
    }
}
