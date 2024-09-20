package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.listOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("fast")
@SuppressWarnings({ "ObviousNullCheck", "ConstantValue" })
public class BasicNullsTest {
    private static final String NULL = null;

    @Test
    public void firstNonNull_of_two_obj() {
        assertThat(BasicNulls.firstNonNull("foo", "bar")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull("foo", NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(NULL, "bar")).isEqualTo("bar");
        assertThrows(NullPointerException.class, () -> BasicNulls.firstNonNull(NULL, NULL));
    }

    @Test
    public void firstNonNull_of_obj_and_supplier() {
        assertThat(BasicNulls.firstNonNull("foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull("foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(NULL, () -> "bar")).isEqualTo("bar");
        assertThrows(NullPointerException.class, () -> BasicNulls.firstNonNull(NULL, () -> NULL));
        assertThat(BasicNulls.firstNonNull("foo", () -> fail("Must not be called"))).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_two_suppliers() {
        assertThat(BasicNulls.<String>firstNonNull(() -> "foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(() -> "foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(() -> NULL, () -> "bar")).isEqualTo("bar");
        assertThrows(NullPointerException.class, () -> BasicNulls.<String>firstNonNull(() -> NULL, () -> NULL));
        assertThat(BasicNulls.<String>firstNonNull(() -> "foo", () -> fail("Must not be called"))).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_two_suppliers_with_default() {
        assertThat(BasicNulls.firstNonNull(() -> "foo", () -> "bar", "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(() -> "foo", () -> NULL, "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(() -> NULL, () -> "bar", "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNull(() -> NULL, () -> NULL, "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNull(() -> "foo", () -> fail("Must not be called"), "def")).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_iterable() {
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> "foo"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> "foo", () -> "bar"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> NULL, () -> "bar"))).isEqualTo("bar");
        assertThrows(NullPointerException.class, () -> BasicNulls.<String>firstNonNull(listOf(() -> NULL, () -> NULL)));
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> "foo", () -> fail("Must not be called")))).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_iterable_with_default() {
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(listOf(() -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo", () -> "bar"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(listOf(() -> NULL, () -> "bar"), "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNull(listOf(() -> NULL, () -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo", () -> fail("Must not be called")), "def")).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExist_of_two_obj() {
        assertThat(BasicNulls.firstNonNullIfExist("foo", "bar")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist("foo", NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(NULL, "bar")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExist(NULL, NULL)).isEqualTo(NULL);
    }

    @Test
    public void firstNonNullIfExist_of_obj_and_supplier() {
        assertThat(BasicNulls.firstNonNullIfExist("foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist("foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(NULL, () -> "bar")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExist(NULL, () -> NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExist("foo", () -> fail("Must not be called"))).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExist_of_two_suppliers() {
        assertThat(BasicNulls.<String>firstNonNullIfExist(() -> "foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExist(() -> "foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExist(() -> NULL, () -> "bar")).isEqualTo("bar");
        assertThat(BasicNulls.<String>firstNonNullIfExist(() -> NULL, () -> NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.<String>firstNonNullIfExist(() -> "foo", () -> fail("Must not be called"))).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExist_of_two_suppliers_with_default() {
        assertThat(BasicNulls.firstNonNullIfExist(() -> "foo", () -> "bar", "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(() -> "foo", () -> "bar", NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(() -> "foo", () -> NULL, "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(() -> NULL, () -> "bar", "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExist(() -> NULL, () -> NULL, "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNullIfExist(() -> NULL, () -> NULL, NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExist(() -> "foo", () -> fail("Must not be called"), NULL)).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExist_of_iterable() {
        assertThat(BasicNulls.<String>firstNonNullIfExist(listOf(() -> "foo"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExist(listOf(() -> "foo", () -> "bar"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExist(listOf(() -> NULL, () -> "bar"))).isEqualTo("bar");
        assertThat(BasicNulls.<String>firstNonNullIfExist(listOf(() -> NULL, () -> NULL))).isEqualTo(NULL);
        assertThat(BasicNulls.<String>firstNonNullIfExist(listOf(() -> "foo", () -> fail("Must not be called")))).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExist_of_iterable_with_default() {
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> "foo"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> "foo"), NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> NULL), NULL)).isEqualTo(NULL);

        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> "foo", () -> "bar"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> NULL, () -> "bar"), "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> NULL, () -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> NULL, () -> NULL), NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExist(listOf(() -> "foo", () -> fail("Must not be called")), NULL)).isEqualTo("foo");
    }
}
