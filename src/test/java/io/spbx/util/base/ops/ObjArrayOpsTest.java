package io.spbx.util.base.ops;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.Predicates.equalTo;
import static org.junit.Assert.assertThrows;

@Tag("fast")
public class ObjArrayOpsTest {
    private static final Integer NULL = null;

    /** {@link ObjArrayOps#indexOf} **/

    @Test
    public void indexOf_value_not_found() {
        assertThat(ObjArrayOps.indexOf(new Integer[0], 0)).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1 }, 0)).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2 }, 0)).isEqualTo(-1);

        assertThat(ObjArrayOps.indexOf(new Integer[0], NULL)).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1 }, NULL)).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2 }, NULL)).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL }, 0)).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL, NULL }, 0)).isEqualTo(-1);
    }

    @Test
    public void indexOf_value_found() {
        assertThat(ObjArrayOps.indexOf(new Integer[] { 0 }, 0)).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 0, 0 }, 0)).isEqualTo(0);

        assertThat(ObjArrayOps.indexOf(new Integer[] { 0, 1, 2 }, 0)).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 0, 2 }, 0)).isEqualTo(1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, 0 }, 0)).isEqualTo(2);

        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL }, NULL)).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL, NULL }, NULL)).isEqualTo(0);

        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL, 1, 2 }, NULL)).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, NULL, 2 }, NULL)).isEqualTo(1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, NULL }, NULL)).isEqualTo(2);
    }

    @Test
    public void indexOf_predicate_not_found() {
        assertThat(ObjArrayOps.indexOf(new Integer[0], equalTo(0))).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1 }, equalTo(0))).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2 }, equalTo(0))).isEqualTo(-1);

        assertThat(ObjArrayOps.indexOf(new Integer[0], equalTo(NULL))).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL }, equalTo(0))).isEqualTo(-1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL, NULL }, equalTo(0))).isEqualTo(-1);
    }

    @Test
    public void indexOf_predicate_found() {
        assertThat(ObjArrayOps.indexOf(new Integer[] { 0 }, equalTo(0))).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 0, 0 }, equalTo(0))).isEqualTo(0);

        assertThat(ObjArrayOps.indexOf(new Integer[] { 0, 1, 2 }, equalTo(0))).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 0, 2 }, equalTo(0))).isEqualTo(1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, 0 }, equalTo(0))).isEqualTo(2);

        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL }, equalTo(NULL))).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL, NULL }, equalTo(NULL))).isEqualTo(0);

        assertThat(ObjArrayOps.indexOf(new Integer[] { NULL, 1, 2 }, equalTo(NULL))).isEqualTo(0);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, NULL, 2 }, equalTo(NULL))).isEqualTo(1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, NULL }, equalTo(NULL))).isEqualTo(2);
    }

    @Test
    public void indexOf_predicate_subarray() {
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, 0 }, i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 0, 0 }, i -> i == 0, 0, -1, -777)).isEqualTo(1);

        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, -2, -1, -777)).isEqualTo(-777);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 1, 0, 3 }, i -> i == 0, -2, -1, -777)).isEqualTo(1);
        assertThat(ObjArrayOps.indexOf(new Integer[] { 0, 2, 3 }, i -> i == 0, -2, -1, -777)).isEqualTo(-777);

        assertThrows(AssertionError.class, () -> ObjArrayOps.indexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, -1, 0, -777));
        assertThrows(AssertionError.class, () -> ObjArrayOps.indexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, -1, -2, -777));
        assertThrows(AssertionError.class, () -> ObjArrayOps.indexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, 0, 0, 0));
    }

    /** {@link ObjArrayOps#lastIndexOf} **/

    @Test
    public void lastIndexOf_value_not_found() {
        assertThat(ObjArrayOps.lastIndexOf(new Integer[0], 0)).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1 }, 0)).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2 }, 0)).isEqualTo(-1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[0], NULL)).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1 }, NULL)).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2 }, NULL)).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL }, 0)).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL, NULL }, 0)).isEqualTo(-1);
    }

    @Test
    public void lastIndexOf_value_found() {
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0 }, 0)).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0, 0 }, 0)).isEqualTo(1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0, 1, 2 }, 0)).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 0, 2 }, 0)).isEqualTo(1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 0 }, 0)).isEqualTo(2);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL }, NULL)).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL, NULL }, NULL)).isEqualTo(1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL, 1, 2 }, NULL)).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, NULL, 2 }, NULL)).isEqualTo(1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, NULL }, NULL)).isEqualTo(2);
    }

    @Test
    public void lastIndexOf_predicate_not_found() {
        assertThat(ObjArrayOps.lastIndexOf(new Integer[0], equalTo(0))).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1 }, equalTo(0))).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2 }, equalTo(0))).isEqualTo(-1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[0], equalTo(NULL))).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL }, equalTo(0))).isEqualTo(-1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL, NULL }, equalTo(0))).isEqualTo(-1);
    }

    @Test
    public void lastIndexOf_predicate_found() {
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0 }, equalTo(0))).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0, 0 }, equalTo(0))).isEqualTo(1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0, 1, 2 }, equalTo(0))).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 0, 2 }, equalTo(0))).isEqualTo(1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 0 }, equalTo(0))).isEqualTo(2);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL }, equalTo(NULL))).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL, NULL }, equalTo(NULL))).isEqualTo(1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { NULL, 1, 2 }, equalTo(NULL))).isEqualTo(0);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, NULL, 2 }, equalTo(NULL))).isEqualTo(1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, NULL }, equalTo(NULL))).isEqualTo(2);
    }

    @Test
    public void lastIndexOf_predicate_subarray() {
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 0 }, i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 0, 0 }, i -> i == 0, 0, -1, -777)).isEqualTo(1);

        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, -2, -1, -777)).isEqualTo(-777);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 1, 0, 3 }, i -> i == 0, -2, -1, -777)).isEqualTo(1);
        assertThat(ObjArrayOps.lastIndexOf(new Integer[] { 0, 2, 3 }, i -> i == 0, -2, -1, -777)).isEqualTo(-777);

        assertThrows(AssertionError.class, () -> ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, -1, 0, -777));
        assertThrows(AssertionError.class, () -> ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, -1, -2, -777));
        assertThrows(AssertionError.class, () -> ObjArrayOps.lastIndexOf(new Integer[] { 1, 2, 3 }, i -> i == 0, 0, 0, 0));
    }

    /** {@link ObjArrayOps#contains} **/

    @Test
    public void contains_not_found() {
        assertThat(ObjArrayOps.contains(new Integer[0], 0)).isFalse();
        assertThat(ObjArrayOps.contains(new Integer[] { 1 }, 0)).isFalse();
        assertThat(ObjArrayOps.contains(new Integer[] { 1, 2 }, 0)).isFalse();

        assertThat(ObjArrayOps.contains(new Integer[0], NULL)).isFalse();
        assertThat(ObjArrayOps.contains(new Integer[] { 1 }, NULL)).isFalse();
        assertThat(ObjArrayOps.contains(new Integer[] { 1, 2 }, NULL)).isFalse();
        assertThat(ObjArrayOps.contains(new Integer[] { NULL }, 0)).isFalse();
        assertThat(ObjArrayOps.contains(new Integer[] { NULL, NULL }, 0)).isFalse();
    }

    @Test
    public void contains_value_found() {
        assertThat(ObjArrayOps.contains(new Integer[] { 0 }, 0)).isTrue();
        assertThat(ObjArrayOps.contains(new Integer[] { 0, 0 }, 0)).isTrue();

        assertThat(ObjArrayOps.contains(new Integer[] { 0, 1, 2 }, 0)).isTrue();
        assertThat(ObjArrayOps.contains(new Integer[] { 1, 0, 2 }, 0)).isTrue();
        assertThat(ObjArrayOps.contains(new Integer[] { 1, 2, 0 }, 0)).isTrue();

        assertThat(ObjArrayOps.contains(new Integer[] { NULL }, NULL)).isTrue();
        assertThat(ObjArrayOps.contains(new Integer[] { NULL, NULL }, NULL)).isTrue();

        assertThat(ObjArrayOps.contains(new Integer[] { NULL, 1, 2 }, NULL)).isTrue();
        assertThat(ObjArrayOps.contains(new Integer[] { 1, NULL, 2 }, NULL)).isTrue();
        assertThat(ObjArrayOps.contains(new Integer[] { 1, 2, NULL }, NULL)).isTrue();
    }

    /** {@link ObjArrayOps#reverse(Object[])} **/

    @Test
    public void reverse_simple() {
        assertThat(ObjArrayOps.reverse(new Integer[0])).asList().isEmpty();
        assertThat(ObjArrayOps.reverse(new Integer[] { 1 })).asList().containsExactly(1);
        assertThat(ObjArrayOps.reverse(new Integer[] { 1, 2 })).asList().containsExactly(2, 1);
        assertThat(ObjArrayOps.reverse(new Integer[] { 1, 2, 3 })).asList().containsExactly(3, 2, 1);

        assertThat(ObjArrayOps.reverse(new Integer[] { NULL })).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.reverse(new Integer[] { NULL, NULL })).asList().containsExactly(NULL, NULL);
        assertThat(ObjArrayOps.reverse(new Integer[] { NULL, NULL, NULL })).asList().containsExactly(NULL, NULL, NULL);
    }

    /** {@link ObjArrayOps#fill} **/

    @Test
    public void fill_value_simple() {
        assertThat(ObjArrayOps.fill(new Integer[0], 777)).asList().isEmpty();
        assertThat(ObjArrayOps.fill(new Integer[1], 777)).asList().containsExactly(777);
        assertThat(ObjArrayOps.fill(new Integer[3], 777)).asList().containsExactly(777, 777, 777);

        assertThat(ObjArrayOps.fill(new Integer[0], NULL)).asList().isEmpty();
        assertThat(ObjArrayOps.fill(new Integer[1], NULL)).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.fill(new Integer[3], NULL)).asList().containsExactly(NULL, NULL, NULL);
    }

    @Test
    public void fill_func_simple() {
        assertThat(ObjArrayOps.fill(new Integer[0], i -> i)).asList().isEmpty();
        assertThat(ObjArrayOps.fill(new Integer[1], i -> i)).asList().containsExactly(0);
        assertThat(ObjArrayOps.fill(new Integer[3], i -> i)).asList().containsExactly(0, 1, 2);

        assertThat(ObjArrayOps.fill(new Integer[0], i -> NULL)).asList().isEmpty();
        assertThat(ObjArrayOps.fill(new Integer[1], i -> NULL)).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.fill(new Integer[3], i -> NULL)).asList().containsExactly(NULL, NULL, NULL);
    }

    /** {@link ObjArrayOps#map} **/

    @Test
    public void map_unary_simple() {
        assertThat(ObjArrayOps.map(new Integer[0], a -> a + 1)).asList().isEmpty();
        assertThat(ObjArrayOps.map(new Integer[] { 1 }, a -> a + 1)).asList().containsExactly(2);
        assertThat(ObjArrayOps.map(new Integer[] { 1, 2 }, a -> a + 1)).asList().containsExactly(2, 3);

        assertThat(ObjArrayOps.map(new Integer[0], a -> NULL)).asList().isEmpty();
        assertThat(ObjArrayOps.map(new Integer[] { 1 }, a -> NULL)).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.map(new Integer[] { 1, 2 }, a -> NULL)).asList().containsExactly(NULL, NULL);
    }

    @Test
    public void map_binary_simple() {
        assertThat(ObjArrayOps.map(new Integer[0], (i, a) -> i)).asList().isEmpty();
        assertThat(ObjArrayOps.map(new Integer[1], (i, a) -> i)).asList().containsExactly(0);
        assertThat(ObjArrayOps.map(new Integer[3], (i, a) -> i)).asList().containsExactly(0, 1, 2);

        assertThat(ObjArrayOps.map(new Integer[0], (i, a) -> NULL)).asList().isEmpty();
        assertThat(ObjArrayOps.map(new Integer[1], (i, a) -> NULL)).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.map(new Integer[3], (i, a) -> NULL)).asList().containsExactly(NULL, NULL, NULL);
    }

    /** {@link ObjArrayOps#concat(Object[], Object[])} **/

    @Test
    public void concat_simple() {
        assertThat(ObjArrayOps.concat(new Integer[0], new Integer[0])).asList().isEmpty();
        assertThat(ObjArrayOps.concat(new Integer[] { 1 }, new Integer[0])).asList().containsExactly(1);
        assertThat(ObjArrayOps.concat(new Integer[] { 1, 2 }, new Integer[0])).asList().containsExactly(1, 2);
        assertThat(ObjArrayOps.concat(new Integer[0], new Integer[] { 1 })).asList().containsExactly(1);
        assertThat(ObjArrayOps.concat(new Integer[0], new Integer[] { 1, 2 })).asList().containsExactly(1, 2);
        assertThat(ObjArrayOps.concat(new Integer[] { 1, 2 }, new Integer[] { 3 })).asList().containsExactly(1, 2, 3);

        assertThat(ObjArrayOps.concat(new Integer[] { NULL }, new Integer[0])).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.concat(new Integer[] { 1, NULL }, new Integer[0])).asList().containsExactly(1, NULL);
        assertThat(ObjArrayOps.concat(new Integer[0], new Integer[] { NULL })).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.concat(new Integer[0], new Integer[] { 1, NULL })).asList().containsExactly(1, NULL);
        assertThat(ObjArrayOps.concat(new Integer[] { NULL }, new Integer[] { NULL })).asList().containsExactly(NULL, NULL);
    }

    /** {@link ObjArrayOps#append(Object[], Object)} **/

    @Test
    public void append_simple() {
        assertThat(ObjArrayOps.append(new Integer[0], 7)).asList().containsExactly(7);
        assertThat(ObjArrayOps.append(new Integer[] { 1 }, 7)).asList().containsExactly(1, 7);
        assertThat(ObjArrayOps.append(new Integer[] { 1, 2 }, 7)).asList().containsExactly(1, 2, 7);

        assertThat(ObjArrayOps.append(new Integer[0], NULL)).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.append(new Integer[] { 1 }, NULL)).asList().containsExactly(1, NULL);
        assertThat(ObjArrayOps.append(new Integer[] { 1, 2 }, NULL)).asList().containsExactly(1, 2, NULL);
    }

    /** {@link ObjArrayOps#prepend(Object, Object[])} **/

    @Test
    public void prepend_simple() {
        assertThat(ObjArrayOps.prepend(7, new Integer[0])).asList().containsExactly(7);
        assertThat(ObjArrayOps.prepend(7, new Integer[] { 1 })).asList().containsExactly(7, 1);
        assertThat(ObjArrayOps.prepend(7, new Integer[] { 1, 2 })).asList().containsExactly(7, 1, 2);

        assertThat(ObjArrayOps.prepend(NULL, new Integer[0])).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.prepend(NULL, new Integer[] { 1 })).asList().containsExactly(NULL, 1);
        assertThat(ObjArrayOps.prepend(NULL, new Integer[] { 1, 2 })).asList().containsExactly(NULL, 1, 2);
    }
    
    /** {@link ObjArrayOps#slice} **/

    @Test
    public void slice_simple() {
        assertThat(ObjArrayOps.slice(new Integer[0], 0, 0)).asList().isEmpty();
        assertThat(ObjArrayOps.slice(new Integer[] { 1 }, 0, 0)).asList().isEmpty();
        assertThat(ObjArrayOps.slice(new Integer[] { 1 }, 1, 1)).asList().isEmpty();
        assertThat(ObjArrayOps.slice(new Integer[] { 1 }, 0, 1)).asList().containsExactly(1);

        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 1, 2)).asList().containsExactly(2);
        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 1, 3)).asList().containsExactly(2, 3);
        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 0, 2)).asList().containsExactly(1, 2);
        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 0, 1)).asList().containsExactly(1);

        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 0)).asList().containsExactly(1, 2, 3);
        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 1)).asList().containsExactly(2, 3);
        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 2)).asList().containsExactly(3);
        assertThat(ObjArrayOps.slice(new Integer[] { 1, 2, 3 }, 3)).asList().isEmpty();

        assertThat(ObjArrayOps.slice(new Integer[3], 1, 2)).asList().containsExactly(NULL);
        assertThat(ObjArrayOps.slice(new Integer[3], 1, 3)).asList().containsExactly(NULL, NULL);
        assertThat(ObjArrayOps.slice(new Integer[3], 0, 2)).asList().containsExactly(NULL, NULL);
        assertThat(ObjArrayOps.slice(new Integer[3], 0, 1)).asList().containsExactly(NULL);
    }

    /** {@link ObjArrayOps#ensureCapacity(Object[], int)} **/

    @Test
    public void ensureCapacity_simple() {
        assertThat(ObjArrayOps.ensureCapacity(new Integer[0], 0)).asList().isEmpty();
        assertThat(ObjArrayOps.ensureCapacity(new Integer[0], 1)).asList().containsExactly(NULL);

        assertThat(ObjArrayOps.ensureCapacity(new Integer[] { 1, 2, 3 }, 1)).asList().containsExactly(1, 2, 3);
        assertThat(ObjArrayOps.ensureCapacity(new Integer[] { 1, 2, 3 }, 2)).asList().containsExactly(1, 2, 3);
        assertThat(ObjArrayOps.ensureCapacity(new Integer[] { 1, 2, 3 }, 3)).asList().containsExactly(1, 2, 3);
        assertThat(ObjArrayOps.ensureCapacity(new Integer[] { 1, 2, 3 }, 4)).asList().containsExactly(1, 2, 3, NULL);
        assertThat(ObjArrayOps.ensureCapacity(new Integer[] { 1, 2, 3 }, 5)).asList().containsExactly(1, 2, 3, NULL, NULL);
    }

}
