package io.spbx.util.base.lang;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.listOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("fast")
@SuppressWarnings({ "ObviousNullCheck", "ConstantValue" })
public class BasicNullsTest {
    private static final String NULL = null;
    private static final Supplier<String> WILL_THROW = () -> fail("Must not be called");

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
        assertThat(BasicNulls.firstNonNull("foo", WILL_THROW)).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_two_suppliers() {
        assertThat(BasicNulls.<String>firstNonNull(() -> "foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(() -> "foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(() -> NULL, () -> "bar")).isEqualTo("bar");
        assertThrows(NullPointerException.class, () -> BasicNulls.<String>firstNonNull(() -> NULL, () -> NULL));
        assertThat(BasicNulls.<String>firstNonNull(() -> "foo", WILL_THROW)).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_two_suppliers_with_default() {
        assertThat(BasicNulls.firstNonNull(() -> "foo", () -> "bar", "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(() -> "foo", () -> NULL, "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(() -> NULL, () -> "bar", "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNull(() -> NULL, () -> NULL, "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNull(() -> "foo", WILL_THROW, "def")).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_iterable() {
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> "foo"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> "foo", () -> "bar"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNull(listOf(() -> NULL, () -> "bar"))).isEqualTo("bar");
        assertThrows(NullPointerException.class, () -> BasicNulls.<String>firstNonNull(listOf(() -> NULL, () -> NULL)));
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo", WILL_THROW))).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_iterable_with_default() {
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(listOf(() -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo", () -> "bar"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNull(listOf(() -> NULL, () -> "bar"), "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNull(listOf(() -> NULL, () -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNull(listOf(() -> "foo", WILL_THROW), "def")).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_of_vararg() {
        assertThat(BasicNulls.firstNonNull(() -> NULL, () -> NULL, () -> NULL, () -> "foo")).isEqualTo("foo");
        assertThrows(NullPointerException.class, () -> BasicNulls.firstNonNull(() -> NULL, () -> NULL, () -> NULL, () -> NULL));
        assertThat(BasicNulls.firstNonNull(() -> "foo", WILL_THROW, WILL_THROW, WILL_THROW)).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExists_of_two_obj() {
        assertThat(BasicNulls.firstNonNullIfExists("foo", "bar")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists("foo", NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(NULL, "bar")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExists(NULL, NULL)).isEqualTo(NULL);
    }

    @Test
    public void firstNonNullIfExists_of_obj_and_supplier() {
        assertThat(BasicNulls.firstNonNullIfExists("foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists("foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(NULL, () -> "bar")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExists(NULL, () -> NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExists("foo", WILL_THROW)).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExists_of_two_suppliers() {
        assertThat(BasicNulls.<String>firstNonNullIfExists(() -> "foo", () -> "bar")).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExists(() -> "foo", () -> NULL)).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExists(() -> NULL, () -> "bar")).isEqualTo("bar");
        assertThat(BasicNulls.<String>firstNonNullIfExists(() -> NULL, () -> NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.<String>firstNonNullIfExists(() -> "foo", WILL_THROW)).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExists_of_two_suppliers_with_default() {
        assertThat(BasicNulls.firstNonNullIfExists(() -> "foo", () -> "bar", "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(() -> "foo", () -> "bar", NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(() -> "foo", () -> NULL, "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(() -> NULL, () -> "bar", "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExists(() -> NULL, () -> NULL, "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNullIfExists(() -> NULL, () -> NULL, NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExists(() -> "foo", WILL_THROW, NULL)).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExists_of_iterable() {
        assertThat(BasicNulls.<String>firstNonNullIfExists(listOf(() -> "foo"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExists(listOf(() -> "foo", () -> "bar"))).isEqualTo("foo");
        assertThat(BasicNulls.<String>firstNonNullIfExists(listOf(() -> NULL, () -> "bar"))).isEqualTo("bar");
        assertThat(BasicNulls.<String>firstNonNullIfExists(listOf(() -> NULL, () -> NULL))).isEqualTo(NULL);
        assertThat(BasicNulls.<String>firstNonNullIfExists(listOf(() -> "foo", WILL_THROW))).isEqualTo("foo");
    }

    @Test
    public void firstNonNullIfExists_of_iterable_with_default() {
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> "foo"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> "foo"), NULL)).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> NULL), NULL)).isEqualTo(NULL);

        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> "foo", () -> "bar"), "def")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> NULL, () -> "bar"), "def")).isEqualTo("bar");
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> NULL, () -> NULL), "def")).isEqualTo("def");
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> NULL, () -> NULL), NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExists(listOf(() -> "foo", WILL_THROW), NULL)).isEqualTo("foo");
    }

    @Test
    public void firstNonNull_IfExists_of_vararg() {
        assertThat(BasicNulls.firstNonNullIfExists(() -> NULL, () -> NULL, () -> NULL, () -> "foo")).isEqualTo("foo");
        assertThat(BasicNulls.firstNonNullIfExists(() -> NULL, () -> NULL, () -> NULL, () -> NULL)).isEqualTo(NULL);
        assertThat(BasicNulls.firstNonNullIfExists(() -> "foo", WILL_THROW, WILL_THROW, WILL_THROW)).isEqualTo("foo");
    }
}
