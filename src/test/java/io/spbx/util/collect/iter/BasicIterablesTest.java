package io.spbx.util.collect.iter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.spbx.util.base.lang.Maybe;
import io.spbx.util.collect.array.Array;
import io.spbx.util.collect.array.ImmutableArray;
import io.spbx.util.collect.list.ImmutableArrayList;
import io.spbx.util.collect.set.ImmutableLinkedHashSet;
import io.spbx.util.testing.MoreTruth;
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
import static io.spbx.util.collect.map.BasicMaps.newMutableMap;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class BasicIterablesTest {
    private static final Integer NULL = null;

    /** {@link BasicIterables#listOf}  **/

    @Test
    public void listOf_simple() {
        MoreTruth.assertThat(BasicIterables.listOf()).isImmutable().isEmpty();
        MoreTruth.assertThat(BasicIterables.listOf(1)).isImmutable().containsExactly(1).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(1, 2)).isImmutable().containsExactly(1, 2).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(1, 2, 3)).isImmutable().containsExactly(1, 2, 3).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(NULL)).isImmutable().containsExactly(NULL).inOrder();

        MoreTruth.assertThat(BasicIterables.listOf(NULL_ARRAY)).isImmutable().isEmpty();
        MoreTruth.assertThat(BasicIterables.listOf(NULL_LIST)).isImmutable().isEmpty();
        MoreTruth.assertThat(BasicIterables.listOf(NULL_SET)).isImmutable().isEmpty();
        MoreTruth.assertThat(BasicIterables.listOf(NULL_COLLECTION)).isImmutable().isEmpty();
        MoreTruth.assertThat(BasicIterables.listOf(NULL_ITERABLE)).isImmutable().isEmpty();

        MoreTruth.assertThat(BasicIterables.listOf(arrayOf(1, NULL))).isImmutable().containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(listOf(1, NULL))).isImmutable().containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(setOf(1, NULL))).isImmutable().containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(collectionOf(1, NULL))).isImmutable().containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.listOf(iterableOf(1, NULL))).isImmutable().containsExactly(1, NULL).inOrder();
    }

    /** {@link BasicIterables#newMutableList}, {@link BasicIterables#mutableListOf}  **/

    @Test
    public void newMutableList_simple() {
        assertThat(BasicIterables.newMutableList()).isEmpty();
        assertThat(BasicIterables.newMutableList(4)).isEmpty();
    }

    @Test
    public void mutableListOf_simple() {
        assertThat(BasicIterables.mutableListOf()).isEmpty();
        assertThat(BasicIterables.mutableListOf(1)).containsExactly(1);
        assertThat(BasicIterables.mutableListOf(NULL)).containsExactly(NULL);
        assertThat(BasicIterables.mutableListOf(1, 2, NULL)).containsExactly(1, 2, NULL);

        assertThat(BasicIterables.mutableListOf(NULL_ARRAY)).isEmpty();
        assertThat(BasicIterables.mutableListOf(NULL_LIST)).isEmpty();
        assertThat(BasicIterables.mutableListOf(NULL_SET)).isEmpty();
        assertThat(BasicIterables.mutableListOf(NULL_COLLECTION)).isEmpty();
        assertThat(BasicIterables.mutableListOf(NULL_ITERABLE)).isEmpty();

        assertThat(BasicIterables.mutableListOf(arrayOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableListOf(listOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableListOf(setOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableListOf(collectionOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableListOf(iterableOf(1, 2, NULL))).containsExactly(1, 2, NULL);
    }

    /** {@link BasicIterables#setOf}  **/

    @Test
    public void setOf_simple() {
        MoreTruth.assertThat(BasicIterables.setOf()).isImmutable().isEmpty();
        MoreTruth.assertThat(BasicIterables.setOf(1)).isImmutable().containsExactly(1).inOrder();
        MoreTruth.assertThat(BasicIterables.setOf(NULL)).isImmutable().containsExactly(NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.setOf(arrayOf(1, NULL))).containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.setOf(listOf(1, NULL))).containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.setOf(setOf(1, NULL))).containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.setOf(collectionOf(1, NULL))).containsExactly(1, NULL).inOrder();
        MoreTruth.assertThat(BasicIterables.setOf(iterableOf(1, NULL))).containsExactly(1, NULL).inOrder();
    }

    /** {@link BasicIterables#sortedSetOf}  **/

    @Test
    public void sortedSetOf_simple() {
        assertThat(BasicIterables.sortedSetOf()).isEmpty();
        assertThat(BasicIterables.sortedSetOf(1)).containsExactly(1).inOrder();
        assertThat(BasicIterables.sortedSetOf(arrayOf(2, 1, 0))).containsExactly(0, 1, 2).inOrder();
        assertThat(BasicIterables.sortedSetOf(listOf(2, 1, 0))).containsExactly(0, 1, 2).inOrder();
        assertThat(BasicIterables.sortedSetOf(setOf(2, 1, 0))).containsExactly(0, 1, 2).inOrder();
        assertThat(BasicIterables.sortedSetOf(collectionOf(2, 1, 0))).containsExactly(0, 1, 2).inOrder();
        assertThat(BasicIterables.sortedSetOf(iterableOf(2, 1, 0))).containsExactly(0, 1, 2).inOrder();
    }

    /** {@link BasicIterables#newMutableSet}, BasicIterables#mutableSetOf} **/

    @Test
    public void newMutableSet_simple() {
        assertThat(BasicIterables.newMutableSet()).isEmpty();
        assertThat(BasicIterables.newMutableSet(4)).isEmpty();
    }

    @Test
    public void mutableSetOf_simple() {
        assertThat(BasicIterables.mutableSetOf()).isEmpty();
        assertThat(BasicIterables.mutableSetOf(1)).containsExactly(1);
        assertThat(BasicIterables.mutableSetOf(NULL)).containsExactly(NULL);
        assertThat(BasicIterables.mutableSetOf(1, 2, NULL)).containsExactly(1, 2, NULL);

        assertThat(BasicIterables.mutableSetOf(NULL_ARRAY)).isEmpty();
        assertThat(BasicIterables.mutableSetOf(NULL_LIST)).isEmpty();
        assertThat(BasicIterables.mutableSetOf(NULL_SET)).isEmpty();
        assertThat(BasicIterables.mutableSetOf(NULL_COLLECTION)).isEmpty();
        assertThat(BasicIterables.mutableSetOf(NULL_ITERABLE)).isEmpty();

        assertThat(BasicIterables.mutableSetOf(arrayOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableSetOf(listOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableSetOf(setOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableSetOf(collectionOf(1, 2, NULL))).containsExactly(1, 2, NULL);
        assertThat(BasicIterables.mutableSetOf(iterableOf(1, 2, NULL))).containsExactly(1, 2, NULL);
    }

    /** {@link BasicIterables#asList(Iterable)} **/

    @Test
    public void asList_list_same_instance() {
        also(listOf(1, 2, NULL), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
        also(List.of(1, 2), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
        also(new ArrayList<>(), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
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
        assertThat(BasicIterables.asList(iterableOf(NULL))).containsExactly(NULL).inOrder();
    }

    /** {@link BasicIterables#asArrayList(Iterable)} **/

    @Test
    public void asArrayList_list_same_instance() {
        also(new ArrayList<>(), items -> assertThat(BasicIterables.asList(items)).isSameInstanceAs(items));
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

    /** {@link BasicIterables#asSet(Iterable)} **/

    @Test
    public void asSet_set_same_instance() {
        also(setOf(1, 2), items -> assertThat(BasicIterables.asSet(items)).isSameInstanceAs(items));
        also(sortedSetOf(1, 2), items -> assertThat(BasicIterables.asSet(items)).isSameInstanceAs(items));
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

    /** {@link BasicIterables#asHashSet(Iterable)} **/

    @Test
    public void asHashSet_same_instance() {
        also(new HashSet<>(1, 2), items -> assertThat(BasicIterables.asHashSet(items)).isSameInstanceAs(items));
        also(new LinkedHashSet<>(1, 2), items -> assertThat(BasicIterables.asHashSet(items)).isSameInstanceAs(items));
    }

    @Test
    public void asHashSet_collections() {
        assertThat(BasicIterables.asHashSet(listOf())).isEmpty();
        assertThat(BasicIterables.asHashSet(listOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asHashSet(listOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void asHashSet_iterable() {
        assertThat(BasicIterables.asHashSet(iterableOf())).isEmpty();
        assertThat(BasicIterables.asHashSet(iterableOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asHashSet(iterableOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#asLinkedHashSet(Iterable)} **/

    @Test
    public void asLinkedHashSet_same_instance() {
        also(new LinkedHashSet<>(1, 2), items -> assertThat(BasicIterables.asLinkedHashSet(items)).isSameInstanceAs(items));
    }

    @Test
    public void asLinkedHashSet_collections() {
        assertThat(BasicIterables.asLinkedHashSet(listOf())).isEmpty();
        assertThat(BasicIterables.asLinkedHashSet(listOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asLinkedHashSet(listOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void asLinkedHashSet_iterable() {
        assertThat(BasicIterables.asLinkedHashSet(iterableOf())).isEmpty();
        assertThat(BasicIterables.asLinkedHashSet(iterableOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asLinkedHashSet(iterableOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#asCollection(Iterable)} **/

    @Test
    public void asCollection_collection_same_instance() {
        also(listOf(1, 2), items -> assertThat(BasicIterables.asCollection(items)).isSameInstanceAs(items));
        also(setOf(1, 2), items -> assertThat(BasicIterables.asCollection(items)).isSameInstanceAs(items));
        also(sortedSetOf(1, 2), items -> assertThat(BasicIterables.asCollection(items)).isSameInstanceAs(items));
    }

    @Test
    public void asCollection_iterable() {
        assertThat(BasicIterables.asCollection(iterableOf())).isEmpty();
        assertThat(BasicIterables.asCollection(iterableOf(1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.asCollection(iterableOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#forEachToList(Consumer)} **/

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

    /** {@link BasicIterables#forEachZipped(Object[], Object[], BiConsumer)} **/

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

    /** {@link BasicIterables#forEachZipped(Iterable, Iterable, BiConsumer)} **/

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

    /** {@link BasicIterables#sizeOf}  **/

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

    /** {@link BasicIterables#isEmpty}  **/

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

    /** {@link BasicIterables#estimateSize} **/

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

    /** {@link BasicIterables#emptyIfNull}  **/

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

    /** {@link BasicIterables#nullIfEmpty}  **/

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

    /** {@link BasicIterables#getFirst} **/

    @Test
    public void getFirst_simple() {
        assertThat(BasicIterables.getFirst(listOf(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.getFirst(listOf(1), -1)).isEqualTo(1);
        assertThat(BasicIterables.getFirst(listOf(1, 2, 3), -1)).isEqualTo(1);

        assertThat(BasicIterables.getFirst(setOf(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.getFirst(setOf(1), -1)).isEqualTo(1);
        assertThat(BasicIterables.getFirst(setOf(1, 2, 3), -1)).isEqualTo(1);

        assertThat(BasicIterables.getFirst(iterableOf(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.getFirst(iterableOf(1), -1)).isEqualTo(1);
        assertThat(BasicIterables.getFirst(iterableOf(1, 2, 3), -1)).isEqualTo(1);

        assertThat(BasicIterables.getFirst(iteratorOf(), -1)).isEqualTo(-1);
        assertThat(BasicIterables.getFirst(iteratorOf(1), -1)).isEqualTo(1);
        assertThat(BasicIterables.getFirst(iteratorOf(1, 2, 3), -1)).isEqualTo(1);
    }

    /** {@link BasicIterables#replaceContent(Collection, Collection)} **/

    @Test
    public void replaceContent_list_simple() {
        ArrayList<Integer> list = BasicIterables.mutableListOf(1, 2, 3);
        assertThat(BasicIterables.replaceContent(list, listOf(3, 2, 1, 0))).isSameInstanceAs(list);
        assertThat(list).containsExactly(3, 2, 1, 0).inOrder();
    }

    @Test
    public void replaceContent_set_simple() {
        HashSet<Integer> set = setOf(1, 2, 3);
        assertThat(BasicIterables.replaceContent(set, listOf(3, 2, 1, 0))).isSameInstanceAs(set);
        assertThat(set).containsExactly(3, 2, 1, 0).inOrder();
    }

    /** {@link BasicIterables#distinctInPlace(Collection)} **/

    @Test
    public void distinctInPlace_unchanged() {
        ArrayList<Integer> list = BasicIterables.mutableListOf(1, 2, 3);
        assertThat(BasicIterables.distinctInPlace(list)).isSameInstanceAs(list);
        assertThat(list).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void distinctInPlace_replaced() {
        ArrayList<Integer> list = BasicIterables.mutableListOf(1, 1, 1, 2, 1, 1, 2, 2, 2, 1, 2);
        assertThat(BasicIterables.distinctInPlace(list)).isSameInstanceAs(list);
        assertThat(list).containsExactly(1, 2).inOrder();
    }

    /** {@link BasicIterables#isAllDistinct(Iterable)} **/

    @Test
    public void isAllDistinct_distinct() {
        assertThat(BasicIterables.isAllDistinct(listOf())).isTrue();
        assertThat(BasicIterables.isAllDistinct(listOf(1))).isTrue();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 2))).isTrue();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 2, 3))).isTrue();
    }

    @Test
    public void isAllDistinct_duplicates() {
        assertThat(BasicIterables.isAllDistinct(listOf(1, 1))).isFalse();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 1, 1))).isFalse();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 2, 1))).isFalse();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 2, 1, 2))).isFalse();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 2, 3, 3))).isFalse();
        assertThat(BasicIterables.isAllDistinct(listOf(1, 2, 3, 4, 1))).isFalse();
    }

    /** {@link BasicIterables#duplicatesOf(Iterable)} **/

    @Test
    public void duplicatesOf_distinct() {
        assertThat(BasicIterables.duplicatesOf(listOf())).isEmpty();
        assertThat(BasicIterables.duplicatesOf(listOf(1))).isEmpty();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2))).isEmpty();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2, 3))).isEmpty();
    }

    @Test
    public void duplicatesOf_has_duplicates() {
        assertThat(BasicIterables.duplicatesOf(listOf(1, 1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 1, 1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2, 1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2, 1, 2))).containsExactly(1, 2).inOrder();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2, 3, 3))).containsExactly(3).inOrder();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2, 3, 4, 1))).containsExactly(1).inOrder();
        assertThat(BasicIterables.duplicatesOf(listOf(1, 2, 3, 3, 3, 1))).containsExactly(3, 1).inOrder();
    }

    /** {@link BasicIterables#concatToList} **/

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

    /** {@link BasicIterables#prependToList(Object, Iterable)} **/

    @Test
    public void prependToList_simple() {
        assertThat(BasicIterables.prependToList(3, listOf())).containsExactly(3).inOrder();
        assertThat(BasicIterables.prependToList(3, listOf(1, 2))).containsExactly(3, 1, 2).inOrder();
        assertThat(BasicIterables.prependToList(2, listOf(1, null))).containsExactly(2, 1, null).inOrder();
        assertThat(BasicIterables.prependToList(null, listOf(null, null))).containsExactly(null, null, null).inOrder();
    }

    /** {@link BasicIterables#appendToList(Iterable, Object)} **/

    @Test
    public void appendToList_simple() {
        assertThat(BasicIterables.appendToList(listOf(), 3)).containsExactly(3).inOrder();
        assertThat(BasicIterables.appendToList(listOf(1, 2), 3)).containsExactly(1, 2, 3).inOrder();
        assertThat(BasicIterables.appendToList(listOf(1, null), 2)).containsExactly(1, null, 2).inOrder();
        assertThat(BasicIterables.appendToList(listOf(null, null), null)).containsExactly(null, null, null).inOrder();
    }

    /** {@link BasicIterables#isImmutable(Collection)} **/

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

    @Test
    public void isImmutable_basics_collections() {
        assertThat(BasicIterables.isImmutable(Array.of(1, 2, 3))).isFalse();
        assertThat(BasicIterables.isImmutable(ImmutableArray.copyOf(1, 2, 3))).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableArrayList.of(1, 2, 3))).isTrue();
        assertThat(BasicIterables.isImmutable(ImmutableLinkedHashSet.of(1, 2, 3))).isTrue();
    }
}
