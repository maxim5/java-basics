package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.Predicates.equalTo;

@Tag("fast")
public class BasicArraysTest {
    private static final Integer NULL = null;

    /** {@link BasicArrays#indexOf} **/

    @Test
    public void indexOf_value_not_found() {
        assertThat(BasicArrays.indexOf(new Integer[0], 0)).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1 }, 0)).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2 }, 0)).isEqualTo(-1);

        assertThat(BasicArrays.indexOf(new Integer[0], NULL)).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1 }, NULL)).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2 }, NULL)).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { NULL }, 0)).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { NULL, NULL }, 0)).isEqualTo(-1);
    }

    @Test
    public void indexOf_value_found() {
        assertThat(BasicArrays.indexOf(new Integer[] { 0 }, 0)).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { 0, 0 }, 0)).isEqualTo(0);

        assertThat(BasicArrays.indexOf(new Integer[] { 0, 1, 2 }, 0)).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 0, 2 }, 0)).isEqualTo(1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2, 0 }, 0)).isEqualTo(2);

        assertThat(BasicArrays.indexOf(new Integer[] { NULL }, NULL)).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { NULL, NULL }, NULL)).isEqualTo(0);

        assertThat(BasicArrays.indexOf(new Integer[] { NULL, 1, 2 }, NULL)).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, NULL, 2 }, NULL)).isEqualTo(1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2, NULL }, NULL)).isEqualTo(2);
    }

    @Test
    public void indexOf_predicate_not_found() {
        assertThat(BasicArrays.indexOf(new Integer[0], equalTo(0))).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1 }, equalTo(0))).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2 }, equalTo(0))).isEqualTo(-1);

        assertThat(BasicArrays.indexOf(new Integer[0], equalTo(NULL))).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { NULL }, equalTo(0))).isEqualTo(-1);
        assertThat(BasicArrays.indexOf(new Integer[] { NULL, NULL }, equalTo(0))).isEqualTo(-1);
    }

    @Test
    public void indexOf_predicate_found() {
        assertThat(BasicArrays.indexOf(new Integer[] { 0 }, equalTo(0))).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { 0, 0 }, equalTo(0))).isEqualTo(0);

        assertThat(BasicArrays.indexOf(new Integer[] { 0, 1, 2 }, equalTo(0))).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 0, 2 }, equalTo(0))).isEqualTo(1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2, 0 }, equalTo(0))).isEqualTo(2);

        assertThat(BasicArrays.indexOf(new Integer[] { NULL }, equalTo(NULL))).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { NULL, NULL }, equalTo(NULL))).isEqualTo(0);

        assertThat(BasicArrays.indexOf(new Integer[] { NULL, 1, 2 }, equalTo(NULL))).isEqualTo(0);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, NULL, 2 }, equalTo(NULL))).isEqualTo(1);
        assertThat(BasicArrays.indexOf(new Integer[] { 1, 2, NULL }, equalTo(NULL))).isEqualTo(2);
    }

    /** {@link BasicArrays#lastIndexOf} **/

    @Test
    public void lastIndexOf_value_not_found() {
        assertThat(BasicArrays.lastIndexOf(new Integer[0], 0)).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1 }, 0)).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2 }, 0)).isEqualTo(-1);

        assertThat(BasicArrays.lastIndexOf(new Integer[0], NULL)).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1 }, NULL)).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2 }, NULL)).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL }, 0)).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL, NULL }, 0)).isEqualTo(-1);
    }

    @Test
    public void lastIndexOf_value_found() {
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 0 }, 0)).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 0, 0 }, 0)).isEqualTo(1);

        assertThat(BasicArrays.lastIndexOf(new Integer[] { 0, 1, 2 }, 0)).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 0, 2 }, 0)).isEqualTo(1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2, 0 }, 0)).isEqualTo(2);

        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL }, NULL)).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL, NULL }, NULL)).isEqualTo(1);

        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL, 1, 2 }, NULL)).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, NULL, 2 }, NULL)).isEqualTo(1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2, NULL }, NULL)).isEqualTo(2);
    }

    @Test
    public void lastIndexOf_predicate_not_found() {
        assertThat(BasicArrays.lastIndexOf(new Integer[0], equalTo(0))).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1 }, equalTo(0))).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2 }, equalTo(0))).isEqualTo(-1);

        assertThat(BasicArrays.lastIndexOf(new Integer[0], equalTo(NULL))).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2 }, equalTo(NULL))).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL }, equalTo(0))).isEqualTo(-1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL, NULL }, equalTo(0))).isEqualTo(-1);
    }

    @Test
    public void lastIndexOf_predicate_found() {
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 0 }, equalTo(0))).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 0, 0 }, equalTo(0))).isEqualTo(1);

        assertThat(BasicArrays.lastIndexOf(new Integer[] { 0, 1, 2 }, equalTo(0))).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 0, 2 }, equalTo(0))).isEqualTo(1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2, 0 }, equalTo(0))).isEqualTo(2);

        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL }, equalTo(NULL))).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL, NULL }, equalTo(NULL))).isEqualTo(1);

        assertThat(BasicArrays.lastIndexOf(new Integer[] { NULL, 1, 2 }, equalTo(NULL))).isEqualTo(0);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, NULL, 2 }, equalTo(NULL))).isEqualTo(1);
        assertThat(BasicArrays.lastIndexOf(new Integer[] { 1, 2, NULL }, equalTo(NULL))).isEqualTo(2);
    }

    /** {@link BasicArrays#contains} **/

    @Test
    public void contains_not_found() {
        assertThat(BasicArrays.contains(new Integer[0], 0)).isFalse();
        assertThat(BasicArrays.contains(new Integer[] { 1 }, 0)).isFalse();
        assertThat(BasicArrays.contains(new Integer[] { 1, 2 }, 0)).isFalse();

        assertThat(BasicArrays.contains(new Integer[0], NULL)).isFalse();
        assertThat(BasicArrays.contains(new Integer[] { 1 }, NULL)).isFalse();
        assertThat(BasicArrays.contains(new Integer[] { 1, 2 }, NULL)).isFalse();
        assertThat(BasicArrays.contains(new Integer[] { NULL }, 0)).isFalse();
        assertThat(BasicArrays.contains(new Integer[] { NULL, NULL }, 0)).isFalse();
    }

    @Test
    public void contains_value_found() {
        assertThat(BasicArrays.contains(new Integer[] { 0 }, 0)).isTrue();
        assertThat(BasicArrays.contains(new Integer[] { 0, 0 }, 0)).isTrue();

        assertThat(BasicArrays.contains(new Integer[] { 0, 1, 2 }, 0)).isTrue();
        assertThat(BasicArrays.contains(new Integer[] { 1, 0, 2 }, 0)).isTrue();
        assertThat(BasicArrays.contains(new Integer[] { 1, 2, 0 }, 0)).isTrue();

        assertThat(BasicArrays.contains(new Integer[] { NULL }, NULL)).isTrue();
        assertThat(BasicArrays.contains(new Integer[] { NULL, NULL }, NULL)).isTrue();

        assertThat(BasicArrays.contains(new Integer[] { NULL, 1, 2 }, NULL)).isTrue();
        assertThat(BasicArrays.contains(new Integer[] { 1, NULL, 2 }, NULL)).isTrue();
        assertThat(BasicArrays.contains(new Integer[] { 1, 2, NULL }, NULL)).isTrue();
    }

    /** {@link BasicArrays#reverse(Object[])} **/

    @Test
    public void reverse_simple() {
        assertThat(BasicArrays.reverse(new Integer[0])).asList().isEmpty();
        assertThat(BasicArrays.reverse(new Integer[] { 1 })).asList().containsExactly(1);
        assertThat(BasicArrays.reverse(new Integer[] { 1, 2 })).asList().containsExactly(2, 1);
        assertThat(BasicArrays.reverse(new Integer[] { 1, 2, 3 })).asList().containsExactly(3, 2, 1);

        assertThat(BasicArrays.reverse(new Integer[] { NULL })).asList().containsExactly(NULL);
        assertThat(BasicArrays.reverse(new Integer[] { NULL, NULL })).asList().containsExactly(NULL, NULL);
        assertThat(BasicArrays.reverse(new Integer[] { NULL, NULL, NULL })).asList().containsExactly(NULL, NULL, NULL);
    }

    /** {@link BasicArrays#fill} **/

    @Test
    public void fill_value_simple() {
        assertThat(BasicArrays.fill(new Integer[0], 777)).asList().isEmpty();
        assertThat(BasicArrays.fill(new Integer[1], 777)).asList().containsExactly(777);
        assertThat(BasicArrays.fill(new Integer[3], 777)).asList().containsExactly(777, 777, 777);

        assertThat(BasicArrays.fill(new Integer[0], NULL)).asList().isEmpty();
        assertThat(BasicArrays.fill(new Integer[1], NULL)).asList().containsExactly(NULL);
        assertThat(BasicArrays.fill(new Integer[3], NULL)).asList().containsExactly(NULL, NULL, NULL);
    }

    @Test
    public void fill_func_simple() {
        assertThat(BasicArrays.fill(new Integer[0], i -> i)).asList().isEmpty();
        assertThat(BasicArrays.fill(new Integer[1], i -> i)).asList().containsExactly(0);
        assertThat(BasicArrays.fill(new Integer[3], i -> i)).asList().containsExactly(0, 1, 2);

        assertThat(BasicArrays.fill(new Integer[0], i -> NULL)).asList().isEmpty();
        assertThat(BasicArrays.fill(new Integer[1], i -> NULL)).asList().containsExactly(NULL);
        assertThat(BasicArrays.fill(new Integer[3], i -> NULL)).asList().containsExactly(NULL, NULL, NULL);
    }
}
