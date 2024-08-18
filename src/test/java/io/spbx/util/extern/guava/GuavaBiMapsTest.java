package io.spbx.util.extern.guava;

import com.google.common.collect.HashBiMap;
import io.spbx.util.base.Pair;
import org.junit.jupiter.api.Test;

import java.util.stream.Collector;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GuavaBiMapsTest {
    private static final Integer NULL = null;

    @Test
    public void toHashBiMap_overwriteDuplicates() {
        Collector<Pair<Integer, Integer>, ?, HashBiMap<Integer, Integer>> collector =
            GuavaBiMaps.toHashBiMap(Pair::first, Pair::second, GuavaBiMaps.BiMapPutMethod.overwrite());

        assertThat(Stream.<Pair<Integer, Integer>>empty().collect(collector)).isEmpty();
        assertThat(streamOf(pairOf(1, 2)).collect(collector)).containsExactly(1, 2);
        assertThat(streamOf(pairOf(1, 2), pairOf(3, 4)).collect(collector)).containsExactly(1, 2, 3, 4);
        assertThat(streamOf(pairOf(1, 2), pairOf(1, 3)).collect(collector)).containsExactly(1, 3);
        assertThat(streamOf(pairOf(1, 2), pairOf(3, 2)).collect(collector)).containsExactly(3, 2);
        assertThat(parallelOf(pairOf(1, 2)).collect(collector)).containsExactly(1, 2);
        assertThat(parallelOf(pairOf(1, 2), pairOf(3, 4)).collect(collector)).containsExactly(1, 2, 3, 4);
        assertThat(parallelOf(pairOf(1, 2), pairOf(1, 3)).collect(collector)).containsExactly(1, 3);
        assertThat(parallelOf(pairOf(1, 2), pairOf(3, 2)).collect(collector)).containsExactly(3, 2);

        assertThat(streamOf(pairOf(NULL, NULL)).collect(collector)).containsExactly(null, null);
        assertThat(streamOf(pairOf(NULL, 1)).collect(collector)).containsExactly(null, 1);
        assertThat(streamOf(pairOf(NULL, 1), pairOf(NULL, 2)).collect(collector)).containsExactly(null, 2);
        assertThat(streamOf(pairOf(1, NULL)).collect(collector)).containsExactly(1, null);
        assertThat(streamOf(pairOf(1, NULL), pairOf(2, NULL)).collect(collector)).containsExactly(2, null);
        assertThat(parallelOf(pairOf(NULL, NULL)).collect(collector)).containsExactly(null, null);
        assertThat(parallelOf(pairOf(NULL, 1)).collect(collector)).containsExactly(null, 1);
        assertThat(parallelOf(pairOf(NULL, 1), pairOf(NULL, 2)).collect(collector)).containsExactly(null, 2);
        assertThat(parallelOf(pairOf(1, NULL)).collect(collector)).containsExactly(1, null);
        assertThat(parallelOf(pairOf(1, NULL), pairOf(2, NULL)).collect(collector)).containsExactly(2, null);
    }

    @Test
    public void toHashBiMap_throwing() {
        Collector<Pair<Integer, Integer>, ?, HashBiMap<Integer, Integer>> collector =
            GuavaBiMaps.toHashBiMap(Pair::first, Pair::second, GuavaBiMaps.BiMapPutMethod.throwing());

        assertThat(Stream.<Pair<Integer, Integer>>empty().collect(collector)).isEmpty();
        assertThat(streamOf(pairOf(1, 2)).collect(collector)).containsExactly(1, 2);
        assertThat(streamOf(pairOf(1, 2), pairOf(3, 4)).collect(collector)).containsExactly(1, 2, 3, 4);
        assertThat(parallelOf(pairOf(1, 2)).collect(collector)).containsExactly(1, 2);
        assertThat(parallelOf(pairOf(1, 2), pairOf(3, 4)).collect(collector)).containsExactly(1, 2, 3, 4);
        assertFailure(() -> streamOf(pairOf(1, 2), pairOf(1, 3)).collect(collector)).throwsIllegalArgument();
        assertFailure(() -> streamOf(pairOf(1, 2), pairOf(3, 2)).collect(collector)).throwsIllegalArgument();
        assertFailure(() -> parallelOf(pairOf(1, 2), pairOf(1, 3)).collect(collector)).throwsIllegalArgument();
        assertFailure(() -> parallelOf(pairOf(1, 2), pairOf(3, 2)).collect(collector)).throwsIllegalArgument();

        assertThat(streamOf(pairOf(NULL, NULL)).collect(collector)).containsExactly(null, null);
        assertThat(streamOf(pairOf(NULL, 1)).collect(collector)).containsExactly(null, 1);
        assertThat(streamOf(pairOf(1, NULL)).collect(collector)).containsExactly(1, null);
        assertThat(parallelOf(pairOf(NULL, NULL)).collect(collector)).containsExactly(null, null);
        assertThat(parallelOf(pairOf(1, NULL)).collect(collector)).containsExactly(1, null);
        assertThat(parallelOf(pairOf(NULL, 1)).collect(collector)).containsExactly(null, 1);
        assertFailure(() -> streamOf(pairOf(NULL, 1), pairOf(NULL, 2)).collect(collector)).throwsIllegalArgument();
        assertFailure(() -> streamOf(pairOf(1, NULL), pairOf(2, NULL)).collect(collector)).throwsIllegalArgument();
        assertFailure(() -> parallelOf(pairOf(NULL, 1), pairOf(NULL, 2)).collect(collector)).throwsIllegalArgument();
        assertFailure(() -> parallelOf(pairOf(1, NULL), pairOf(2, NULL)).collect(collector)).throwsIllegalArgument();
    }
}
