package io.spbx.util.props;

import io.spbx.util.base.lang.Maybe;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class StandardPropertiesTest {
    @Test
    public void getEnum_simple() {
        StandardProperties properties = key -> key.isEmpty() ? null : key;

        assertThat(properties.getEnum("true", Maybe.UNKNOWN)).isEqualTo(Maybe.TRUE);
        assertThat(properties.getEnum("True", Maybe.UNKNOWN)).isEqualTo(Maybe.TRUE);
        assertThat(properties.getEnum("TRUE", Maybe.UNKNOWN)).isEqualTo(Maybe.TRUE);

        assertThat(properties.getEnum("false", Maybe.UNKNOWN)).isEqualTo(Maybe.FALSE);
        assertThat(properties.getEnum("False", Maybe.UNKNOWN)).isEqualTo(Maybe.FALSE);
        assertThat(properties.getEnum("FALSE", Maybe.UNKNOWN)).isEqualTo(Maybe.FALSE);

        assertThat(properties.getEnum("", Maybe.UNKNOWN)).isEqualTo(Maybe.UNKNOWN);
        assertThat(properties.getEnum("foo", Maybe.UNKNOWN)).isEqualTo(Maybe.UNKNOWN);
    }

    @Test
    public void getEnumOrNull_simple() {
        StandardProperties properties = key -> key.isEmpty() ? null : key;

        assertThat(properties.<Maybe>getEnumOrNull("TRUE", Maybe::valueOf)).isEqualTo(Maybe.TRUE);
        assertThat(properties.<Maybe>getEnumOrNull("FALSE", Maybe::valueOf)).isEqualTo(Maybe.FALSE);
        assertThat(properties.<Maybe>getEnumOrNull("UNKNOWN", Maybe::valueOf)).isEqualTo(Maybe.UNKNOWN);

        assertThat(properties.<Maybe>getEnumOrNull("", Maybe::valueOf)).isNull();
        assertThat(properties.<Maybe>getEnumOrNull("true", Maybe::valueOf)).isNull();
        assertThat(properties.<Maybe>getEnumOrNull("false", Maybe::valueOf)).isNull();
        assertThat(properties.<Maybe>getEnumOrNull("foo", Maybe::valueOf)).isNull();
    }
}
