package io.spbx.util.collect;

import io.spbx.util.base.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.BasicCollectors.MapMergers.ignoreDuplicates;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.*;
import static io.spbx.util.testing.TestingPrimitives.bytes;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class BasicCollectorsTest {
    private static final Integer NULL = null;

    /** {@link BasicCollectors#toMap } */

    @Test
    public void toMap_default() {
        Collector<Integer, ?, Map<Long, Long>> collector = BasicCollectors.toMap(i -> (long) i, i -> i * 10L, HashMap::new);
        assertThat(Stream.<Integer>of().collect(collector)).containsExactly();
        assertThat(streamOf(1).collect(collector)).containsExactly(1L, 10L);
        assertThat(streamOf(1, 2).collect(collector)).containsExactly(1L, 10L, 2L, 20L);
        assertThat(parallelOf(1).collect(collector)).containsExactly(1L, 10L);
        assertThat(parallelOf(1, 2).collect(collector)).containsExactly(1L, 10L, 2L, 20L);
        assertFailure(() -> streamOf(1, 2, 1).collect(collector)).throwsIllegalState();
        assertFailure(() -> parallelOf(1, 2, 1).collect(collector)).throwsIllegalState();
    }

    @Test
    public void toMap_default_nulls() {
        Collector<Integer, ?, Map<Integer, Integer>> collector = BasicCollectors.toMap(i -> i, i -> i, HashMap::new);
        assertThat(streamOf(NULL).collect(collector)).containsExactly(null, null);
        assertThat(streamOf(NULL, NULL).collect(collector)).containsExactly(null, null);
        assertThat(parallelOf(NULL).collect(collector)).containsExactly(null, null);
        assertThat(parallelOf(NULL, NULL).collect(collector)).containsExactly(null, null);
    }

    @Test
    public void toMap_default_null_keys() {
        Collector<Pair<Integer, Integer>, ?, Map<Integer, Integer>> collector =
            BasicCollectors.toMap(Pair::first, Pair::second, HashMap::new);
        assertThat(streamOf(pairOf(NULL, 1)).collect(collector)).containsExactly(null, 1);
        assertThat(parallelOf(pairOf(NULL, 1)).collect(collector)).containsExactly(null, 1);
        assertFailure(() -> streamOf(pairOf(NULL, 1), pairOf(NULL, 2)).collect(collector)).throwsIllegalState();
        assertFailure(() -> parallelOf(pairOf(NULL, 1), pairOf(NULL, 2)).collect(collector)).throwsIllegalState();
    }

    @Test
    public void toMap_default_null_values() {
        Collector<Pair<Integer, Integer>, ?, Map<Integer, Integer>> collector =
            BasicCollectors.toMap(Pair::first, Pair::second, HashMap::new);
        assertThat(streamOf(pairOf(1, NULL)).collect(collector)).containsExactly(1, null);
        assertThat(streamOf(pairOf(1, NULL), pairOf(2, NULL)).collect(collector)).containsExactly(1, null, 2, null);
        assertThat(streamOf(pairOf(1, NULL), pairOf(1, NULL)).collect(collector)).containsExactly(1, null);
        assertThat(streamOf(pairOf(1, NULL), pairOf(1, NULL), pairOf(1, 2)).collect(collector)).containsExactly(1, 2);
        assertThat(parallelOf(pairOf(1, NULL), pairOf(2, NULL)).collect(collector)).containsExactly(1, null, 2, null);
        assertThat(parallelOf(pairOf(1, NULL), pairOf(1, NULL), pairOf(1, 2)).collect(collector)).containsExactly(1, 2);
        assertFailure(() -> streamOf(pairOf(1, NULL), pairOf(1, 1), pairOf(1, 2)).collect(collector)).throwsIllegalState();
        assertFailure(() -> parallelOf(pairOf(1, NULL), pairOf(1, 1), pairOf(1, 2)).collect(collector)).throwsIllegalState();
    }

    @Test
    public void toMap_with_merger() {
        Collector<Integer, ?, Map<Long, Long>> collector =
            BasicCollectors.toMap(i -> (long) i, i -> i * 10L, ignoreDuplicates(), LinkedHashMap::new);
        assertThat(Stream.<Integer>of().collect(collector)).containsExactly();
        assertThat(streamOf(1).collect(collector)).containsExactly(1L, 10L).inOrder();
        assertThat(streamOf(1, 2).collect(collector)).containsExactly(1L, 10L, 2L, 20L).inOrder();
        assertThat(streamOf(1, 1, 1).collect(collector)).containsExactly(1L, 10L).inOrder();
        assertThat(streamOf(1, 2, 1).collect(collector)).containsExactly(1L, 10L, 2L, 20L).inOrder();
        assertThat(parallelOf(1).collect(collector)).containsExactly(1L, 10L);
        assertThat(parallelOf(1, 2).collect(collector)).containsExactly(1L, 10L, 2L, 20L);
        assertThat(parallelOf(1, 1, 1).collect(collector)).containsExactly(1L, 10L);
        assertThat(parallelOf(1, 2, 1).collect(collector)).containsExactly(1L, 10L, 2L, 20L);
    }

    @Test
    public void toMap_with_merger_nulls() {
        Collector<Integer, ?, Map<Integer, Integer>> collector =
            BasicCollectors.toMap(i -> i, i -> i, ignoreDuplicates(), LinkedHashMap::new);
        assertThat(streamOf(NULL).collect(collector)).containsExactly(null, null).inOrder();
        assertThat(streamOf(NULL, NULL).collect(collector)).containsExactly(null, null).inOrder();
        assertThat(streamOf(NULL, NULL, NULL).collect(collector)).containsExactly(null, null).inOrder();
        assertThat(parallelOf(NULL).collect(collector)).containsExactly(null, null);
        assertThat(parallelOf(NULL, NULL).collect(collector)).containsExactly(null, null);
        assertThat(parallelOf(NULL, NULL, NULL).collect(collector)).containsExactly(null, null);
    }

    /** {@link BasicCollectors#toOnlyNonNullOrEmpty()} */

    @Test
    public void toOnlyNonNullOrEmpty_simple() {
        assertThat(streamOf().collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(1).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(streamOf(1, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(1, 2, 3).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(1, 2, 3, 4).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf().collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(1).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(parallelOf(1, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(1, 2, 3).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(1, 2, 3, 4).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
    }

    @Test
    public void toOnlyNonNullOrEmpty_all_nulls() {
        assertThat(streamOf(NULL).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(null, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(null, null, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(NULL).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(null, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(null, null, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
    }

    @Test
    public void toOnlyNonNullOrEmpty_nulls_and_one_value() {
        assertThat(streamOf(1, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(streamOf(null, 1).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(streamOf(null, null, 1, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(parallelOf(1, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(parallelOf(null, 1).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
        assertThat(parallelOf(null, null, 1, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).hasValue(1);
    }

    @Test
    public void toOnlyNonNullOrEmpty_nulls_and_several_values() {
        assertThat(streamOf(1, 2, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(1, null, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(1, null, null, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(null, 1, null, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(streamOf(null, null, 1, 2, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(1, 2, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(1, null, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(1, null, null, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(null, 1, null, 2).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
        assertThat(parallelOf(null, null, 1, 2, null).collect(BasicCollectors.toOnlyNonNullOrEmpty())).isEmpty();
    }

    /** {@link BasicCollectors#allEqual } */

    @Test
    public void allEqual() {
        assertThat(BasicCollectors.allEqual(streamOf())).isTrue();
        assertThat(BasicCollectors.allEqual(streamOf(1))).isTrue();
        assertThat(BasicCollectors.allEqual(streamOf(1, 1))).isTrue();
        assertThat(BasicCollectors.allEqual(streamOf(1, 1, 1))).isTrue();
        assertThat(BasicCollectors.allEqual(streamOf(1, 2))).isFalse();
        assertThat(BasicCollectors.allEqual(streamOf(1, 2, 1))).isFalse();
        assertThat(BasicCollectors.allEqual(streamOf(1, 2, 2))).isFalse();
        assertThat(BasicCollectors.allEqual(streamOf(1, 1, 2))).isFalse();
        assertThat(BasicCollectors.allEqual(parallelOf())).isTrue();
        assertThat(BasicCollectors.allEqual(parallelOf(1))).isTrue();
        assertThat(BasicCollectors.allEqual(parallelOf(1, 1))).isTrue();
        assertThat(BasicCollectors.allEqual(parallelOf(1, 1, 1))).isTrue();
        assertThat(BasicCollectors.allEqual(parallelOf(1, 2))).isFalse();
        assertThat(BasicCollectors.allEqual(parallelOf(1, 2, 1))).isFalse();
        assertThat(BasicCollectors.allEqual(parallelOf(1, 2, 2))).isFalse();
        assertThat(BasicCollectors.allEqual(parallelOf(1, 1, 2))).isFalse();
    }

    /** {@link BasicCollectors#toByteArray()} */

    @Test
    public void toByteArray_simple() {
        assertThat(Stream.<Integer>of().collect(BasicCollectors.toByteArray())).isEmpty();
        assertThat(streamOf(1).collect(BasicCollectors.toByteArray())).isEqualTo(bytes(1));
        assertThat(streamOf(1, 2).collect(BasicCollectors.toByteArray())).isEqualTo(bytes(1, 2));
        assertThat(parallelOf(1).collect(BasicCollectors.toByteArray())).isEqualTo(bytes(1));
        assertThat(parallelOf(1, 2).collect(BasicCollectors.toByteArray())).isEqualTo(bytes(1, 2));
        assertThat(intStreamOf().boxed().collect(BasicCollectors.toByteArray())).isEmpty();
        assertThat(intStreamOf(1).boxed().collect(BasicCollectors.toByteArray())).isEqualTo(bytes(1));
        assertThat(intStreamOf(1, 2).boxed().collect(BasicCollectors.toByteArray())).isEqualTo(bytes(1, 2));
    }
}
