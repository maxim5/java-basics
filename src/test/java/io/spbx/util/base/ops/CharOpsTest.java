package io.spbx.util.base.ops;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class CharOpsTest {
    @Test
    public void to_ascii_lower_case() {
        assertThat(CharOps.toAsciiLowerCase('A')).isEqualTo('a');
        assertThat(CharOps.toAsciiLowerCase('F')).isEqualTo('f');
        assertThat(CharOps.toAsciiLowerCase('Z')).isEqualTo('z');

        assertThat(CharOps.toAsciiLowerCase('a')).isEqualTo('a');
        assertThat(CharOps.toAsciiLowerCase('f')).isEqualTo('f');
        assertThat(CharOps.toAsciiLowerCase('z')).isEqualTo('z');
        assertThat(CharOps.toAsciiLowerCase('0')).isEqualTo('0');
        assertThat(CharOps.toAsciiLowerCase(' ')).isEqualTo(' ');
    }

    @Test
    public void to_ascii_upper_case() {
        assertThat(CharOps.toAsciiUpperCase('a')).isEqualTo('A');
        assertThat(CharOps.toAsciiUpperCase('f')).isEqualTo('F');
        assertThat(CharOps.toAsciiUpperCase('z')).isEqualTo('Z');

        assertThat(CharOps.toAsciiUpperCase('A')).isEqualTo('A');
        assertThat(CharOps.toAsciiUpperCase('F')).isEqualTo('F');
        assertThat(CharOps.toAsciiUpperCase('Z')).isEqualTo('Z');
        assertThat(CharOps.toAsciiUpperCase('0')).isEqualTo('0');
        assertThat(CharOps.toAsciiUpperCase(' ')).isEqualTo(' ');
    }

    @Test
    public void is_ascii_lower_letter() {
        assertThat(CharOps.isAsciiLowerCase('a')).isTrue();
        assertThat(CharOps.isAsciiLowerCase('f')).isTrue();
        assertThat(CharOps.isAsciiLowerCase('z')).isTrue();

        assertThat(CharOps.isAsciiLowerCase('A')).isFalse();
        assertThat(CharOps.isAsciiLowerCase('F')).isFalse();
        assertThat(CharOps.isAsciiLowerCase('Z')).isFalse();
        assertThat(CharOps.isAsciiLowerCase('0')).isFalse();
        assertThat(CharOps.isAsciiLowerCase(' ')).isFalse();
    }

    @Test
    public void is_ascii_upper_letter() {
        assertThat(CharOps.isAsciiUpperCase('A')).isTrue();
        assertThat(CharOps.isAsciiUpperCase('F')).isTrue();
        assertThat(CharOps.isAsciiUpperCase('Z')).isTrue();

        assertThat(CharOps.isAsciiUpperCase('a')).isFalse();
        assertThat(CharOps.isAsciiUpperCase('f')).isFalse();
        assertThat(CharOps.isAsciiUpperCase('z')).isFalse();
        assertThat(CharOps.isAsciiUpperCase('0')).isFalse();
        assertThat(CharOps.isAsciiUpperCase(' ')).isFalse();
    }

    @Test
    public void range_simple() {
        assertThat(CharOps.range('a', 'a')).asList().isEmpty();
        assertThat(CharOps.range('a', 'b')).asList().containsExactly('a');
        assertThat(CharOps.range('a', 'f')).asList().containsExactly('a', 'b', 'c', 'd', 'e');
    }
}
