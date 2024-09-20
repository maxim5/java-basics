package io.spbx.util.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.spbx.util.base.Maybe;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.BasicMaps.newMutableMap;
import static io.spbx.util.func.ScopeFunctions.alsoApply;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class BasicIterablesTest {
    private static final Integer NULL = null;

    /** {@link BasicIterables#asList(Iterable)} */

    @Test
    public void asList_list_same_instance() {
        alsoApply(listOf(1, 2), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
        alsoApply(List.of(1, 2), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
        alsoApply(new ArrayList<>(), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
    }

    @Test
    public void asList_collection() {
        assertThat(BasicIterables.asList(setOf())).isEmpty();
        assertThat(BasicIterables.asList(setOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asList(setOf(1, 2))).containsExactly(1, 2).inOrder();
        assertThat(BasicIterables.asList(sortedSetOf())).isEmpty();
        assertThat(BasicIterables.asList(sortedSetOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asList(sortedSetOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void asList_iterable() {
        assertThat(BasicIterables.asList(iterableOf())).isEmpty();
        assertThat(BasicIterables.asList(iterableOf("foo"))).containsExactly("foo").inOrder();
        assertThat(BasicIterables.asList(iterableOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
    }

    /** {@link BasicIterables#asArrayList(Iterable)} */

    @Test
    public void asArrayList_list_same_instance() {
        alsoApply(new ArrayList<>(), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
    }

    @Test
    public void asArrayList_other_lists_and_collections() {
        assertThat(BasicIterables.asArrayList(listOf())).isEmpty();
        assertThat(BasicIterables.asArrayList(listOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asArrayList(listOf(1, 2))).containsExactly(1, 2).inOrder();
        assertThat(BasicIterables.asArrayList(setOf())).isEmpty();
        assertThat(BasicIterables.asArrayList(setOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asArrayList(setOf(1, 2))).containsExactly(1, 2).inOrder();
        assertThat(BasicIterables.asArrayList(sortedSetOf())).isEmpty();
        assertThat(BasicIterables.asArrayList(sortedSetOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asArrayList(sortedSetOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void asArrayList_iterable() {
        assertThat(BasicIterables.asArrayList(iterableOf())).isEmpty();
        assertThat(BasicIterables.asArrayList(iterableOf("foo"))).containsExactly("foo").inOrder();
        assertThat(BasicIterables.asArrayList(iterableOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
    }

    /** {@link BasicIterables#asSet(Iterable)} */

    @Test
    public void asSet_set_same_instance() {
        alsoApply(setOf(1, 2), items -> assertThat(BasicIterables.asSet(items)).isSameInstanceAs(items));
        alsoApply(sortedSetOf(1, 2), items -> assertThat(BasicIterables.asSet(items)).isSameInstanceAs(items));
    }

    @Test
    public void asSet_collections() {
        assertThat(BasicIterables.asSet(listOf())).isEmpty();
        assertThat(BasicIterables.asSet(listOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asSet(listOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void asSet_iterable() {
        assertThat(BasicIterables.asSet(iterableOf())).isEmpty();
        assertThat(BasicIterables.asSet(iterableOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asSet(iterableOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#asCollection(Iterable)} */

    @Test
    public void asCollection_collection_same_instance() {
        alsoApply(listOf(1, 2), items -> assertThat(BasicIterables.asCollection(items)).isSameInstanceAs(items));
        alsoApply(setOf(1, 2), items -> assertThat(BasicIterables.asCollection(items)).isSameInstanceAs(items));
        alsoApply(sortedSetOf(1, 2), items -> assertThat(BasicIterables.asCollection(items)).isSameInstanceAs(items));
    }

    @Test
    public void asCollection_iterable() {
        assertThat(BasicIterables.asCollection(iterableOf())).isEmpty();
        assertThat(BasicIterables.asCollection(iterableOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asCollection(iterableOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#forEachToList(Consumer)} */

    @Test
    public void forEachToList_simple() {
        List<Integer> items = listOf(1, 2, 3);
        assertThat(BasicIterables.forEachToList(items::forEach)).isEqualTo(items);
    }

    @Test
    public void forEachToList_empty() {
        List<Integer> items = listOf();
        assertThat(BasicIterables.forEachToList(items::forEach)).isEqualTo(items);
    }

    /** {@link BasicIterables#forEachZipped(Object[], Object[], BiConsumer)} */

    @Test
    public void forEachZipped_arrays_simple() {
        Map<Integer, String> map = newMutableMap();
        BasicIterables.forEachZipped(arrayOf(1, 2, 3), arrayOf("1", "2", "3"), map::put);
        assertThat(map).containsExactly(1, "1", 2, "2", 3, "3");
    }

    @Test
    public void forEachZipped_arrays_empty() {
        Map<Integer, String> map = newMutableMap();
        BasicIterables.forEachZipped(arrayOf(), arrayOf(), map::put);
        assertThat(map).isEmpty();
    }

    @Test
    public void forEachZipped_arrays_mismatch() {
        assertFailure(() -> BasicIterables.forEachZipped(arrayOf(), arrayOf(1, 2), (a, b) -> {})).throwsAssertion();
        assertFailure(() -> BasicIterables.forEachZipped(arrayOf(1, 2), arrayOf(), (a, b) -> {})).throwsAssertion();
        assertFailure(() -> BasicIterables.forEachZipped(arrayOf(1), arrayOf(1, 2), (a, b) -> {})).throwsAssertion();
        assertFailure(() -> BasicIterables.forEachZipped(arrayOf(1, 2), arrayOf(1), (a, b) -> {})).throwsAssertion();
    }

    /** {@link BasicIterables#forEachZipped(Iterable, Iterable, BiConsumer)} */

    @Test
    public void forEachZipped_iterables_simple() {
        Map<Integer, String> map = newMutableMap();
        BasicIterables.forEachZipped(iterableOf(1, 2, 3), iterableOf("1", "2", "3"), map::put);
        assertThat(map).containsExactly(1, "1", 2, "2", 3, "3");
    }

    @Test
    public void forEachZipped_iterables_empty() {
        Map<Integer, String> map = newMutableMap();
        BasicIterables.forEachZipped(iterableOf(), iterableOf(), map::put);
        assertThat(map).isEmpty();
    }

    @Test
    public void forEachZipped_iterables_mismatch() {
        assertFailure(() -> BasicIterables.forEachZipped(iterableOf(), iterableOf(1, 2), (a, b) -> {})).throwsAssertion();
        assertFailure(() -> BasicIterables.forEachZipped(iterableOf(1, 2), iterableOf(), (a, b) -> {})).throwsAssertion();
        assertFailure(() -> BasicIterables.forEachZipped(iterableOf(1), iterableOf(1, 2), (a, b) -> {})).throwsAssertion();
        assertFailure(() -> BasicIterables.forEachZipped(iterableOf(1, 2), iterableOf(1), (a, b) -> {})).throwsAssertion();
    }

    /** {@link BasicIterables#sizeOf}  */

    @Test
    public void sizeOf_list() {
        assertThat(BasicIterables.sizeOf(listOf())).isEqualTo(0);
        assertThat(BasicIterables.sizeOf(listOf(1))).isEqualTo(1);
        assertThat(BasicIterables.sizeOf(listOf(1, 2))).isEqualTo(2);
    }

    @Test
    public void sizeOf_set() {
        assertThat(BasicIterables.sizeOf(setOf())).isEqualTo(0);
        assertThat(BasicIterables.sizeOf(setOf(1))).isEqualTo(1);
        assertThat(BasicIterables.sizeOf(setOf(1, 2))).isEqualTo(2);
    }

    @Test
    public void sizeOf_iterable() {
        assertThat(BasicIterables.sizeOf(iterableOf(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.sizeOf(iterableOf(1), -1)).isEqualTo(-1);
        assertThat(BasicIterables.sizeOf(iterableOf(1, 2), -1)).isEqualTo(-1);
    }

    /** {@link BasicIterables#isEmpty}  */

    @Test
    public void isEmpty_list() {
        assertThat(BasicIterables.isEmpty(listOf())).isTrue();
        assertThat(BasicIterables.isEmpty(listOf(1))).isFalse();
        assertThat(BasicIterables.isEmpty(listOf(1, 2))).isFalse();
    }

    @Test
    public void isEmpty_set() {
        assertThat(BasicIterables.isEmpty(setOf())).isTrue();
        assertThat(BasicIterables.isEmpty(setOf(1))).isFalse();
        assertThat(BasicIterables.isEmpty(setOf(1, 2))).isFalse();
    }

    @Test
    public void isEmpty_iterable() {
        assertThat(BasicIterables.isEmpty(ArrayIterable.of())).isTrue();
        assertThat(BasicIterables.isEmpty(ArrayIterable.of(1))).isFalse();
        assertThat(BasicIterables.isEmpty(ArrayIterable.of(1, 2))).isFalse();
    }

    /** {@link BasicIterables#estimateSize} */

    @Test
    public void estimateSize_collection() {
        assertThat(BasicIterables.estimateSize(listOf(), -1)).isEqualTo(0);
        assertThat(BasicIterables.estimateSize(listOf(1), -1)).isEqualTo(1);
        assertThat(BasicIterables.estimateSize(listOf(1, 2), -1)).isEqualTo(2);
    }

    @Test
    public void estimateSize_iterable() {
        assertThat(BasicIterables.estimateSize(iterableOf(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.estimateSize(iterableOf(1), -1)).isEqualTo(-1);
        assertThat(BasicIterables.estimateSize(iterableOf(1, 2), -1)).isEqualTo(-1);
    }

    @Test
    public void estimateSize_spliterator() {
        assertThat(BasicIterables.estimateSize(spliteratorOf(), -1)).isEqualTo(0);
        assertThat(BasicIterables.estimateSize(spliteratorOf(1), -1)).isEqualTo(1);
        assertThat(BasicIterables.estimateSize(spliteratorOf(1, 2), -1)).isEqualTo(2);
    }

    @Test
    public void estimateSize_spliterator_unknown_size() {
        assertThat(BasicIterables.estimateSize(spliteratorOfUnknownSize(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.estimateSize(spliteratorOfUnknownSize(1), -1)).isEqualTo(-1);
        assertThat(BasicIterables.estimateSize(spliteratorOfUnknownSize(1, 2), -1)).isEqualTo(-1);
    }

    /** {@link BasicIterables#emptyIfNull}  */

    @Test
    public void emptyIfNull_list() {
        assertThat(BasicIterables.emptyIfNull(NULL_LIST)).isEmpty();
        assertThat(BasicIterables.emptyIfNull(listOf())).isEmpty();
        assertThat(BasicIterables.emptyIfNull(listOf(1))).containsExactly(1);
    }

    @Test
    public void emptyIfNull_set() {
        assertThat(BasicIterables.emptyIfNull(NULL_SET)).isEmpty();
        assertThat(BasicIterables.emptyIfNull(setOf())).isEmpty();
        assertThat(BasicIterables.emptyIfNull(setOf(1))).containsExactly(1);
    }

    @Test
    @SuppressWarnings("ObviousNullCheck")
    public void emptyIfNull_collection() {
        assertThat(BasicIterables.emptyIfNull(NULL_COLLECTION)).isEmpty();
        assertThat(BasicIterables.emptyIfNull(new ArrayDeque<>())).isEmpty();
        assertThat(BasicIterables.emptyIfNull(new ArrayDeque<>(listOf(1)))).containsExactly(1);
    }

    @Test
    public void emptyIfNull_iterable() {
        assertThat(BasicIterables.emptyIfNull(NULL_ITERABLE)).isEmpty();
        assertThat(BasicIterables.emptyIfNull(ArrayIterable.of())).isEmpty();
        assertThat(BasicIterables.emptyIfNull(ArrayIterable.of(1))).containsExactly(1);
    }

    /** {@link BasicIterables#nullIfEmpty}  */

    @Test
    public void nullIfEmpty_list() {
        assertThat(BasicIterables.nullIfEmpty(NULL_LIST)).isNull();
        assertThat(BasicIterables.nullIfEmpty(listOf())).isNull();
        assertThat(BasicIterables.nullIfEmpty(listOf(1))).containsExactly(1);
    }

    @Test
    public void nullIfEmpty_set() {
        assertThat(BasicIterables.nullIfEmpty(NULL_SET)).isNull();
        assertThat(BasicIterables.nullIfEmpty(setOf())).isNull();
        assertThat(BasicIterables.nullIfEmpty(setOf(1))).containsExactly(1);
    }

    @Test
    public void nullIfEmpty_collection() {
        assertThat(BasicIterables.nullIfEmpty(NULL_COLLECTION)).isNull();
        assertThat(BasicIterables.nullIfEmpty(new ArrayDeque<>())).isNull();
        assertThat(BasicIterables.nullIfEmpty(new ArrayDeque<>(listOf(1)))).containsExactly(1);
    }

    @Test
    public void nullIfEmpty_iterable() {
        assertThat(BasicIterables.nullIfEmpty(NULL_ITERABLE)).isNull();
        assertThat(BasicIterables.nullIfEmpty(ArrayIterable.of())).isNull();
        assertThat(BasicIterables.nullIfEmpty(ArrayIterable.of(1))).containsExactly(1);
    }

    /** {@link BasicIterables#replaceListElements(List, Collection)} */

    @Test
    public void replaceListElements_simple() {
        ArrayList<Integer> list = BasicIterables.newMutableList(listOf(1, 2, 3));
        assertThat(BasicIterables.replaceListElements(list, listOf(3, 2, 1))).isSameInstanceAs(list);
        assertThat(list).containsExactly(3, 2, 1).inOrder();
    }

    /** {@link BasicIterables#distinctInPlace(List)} */

    @Test
    public void distinctInPlace_unchanged() {
        ArrayList<Integer> list = BasicIterables.newMutableList(listOf(1, 2, 3));
        assertThat(BasicIterables.distinctInPlace(list)).isSameInstanceAs(list);
        assertThat(list).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void distinctInPlace_replaced() {
        ArrayList<Integer> list = BasicIterables.newMutableList(listOf(1, 1, 1, 2, 1, 1, 2, 2, 2, 1, 2));
        assertThat(BasicIterables.distinctInPlace(list)).isSameInstanceAs(list);
        assertThat(list).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#concatToList} */

    @Test
    public void concatToList_lists_of_two() {
        assertThat(BasicIterables.concatToList(listOf(1, 2), listOf(3))).containsExactly(1, 2, 3).inOrder();
        assertThat(BasicIterables.concatToList(listOf(1, null), listOf(null, 2))).containsExactly(1, null, null, 2).inOrder();
        assertThat(BasicIterables.concatToList(listOf(NULL), listOf(null, null))).containsExactly(null, null, null).inOrder();
    }

    @Test
    public void concatToList_lists_of_three() {
        assertThat(BasicIterables.concatToList(listOf(1, 2), listOf(3), listOf(4))).containsExactly(1, 2, 3, 4).inOrder();
        assertThat(BasicIterables.concatToList(listOf(), listOf(1), listOf())).containsExactly(1).inOrder();
        assertThat(BasicIterables.concatToList(listOf(), listOf(), listOf())).isEmpty();

        assertThat(BasicIterables.concatToList(listOf(1), listOf(2, null), listOf(null, 3)))
            .containsExactly(1, 2, null, null, 3).inOrder();
        assertThat(BasicIterables.concatToList(listOf(NULL), listOf(null, null), listOf(NULL)))
            .containsExactly(null, null, null, null).inOrder();
    }

    @Test
    public void concatAllToList_lists_of_vararg() {
        assertThat(BasicIterables.concatAllToList()).isEmpty();
        assertThat(BasicIterables.concatAllToList(listOf())).isEmpty();
        assertThat(BasicIterables.concatAllToList(listOf(), listOf(), listOf())).isEmpty();

        assertThat(BasicIterables.concatAllToList(listOf(1, 2), listOf(), listOf())).containsExactly(1, 2).inOrder();
        assertThat(BasicIterables.concatAllToList(listOf(), listOf(1), listOf(2), listOf(3))).containsExactly(1, 2, 3).inOrder();
        assertThat(BasicIterables.concatAllToList(listOf(1, 2), listOf(), listOf(3), listOf())).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void concatToList_iterables_of_two() {
        assertThat(BasicIterables.concatToList(iterableOf(1, 2), listOf(3))).containsExactly(1, 2, 3).inOrder();
        assertThat(BasicIterables.concatToList(iterableOf(1, null), iterableOf(null, 2))).containsExactly(1, null, null, 2).inOrder();
        assertThat(BasicIterables.concatToList(iterableOf(NULL), iterableOf(null, null))).containsExactly(null, null, null).inOrder();
    }

    @Test
    public void concatToList_iterables_of_three() {
        assertThat(BasicIterables.concatToList(iterableOf(1, 2), iterableOf(3), iterableOf(4))).containsExactly(1, 2, 3, 4).inOrder();
        assertThat(BasicIterables.concatToList(iterableOf(), iterableOf(1), iterableOf())).containsExactly(1).inOrder();
        assertThat(BasicIterables.concatToList(iterableOf(), iterableOf(), iterableOf())).isEmpty();

        assertThat(BasicIterables.concatToList(iterableOf(1), iterableOf(2, null), iterableOf(null, 3)))
            .containsExactly(1, 2, null, null, 3).inOrder();
        assertThat(BasicIterables.concatToList(iterableOf(NULL), iterableOf(null, null), iterableOf(NULL)))
            .containsExactly(null, null, null, null).inOrder();
    }

    @Test
    public void concatToList_streams_of_two() {
        assertThat(BasicIterables.concatToList(streamOf(1, 2), streamOf(3))).containsExactly(1, 2, 3).inOrder();
        assertThat(BasicIterables.concatToList(streamOf(1, null), streamOf(null, 2))).containsExactly(1, null, null, 2).inOrder();
        assertThat(BasicIterables.concatToList(streamOf(NULL), streamOf(null, null))).containsExactly(null, null, null).inOrder();
    }

    @Test
    public void concatToList_streams_of_three() {
        assertThat(BasicIterables.concatToList(streamOf(1, 2), streamOf(3), streamOf(4))).containsExactly(1, 2, 3, 4).inOrder();
        assertThat(BasicIterables.concatToList(streamOf(), streamOf(1), streamOf())).containsExactly(1).inOrder();
        assertThat(BasicIterables.concatToList(streamOf(), streamOf(), streamOf())).isEmpty();

        assertThat(BasicIterables.concatToList(streamOf(1), streamOf(2, null), streamOf(null, 3)))
            .containsExactly(1, 2, null, null, 3).inOrder();
        assertThat(BasicIterables.concatToList(streamOf(NULL), streamOf(null, null), streamOf(NULL)))
            .containsExactly(null, null, null, null).inOrder();
    }

    /** {@link BasicIterables#appendToList(Iterable, Object)} */

    @Test
    public void appendToList_simple() {
        assertThat(BasicIterables.appendToList(listOf(1, 2), 3)).containsExactly(1, 2, 3).inOrder();
        assertThat(BasicIterables.appendToList(listOf(1, null), 2)).containsExactly(1, null, 2).inOrder();
        assertThat(BasicIterables.appendToList(listOf(null, null), null)).containsExactly(null, null, null).inOrder();
    }

    /** {@link BasicIterables#isImmutable(Collection)} */

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Test
    public void isImmutable_list() {
        assertThat(BasicIterables.isImmutable(List.of())).isTrue();
        assertThat(BasicIterables.isImmutable(List.of(1))).isTrue();
        assertThat(BasicIterables.isImmutable(List.of(1, 2))).isTrue();
        assertThat(BasicIterables.isImmutable(List.of(1, 2, 3, 4, 5))).isTrue();

        assertThat(BasicIterables.isImmutable(ImmutableList.of())).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableList.of(1))).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableList.of(1, 2))).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableList.of(1, 2, 3, 4, 5))).isTrue();

        assertThat(BasicIterables.isImmutable(Arrays.asList())).isFalse();
        assertThat(BasicIterables.isImmutable(Arrays.asList(1))).isFalse();
        assertThat(BasicIterables.isImmutable(Arrays.asList(1, 2, 3))).isFalse();

        assertThat(BasicIterables.isImmutable(new ArrayList<>())).isFalse();
        assertThat(BasicIterables.isImmutable(new LinkedList<>())).isFalse();
        assertThat(BasicIterables.isImmutable(new Vector<>())).isFalse();
    }

    @Test
    public void isImmutable_set() {
        assertThat(BasicIterables.isImmutable(Set.of())).isTrue();
        assertThat(BasicIterables.isImmutable(Set.of(1))).isTrue();
        assertThat(BasicIterables.isImmutable(Set.of(1, 2))).isTrue();
        assertThat(BasicIterables.isImmutable(Set.of(1, 2, 3, 4, 5))).isTrue();

        assertThat(BasicIterables.isImmutable(ImmutableSet.of())).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableSet.of(1))).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableSet.of(1, 2))).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableSet.of(1, 2, 3, 4, 5))).isTrue();

        assertThat(BasicIterables.isImmutable(new HashSet<>())).isFalse();
        assertThat(BasicIterables.isImmutable(new LinkedHashSet<>())).isFalse();
        assertThat(BasicIterables.isImmutable(new TreeSet<>())).isFalse();
        assertThat(BasicIterables.isImmutable(EnumSet.of(Maybe.TRUE))).isFalse();
    }

    @Test
    public void isImmutable_java_collections() {
        assertThat(BasicIterables.isImmutable(Collections.emptyList())).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.emptySet())).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.emptySortedSet())).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.emptyNavigableSet())).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.singletonList(1))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.singleton(1))).isTrue();

        assertThat(BasicIterables.isImmutable(Collections.unmodifiableCollection(listOf()))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableCollection(listOf(1)))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableList(listOf()))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableList(listOf(1)))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableSet(setOf()))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableSet(setOf(1)))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableSequencedCollection(listOf()))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableSequencedCollection(listOf(1)))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableSequencedSet(new LinkedHashSet<>()))).isTrue();
        assertThat(BasicIterables.isImmutable(Collections.unmodifiableSortedSet(new TreeSet<>()))).isTrue();
    }
}
