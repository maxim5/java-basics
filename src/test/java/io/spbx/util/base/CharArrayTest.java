package io.spbx.util.base;

import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.MoreTruth.assertAlso;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CharArrayTest {
    @Test
    public void create_empty_string() {
        CharArray array = CharArray.of("");
        assertThat(array.start()).isEqualTo(0);
        assertThat(array.end()).isEqualTo(0);
        assertThat(array.length()).isEqualTo(0);
    }

    @Test
    public void create_empty_same_pointers() {
        CharArray array = CharArray.of("foo", 3, 3);
        assertThat(array.start()).isEqualTo(3);
        assertThat(array.end()).isEqualTo(3);
        assertThat(array.length()).isEqualTo(0);
    }

    @Test
    public void create_from_nio_buffer_readonly() {
        CharBuffer nioBuffer = CharBuffer.wrap("foobar", 2, 5);
        assertThat(nioBuffer.isReadOnly()).isTrue();

        CharArray array = CharArray.of(nioBuffer);
        assertThat(CharArray.of("oba")).isEqualTo(array);
        assertThat("oba".toCharArray()).isEqualTo(array.chars);
        assertThat(0).isEqualTo(array.start);
        assertThat(3).isEqualTo(array.end);
    }

    @Test
    public void create_from_nio_buffer_writable() {
        CharBuffer nioBuffer = CharBuffer.wrap("foobar".toCharArray(), 2, 3);
        assertThat(nioBuffer.isReadOnly()).isFalse();

        CharArray array = CharArray.of(nioBuffer);
        assertThat(CharArray.of("oba")).isEqualTo(array);
        assertThat("foobar".toCharArray()).isEqualTo(array.chars);
        assertThat(2).isEqualTo(array.start);
        assertThat(5).isEqualTo(array.end);
    }

    @Test
    public void equals_and_hashCode() {
        assertAlso(CharArray.of("")).isEquivalentTo(CharArray.of(""));
        assertAlso(CharArray.of("")).isEquivalentTo(CharArray.of("foo", 0, 0));
        assertAlso(CharArray.of("")).isEquivalentTo(CharArray.of("foo", 1, 1));
        assertAlso(CharArray.of("")).isEquivalentTo(CharArray.of("foo", 2, 2));
        assertAlso(CharArray.of("")).isEquivalentTo(CharArray.of("foo", 3, 3));
        assertAlso(CharArray.of("foo")).isEquivalentTo(CharArray.of("foo"));
        assertAlso(CharArray.of("foo")).isEquivalentTo(CharArray.of("foobar", 0, 3));
        assertAlso(CharArray.of("foo")).isEquivalentTo(CharArray.of("barfoo", 3, 6));
    }

    @Test
    public void create_invalid_pointers() {
        // noinspection ConstantConditions
        assertThrows(IllegalArgumentException.class, () -> CharArray.wrap(null, 0, 0));
        assertThrows(AssertionError.class, () -> CharArray.of("foo", -1, 2));
        assertThrows(AssertionError.class, () -> CharArray.of("foo", 2, 1));
        assertThrows(AssertionError.class, () -> CharArray.of("foo", 0, 4));
        assertThrows(AssertionError.class, () -> CharArray.of("foo", 4, 4));
    }

    @Test
    public void char_at() {
        CharArray array = CharArray.of("foobar");

        assertThat(array.charAt(0)).isEqualTo('f');
        assertThat(array.charAt(1)).isEqualTo('o');
        assertThat(array.charAt(2)).isEqualTo('o');
        assertThat(array.charAt(3)).isEqualTo('b');
        assertThat(array.charAt(4)).isEqualTo('a');
        assertThat(array.charAt(5)).isEqualTo('r');
        assertThrows(AssertionError.class, () -> array.charAt(-1));
        assertThrows(AssertionError.class, () -> array.charAt(6));
    }

    @Test
    public void at() {
        CharArray array = CharArray.of("foobar");

        assertThat(array.at(0)).isEqualTo('f');
        assertThat(array.at(1)).isEqualTo('o');
        assertThat(array.at(2)).isEqualTo('o');
        assertThat(array.at(3)).isEqualTo('b');
        assertThat(array.at(4)).isEqualTo('a');
        assertThat(array.at(5)).isEqualTo('r');
        assertThat(array.at(6)).isEqualTo(-1);
        assertThat(array.at(-1)).isEqualTo('r');
        assertThat(array.at(-2)).isEqualTo('a');
        assertThat(array.at(-3)).isEqualTo('b');
        assertThat(array.at(-4)).isEqualTo('o');
        assertThat(array.at(-5)).isEqualTo('o');
        assertThat(array.at(-6)).isEqualTo('f');
        assertThat(array.at(-7)).isEqualTo(-1);
        assertThat(array.at(+100)).isEqualTo(-1);
        assertThat(array.at(-100)).isEqualTo(-1);
    }

    @Test
    public void iterator() {
        assertThat(CharArray.of("").iterator().hasNext()).isFalse();

        for (CharArray.CharCursor cursor : CharArray.of("foo").toIterable()) {
            assertThat(cursor.ch).isEqualTo("foo".charAt(cursor.index));
        }
        for (CharArray.CharCursor cursor : CharArray.of("foobar").substring(3, 6).toIterable()) {
            assertThat(cursor.ch).isEqualTo("bar".charAt(cursor.index));
        }
    }

    @Test
    public void indexOf_char() {
        CharArray array = CharArray.of("foo-bar-baz");

        assertThat(array.indexOf('f')).isEqualTo(0);
        assertThat(array.indexOf('o')).isEqualTo(1);
        assertThat(array.indexOf('-')).isEqualTo(3);
        assertThat(array.indexOf('a')).isEqualTo(5);
        assertThat(array.indexOf('z')).isEqualTo(10);
        assertThat(array.indexOf('w')).isEqualTo(-1);

        assertThat(array.indexOf('f', 1)).isEqualTo(-1);
        assertThat(array.indexOf('f', 1, -2)).isEqualTo(-2);
        assertThat(array.indexOf('f', 1, array.length())).isEqualTo(11);
        assertThat(array.indexOf('o', 1)).isEqualTo(1);
        assertThat(array.indexOf('o', 1, -2)).isEqualTo(1);
        assertThat(array.indexOf('-', 3)).isEqualTo(3);
        assertThat(array.indexOf('-', 3, array.length())).isEqualTo(3);
        assertThat(array.indexOf('-', 4)).isEqualTo(7);
        assertThat(array.indexOf('-', 4, array.length())).isEqualTo(7);
        assertThat(array.indexOf('-', 4, -2)).isEqualTo(7);
        assertThat(array.indexOf('-', 7, array.length())).isEqualTo(7);
        assertThat(array.indexOf('-', 8, array.length())).isEqualTo(11);
    }

    @Test
    public void indexOf_char_subarray() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.indexOf('f')).isEqualTo(-1);
        assertThat(array.indexOf('a')).isEqualTo(-1);
        assertThat(array.indexOf('b')).isEqualTo(2);
        assertThat(array.indexOf('o', 3)).isEqualTo(-1);
        assertThat(array.indexOf('o', 3, -2)).isEqualTo(-2);
    }

    @Test
    public void indexOf_array() {
        CharArray array = CharArray.of("foo-bar-baz");

        assertThat(array.indexOf("foo")).isEqualTo(0);
        assertThat(array.indexOf("bar")).isEqualTo(4);
        assertThat(array.indexOf("baz")).isEqualTo(8);
        assertThat(array.indexOf(CharArray.of("foo"))).isEqualTo(0);
        assertThat(array.indexOf(CharArray.of("bar"))).isEqualTo(4);
        assertThat(array.indexOf(CharArray.of("baz"))).isEqualTo(8);
        assertThat(array.indexOf(array)).isEqualTo(0);

        assertThat(array.indexOf(CharArray.of("-"))).isEqualTo(3);
        assertThat(array.indexOf(CharArray.of("-"), 4)).isEqualTo(7);
        assertThat(array.indexOf(CharArray.of("o"))).isEqualTo(1);
        assertThat(array.indexOf(CharArray.of("o"), 2)).isEqualTo(2);
        assertThat(array.indexOf(CharArray.of("a"))).isEqualTo(5);
        assertThat(array.indexOf(CharArray.of("a"), 6)).isEqualTo(9);
        assertThat(array.indexOf(CharArray.of("z"))).isEqualTo(10);
        assertThat(array.indexOf(CharArray.of("z"), 11)).isEqualTo(-1);

        assertThat(array.indexOf(CharArray.of("foo"), 1, -2)).isEqualTo(-2);
        assertThat(array.indexOf(CharArray.of("bar"), 5, -2)).isEqualTo(-2);
        assertThat(array.indexOf(CharArray.of("baz"), 10, -2)).isEqualTo(-2);
    }

    @Test
    public void indexOf_array_in_subarray() {
        CharArray array = CharArray.of("foo-bar-baz", 4, 10);  // bar-ba

        assertThat(array.indexOf("foo")).isEqualTo(-1);
        assertThat(array.indexOf("bar")).isEqualTo(0);
        assertThat(array.indexOf("baz")).isEqualTo(-1);
        assertThat(array.indexOf(CharArray.of("foo"))).isEqualTo(-1);
        assertThat(array.indexOf(CharArray.of("bar"))).isEqualTo(0);
        assertThat(array.indexOf(CharArray.of("baz"))).isEqualTo(-1);
        assertThat(array.indexOf(array)).isEqualTo(0);

        assertThat(array.indexOf(CharArray.of("-"))).isEqualTo(3);
        assertThat(array.indexOf(CharArray.of("-"), 4)).isEqualTo(-1);
        assertThat(array.indexOf(CharArray.of("a"))).isEqualTo(1);
        assertThat(array.indexOf(CharArray.of("a"), 2)).isEqualTo(5);
        assertThat(array.indexOf(CharArray.of("o"))).isEqualTo(-1);
        assertThat(array.indexOf(CharArray.of("o"), 0, -2)).isEqualTo(-2);
    }

    @Test
    public void indexOf_predicate() {
        CharArray array = CharArray.of("foobar", 1, 5);  // ooba

        assertThat(array.indexOf(i -> i < 'b')).isEqualTo(3);
        assertThat(array.indexOf(i -> i == 'b')).isEqualTo(2);
        assertThat(array.indexOf(i -> i > 'b')).isEqualTo(0);
        assertThat(array.indexOf(i -> i > 'b', 1)).isEqualTo(1);
        assertThat(array.indexOf(i -> i > 'b', 2)).isEqualTo(-1);
        assertThat(array.indexOf(i -> i > 'b', 2, -2)).isEqualTo(-2);
    }

    @Test
    public void indexOfAny() {
        CharArray array = CharArray.of("foo-bar-baz");

        assertThat(array.indexOfAny('f', 'o')).isEqualTo(0);
        assertThat(array.indexOfAny('o', 'o')).isEqualTo(1);
        assertThat(array.indexOfAny('a', '-')).isEqualTo(3);
        assertThat(array.indexOfAny('a', 'z')).isEqualTo(5);
        assertThat(array.indexOfAny('z', 'w')).isEqualTo(10);
        assertThat(array.indexOfAny('x', 'y')).isEqualTo(-1);

        assertThat(array.indexOfAny('f', 'g', 1)).isEqualTo(-1);
        assertThat(array.indexOfAny('f', 'g', 1, -2)).isEqualTo(-2);
        assertThat(array.indexOfAny('f', 'g', 1, array.length())).isEqualTo(11);
        assertThat(array.indexOfAny('o', 'a', 1)).isEqualTo(1);
        assertThat(array.indexOfAny('o', 'a', 1, -2)).isEqualTo(1);
        assertThat(array.indexOfAny('-', 'a', 3)).isEqualTo(3);
        assertThat(array.indexOfAny('-', 'a', 3, array.length())).isEqualTo(3);
        assertThat(array.indexOfAny('z', '-', 4)).isEqualTo(7);
        assertThat(array.indexOfAny('z', '-', 4, array.length())).isEqualTo(7);
        assertThat(array.indexOfAny('z', '-', 4, -2)).isEqualTo(7);
        assertThat(array.indexOfAny('z', '-', 7, array.length())).isEqualTo(7);
        assertThat(array.indexOfAny('a', '-', 8, array.length())).isEqualTo(9);
        assertThat(array.indexOfAny('z', '-', 8, array.length())).isEqualTo(10);
        assertThat(array.indexOfAny('z', '-', 11, array.length())).isEqualTo(11);
        assertThat(array.indexOfAny('o', '-', 8, array.length())).isEqualTo(11);
    }

    @Test
    public void indexOfAny_subarray() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.indexOfAny('f', 'a')).isEqualTo(-1);
        assertThat(array.indexOfAny('f', 'b')).isEqualTo(2);
        assertThat(array.indexOfAny('o', 'o', 3)).isEqualTo(-1);
        assertThat(array.indexOfAny('o', 'o', 3, -2)).isEqualTo(-2);
    }

    @Test
    public void lastIndexOf_char() {
        CharArray array = CharArray.of("foo-bar-baz");

        assertThat(array.lastIndexOf('f')).isEqualTo(0);
        assertThat(array.lastIndexOf('o')).isEqualTo(2);
        assertThat(array.lastIndexOf('-')).isEqualTo(7);
        assertThat(array.lastIndexOf('a')).isEqualTo(9);
        assertThat(array.lastIndexOf('z')).isEqualTo(10);
        assertThat(array.lastIndexOf('w')).isEqualTo(-1);

        assertThat(array.lastIndexOf('f', 10)).isEqualTo(0);
        assertThat(array.lastIndexOf('f', 10, -1)).isEqualTo(0);
        assertThat(array.lastIndexOf('a', 10)).isEqualTo(9);
        assertThat(array.lastIndexOf('a', 9)).isEqualTo(9);
        assertThat(array.lastIndexOf('a', 8)).isEqualTo(5);
        assertThat(array.lastIndexOf('a', 5)).isEqualTo(5);
        assertThat(array.lastIndexOf('a', 4)).isEqualTo(-1);
        assertThat(array.lastIndexOf('a', 4, -2)).isEqualTo(-2);
    }

    @Test
    public void lastIndexOf_char_in_subarray() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.lastIndexOf('f')).isEqualTo(-1);
        assertThat(array.lastIndexOf('a')).isEqualTo(-1);
        assertThat(array.lastIndexOf('a', 2)).isEqualTo(-1);
        assertThat(array.lastIndexOf('a', 3)).isEqualTo(-1);
        assertThat(array.lastIndexOf('b')).isEqualTo(2);
        assertThat(array.lastIndexOf('b', 2)).isEqualTo(2);
        assertThat(array.lastIndexOf('b', 3)).isEqualTo(2);
        assertThat(array.lastIndexOf('o', 0)).isEqualTo(0);
        assertThat(array.lastIndexOf('o', 3)).isEqualTo(1);
        assertThat(array.lastIndexOf('o', 3, -2)).isEqualTo(1);
    }

    @Test
    public void lastIndexOf_array() {
        CharArray array = CharArray.of("foo-bar-baz");

        assertThat(array.lastIndexOf("foo")).isEqualTo(0);
        assertThat(array.lastIndexOf("bar")).isEqualTo(4);
        assertThat(array.lastIndexOf("baz")).isEqualTo(8);
        assertThat(array.lastIndexOf(CharArray.of("foo"))).isEqualTo(0);
        assertThat(array.lastIndexOf(CharArray.of("bar"))).isEqualTo(4);
        assertThat(array.lastIndexOf(CharArray.of("baz"))).isEqualTo(8);
        assertThat(array.lastIndexOf(array)).isEqualTo(0);

        assertThat(array.lastIndexOf("f")).isEqualTo(0);
        assertThat(array.lastIndexOf("o")).isEqualTo(2);
        assertThat(array.lastIndexOf("-")).isEqualTo(7);
        assertThat(array.lastIndexOf("a")).isEqualTo(9);
        assertThat(array.lastIndexOf("z")).isEqualTo(10);
        assertThat(array.lastIndexOf("w")).isEqualTo(-1);

        assertThat(array.lastIndexOf(CharArray.of("f"), 10)).isEqualTo(0);
        assertThat(array.lastIndexOf(CharArray.of("f"), 10, -1)).isEqualTo(0);
        assertThat(array.lastIndexOf(CharArray.of("o"))).isEqualTo(2);
        assertThat(array.lastIndexOf(CharArray.of("o"), 1)).isEqualTo(1);
        assertThat(array.lastIndexOf(CharArray.of("o"), 0)).isEqualTo(-1);
        assertThat(array.lastIndexOf(CharArray.of("a"))).isEqualTo(9);
        assertThat(array.lastIndexOf(CharArray.of("a"), 8)).isEqualTo(5);
        assertThat(array.lastIndexOf(CharArray.of("a"), 4)).isEqualTo(-1);
        assertThat(array.lastIndexOf(CharArray.of("a"), 4, -2)).isEqualTo(-2);
    }

    @Test
    public void lastIndexOf_array_in_subarray() {
        CharArray array = CharArray.of("foo-bar-baz", 4, 10);  // bar-ba

        assertThat(array.lastIndexOf("foo")).isEqualTo(-1);
        assertThat(array.lastIndexOf("bar")).isEqualTo(0);
        assertThat(array.lastIndexOf("baz")).isEqualTo(-1);
        assertThat(array.lastIndexOf("-")).isEqualTo(3);
        assertThat(array.lastIndexOf(array)).isEqualTo(0);

        assertThat(array.lastIndexOf(CharArray.of("f"))).isEqualTo(-1);
        assertThat(array.lastIndexOf(CharArray.of("o"))).isEqualTo(-1);
        assertThat(array.lastIndexOf(CharArray.of("a"))).isEqualTo(5);
        assertThat(array.lastIndexOf(CharArray.of("a"), 4)).isEqualTo(1);
        assertThat(array.lastIndexOf(CharArray.of("a"), 0)).isEqualTo(-1);
        assertThat(array.lastIndexOf(CharArray.of("a"), 0, -2)).isEqualTo(-2);
    }

    @Test
    public void lastIndexOf_predicate() {
        CharArray array = CharArray.of("foo-bar-baz", 4, 10);  // bar-ba

        assertThat(array.lastIndexOf(i -> i < 'b')).isEqualTo(5);
        assertThat(array.lastIndexOf(i -> i == 'b')).isEqualTo(4);
        assertThat(array.lastIndexOf(i -> i > 'b')).isEqualTo(2);
        assertThat(array.lastIndexOf(i -> i > 'b', 1)).isEqualTo(-1);
        assertThat(array.lastIndexOf(i -> i > 'b', 1, -2)).isEqualTo(-2);
    }

    @Test
    public void lastIndexOfAny() {
        CharArray array = CharArray.of("foo-bar-baz");

        assertThat(array.lastIndexOfAny('f', 'g')).isEqualTo(0);
        assertThat(array.lastIndexOfAny('f', 'o')).isEqualTo(2);
        assertThat(array.lastIndexOfAny('o', 'f')).isEqualTo(2);
        assertThat(array.lastIndexOfAny('o', 'x')).isEqualTo(2);
        assertThat(array.lastIndexOfAny('-', 'o')).isEqualTo(7);
        assertThat(array.lastIndexOfAny('-', 'a')).isEqualTo(9);
        assertThat(array.lastIndexOfAny('-', 'z')).isEqualTo(10);
        assertThat(array.lastIndexOfAny('x', 'y')).isEqualTo(-1);

        assertThat(array.lastIndexOfAny('f', 'g', 10)).isEqualTo(0);
        assertThat(array.lastIndexOfAny('f', 'g', 10, -1)).isEqualTo(0);
        assertThat(array.lastIndexOfAny('f', 'z', 10)).isEqualTo(10);
        assertThat(array.lastIndexOfAny('f', 'z', 10, -1)).isEqualTo(10);
        assertThat(array.lastIndexOfAny('a', 'z', 10)).isEqualTo(10);
        assertThat(array.lastIndexOfAny('a', 'r', 10)).isEqualTo(9);
        assertThat(array.lastIndexOfAny('a', 'z', 9)).isEqualTo(9);
        assertThat(array.lastIndexOfAny('a', 'z', 8)).isEqualTo(5);
        assertThat(array.lastIndexOfAny('a', 'r', 5)).isEqualTo(5);
        assertThat(array.lastIndexOfAny('a', 'r', 4)).isEqualTo(-1);
        assertThat(array.lastIndexOfAny('a', 'r', 4, -2)).isEqualTo(-2);
    }

    @Test
    public void lastIndexOfAny_subarray() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.lastIndexOfAny('f', 'a')).isEqualTo(-1);
        assertThat(array.lastIndexOfAny('a', 'f')).isEqualTo(-1);
        assertThat(array.lastIndexOfAny('a', 'f', 2)).isEqualTo(-1);
        assertThat(array.lastIndexOfAny('a', 'f', 3)).isEqualTo(-1);
        assertThat(array.lastIndexOfAny('b', 'o')).isEqualTo(2);
        assertThat(array.lastIndexOfAny('b', 'o', 2)).isEqualTo(2);
        assertThat(array.lastIndexOfAny('b', 'o', 3)).isEqualTo(2);
        assertThat(array.lastIndexOfAny('o', 'f', 0)).isEqualTo(0);
        assertThat(array.lastIndexOfAny('o', 'f', 3)).isEqualTo(1);
        assertThat(array.lastIndexOfAny('o', 'f', 3, -2)).isEqualTo(1);
    }

    @Test
    public void contains_char() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.contains('o')).isTrue();
        assertThat(array.contains('b')).isTrue();
        assertThat(array.contains('f')).isFalse();
        assertThat(array.contains('a')).isFalse();
        assertThat(array.contains('x')).isFalse();
    }

    @Test
    public void contains_array() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.contains("o")).isTrue();
        assertThat(array.contains("b")).isTrue();
        assertThat(array.contains("f")).isFalse();
        assertThat(array.contains("a")).isFalse();
        assertThat(array.contains("x")).isFalse();

        assertThat(array.contains("oo")).isTrue();
        assertThat(array.contains("ob")).isTrue();
        assertThat(array.contains(array)).isTrue();
    }

    @Test
    public void containsAny() {
        CharArray array = CharArray.of("foobar", 1, 4);  // oob

        assertThat(array.containsAny('o', 'b')).isTrue();
        assertThat(array.containsAny('o', 'x')).isTrue();
        assertThat(array.containsAny('b', 'x')).isTrue();
        assertThat(array.containsAny('b', 'a')).isTrue();
        assertThat(array.containsAny('a', 'x')).isFalse();
    }

    @Test
    public void split_by_char() {
        assertThat(CharArray.of("").split('.')).containsExactly(CharArray.of(""));
        assertThat(CharArray.of(".").split('.')).containsExactly(CharArray.of(""), CharArray.of(""));
        assertThat(CharArray.of("..").split('.')).containsExactly(CharArray.of(""), CharArray.of(""), CharArray.of(""));
        assertThat(CharArray.of("a.").split('.')).containsExactly(CharArray.of("a"), CharArray.of(""));
        assertThat(CharArray.of("a.b").split('.')).containsExactly(CharArray.of("a"), CharArray.of("b"));
        assertThat(CharArray.of("a..b").split('.')).containsExactly(CharArray.of("a"), CharArray.of(""), CharArray.of("b"));
        assertThat(CharArray.of("ab").split('.')).containsExactly(CharArray.of("ab"));
        assertThat(CharArray.of("ab.").split('.')).containsExactly(CharArray.of("ab"), CharArray.of(""));
        assertThat(CharArray.of(".ab").split('.')).containsExactly(CharArray.of(""), CharArray.of("ab"));
        assertThat(CharArray.of("ab.cd").split('.')).containsExactly(CharArray.of("ab"), CharArray.of("cd"));
        assertThat(CharArray.of("a.b.c").split('.')).containsExactly(CharArray.of("a"), CharArray.of("b"), CharArray.of("c"));
    }

    @Test
    public void trim_by_predicate() {
        assertThat(CharArray.of("").trimStart(Character::isDigit)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("").trimStart(Character::isDigit)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("").trimStart(Character::isDigit)).isEqualTo(CharArray.of(""));

        assertThat(CharArray.of("foobar").trimStart(Character::isDigit)).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").trimEnd(Character::isDigit)).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").trim(Character::isDigit)).isEqualTo(CharArray.of("foobar"));

        assertThat(CharArray.of("123foobar456").trimStart(Character::isDigit)).isEqualTo(CharArray.of("foobar456"));
        assertThat(CharArray.of("123foobar456").trimEnd(Character::isDigit)).isEqualTo(CharArray.of("123foobar"));
        assertThat(CharArray.of("123foobar456").trim(Character::isDigit)).isEqualTo(CharArray.of("foobar"));

        assertThat(CharArray.of("123").trimStart(Character::isDigit)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("123").trimStart(Character::isDigit)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("123").trimStart(Character::isDigit)).isEqualTo(CharArray.of(""));
    }

    @Test
    public void trim_char() {
        assertThat(CharArray.of("").trim('a')).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("bbb").trim('a')).isEqualTo(CharArray.of("bbb"));
        assertThat(CharArray.of("abba").trim('a')).isEqualTo(CharArray.of("bb"));
        assertThat(CharArray.of("baba").trim('a')).isEqualTo(CharArray.of("bab"));
        assertThat(CharArray.of("aba").trim('a')).isEqualTo(CharArray.of("b"));
        assertThat(CharArray.of("a-a-a").trim('a')).isEqualTo(CharArray.of("-a-"));
        assertThat(CharArray.of("aaa").trim('a')).isEqualTo(CharArray.of(""));
    }

    @Test
    public void trim_spaces() {
        assertThat(CharArray.of("").trim()).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of(" ").trim()).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("    ").trim()).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("  \t\n\r").trim()).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("\nfoo bar  \t\t").trim()).isEqualTo(CharArray.of("foo bar"));
    }

    @Test
    public void commonPrefix() {
        assertThat(CharArray.of("foo").commonPrefix("bar")).isEqualTo(0);
        assertThat(CharArray.of("bar").commonPrefix("baz")).isEqualTo(2);
        assertThat(CharArray.of("foo").commonPrefix("foo")).isEqualTo(3);

        assertThat(CharArray.of("foo").commonPrefix(CharArray.of("bar"))).isEqualTo(0);
        assertThat(CharArray.of("bar").commonPrefix(CharArray.of("baz"))).isEqualTo(2);

        assertThat(CharArray.of("foo").commonPrefix(CharArray.of("foobar", 3, 6))).isEqualTo(0);
        assertThat(CharArray.of("bar").commonPrefix(CharArray.of("barbaz", 3, 6))).isEqualTo(2);
        assertThat(CharArray.of("foobar", 3, 6).commonPrefix(CharArray.of("foo"))).isEqualTo(0);
        assertThat(CharArray.of("barbaz", 3, 6).commonPrefix(CharArray.of("bar"))).isEqualTo(2);
    }

    @Test
    public void commonPrefix_empty() {
        assertThat(CharArray.of("").commonPrefix(CharArray.of(""))).isEqualTo(0);
        assertThat(CharArray.of("foo").commonPrefix(CharArray.of(""))).isEqualTo(0);
        assertThat(CharArray.of("").commonPrefix(CharArray.of("foo"))).isEqualTo(0);

        assertThat(CharArray.of("foo", 1, 2).commonPrefix(CharArray.of("foo", 3, 3))).isEqualTo(0);
        assertThat(CharArray.of("xxx", 1, 1).commonPrefix(CharArray.of("xxx", 2, 2))).isEqualTo(0);
    }

    @Test
    public void commonPrefix_same_prefix() {
        assertThat(CharArray.of("foo").commonPrefix(CharArray.of("foo"))).isEqualTo(3);
        assertThat(CharArray.of("foo").commonPrefix(CharArray.of("foobar"))).isEqualTo(3);
        assertThat(CharArray.of("foobar").commonPrefix(CharArray.of("foo"))).isEqualTo(3);

        assertThat(CharArray.of("foo").commonPrefix(CharArray.of("barfoo", 3, 6))).isEqualTo(3);
        assertThat(CharArray.of("barfoo", 3, 6).commonPrefix(CharArray.of("foo"))).isEqualTo(3);
    }

    @Test
    public void isPrefixOf() {
        assertThat(CharArray.of("foo").isPrefixOf(CharArray.of("bar"))).isFalse();
        assertThat(CharArray.of("foo").isPrefixOf(CharArray.of("foobar"))).isTrue();
        assertThat(CharArray.of("foo").isPrefixOf(CharArray.of("barfoo"))).isFalse();
        assertThat(CharArray.of("foo").isPrefixOf(CharArray.of("foo"))).isTrue();
        assertThat(CharArray.of("foo").isPrefixOf(CharArray.of("fo"))).isFalse();

        assertThat(CharArray.of("").isPrefixOf(CharArray.of(""))).isTrue();
        assertThat(CharArray.of("foo").isPrefixOf(CharArray.of(""))).isFalse();
        assertThat(CharArray.of("").isPrefixOf(CharArray.of("foo"))).isTrue();
    }

    @Test
    public void commonSuffix() {
        assertThat(CharArray.of("foo").commonSuffix("bar")).isEqualTo(0);
        assertThat(CharArray.of("foo").commonSuffix("boo")).isEqualTo(2);
        assertThat(CharArray.of("foo").commonSuffix("foo")).isEqualTo(3);

        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("bar"))).isEqualTo(0);
        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("boo"))).isEqualTo(2);

        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("foobar", 3, 6))).isEqualTo(0);
        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("fooboo", 3, 6))).isEqualTo(2);
        assertThat(CharArray.of("foobar", 3, 6).commonSuffix(CharArray.of("foo"))).isEqualTo(0);
        assertThat(CharArray.of("fooboo", 3, 6).commonSuffix(CharArray.of("foo"))).isEqualTo(2);
    }

    @Test
    public void commonSuffix_empty() {
        assertThat(CharArray.of("").commonSuffix(CharArray.of(""))).isEqualTo(0);
        assertThat(CharArray.of("foo").commonSuffix(CharArray.of(""))).isEqualTo(0);
        assertThat(CharArray.of("").commonSuffix(CharArray.of("foo"))).isEqualTo(0);

        assertThat(CharArray.of("foo", 1, 2).commonSuffix(CharArray.of("foo", 3, 3))).isEqualTo(0);
        assertThat(CharArray.of("xxx", 1, 1).commonSuffix(CharArray.of("xxx", 2, 2))).isEqualTo(0);
    }

    @Test
    public void commonSuffix_same_suffix() {
        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("foo"))).isEqualTo(3);
        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("barfoo"))).isEqualTo(3);
        assertThat(CharArray.of("barfoo").commonSuffix(CharArray.of("foo"))).isEqualTo(3);

        assertThat(CharArray.of("foo").commonSuffix(CharArray.of("barfoo", 3, 6))).isEqualTo(3);
        assertThat(CharArray.of("barfoo", 3, 6).commonSuffix(CharArray.of("foo"))).isEqualTo(3);
    }

    @Test
    public void isSuffixOf() {
        assertThat(CharArray.of("foo").isSuffixOf(CharArray.of("bar"))).isFalse();
        assertThat(CharArray.of("foo").isSuffixOf(CharArray.of("foobar"))).isFalse();
        assertThat(CharArray.of("foo").isSuffixOf(CharArray.of("barfoo"))).isTrue();
        assertThat(CharArray.of("foo").isSuffixOf(CharArray.of("foo"))).isTrue();
        assertThat(CharArray.of("foo").isSuffixOf(CharArray.of("oo"))).isFalse();

        assertThat(CharArray.of("").isSuffixOf(CharArray.of(""))).isTrue();
        assertThat(CharArray.of("foo").isSuffixOf(CharArray.of(""))).isFalse();
        assertThat(CharArray.of("").isSuffixOf(CharArray.of("foo"))).isTrue();
    }

    @Test
    public void startsWith() {
        CharArray array = CharArray.of("foo");
        assertThat(array.startsWith(CharArray.of(""))).isTrue();
        assertThat(array.startsWith(CharArray.of("f"))).isTrue();
        assertThat(array.startsWith(CharArray.of("fo"))).isTrue();
        assertThat(array.startsWith(CharArray.of("foo"))).isTrue();

        assertThat(array.startsWith(CharArray.of("x"))).isFalse();
        assertThat(array.startsWith(CharArray.of("bar"))).isFalse();
        assertThat(array.startsWith(CharArray.of("foe"))).isFalse();
        assertThat(array.startsWith(CharArray.of("foo!"))).isFalse();
        assertThat(array.startsWith(CharArray.of("foobar"))).isFalse();
    }

    @Test
    public void startsWith_string() {
        CharArray array = CharArray.of("foo");
        assertThat(array.startsWith("")).isTrue();
        assertThat(array.startsWith("f")).isTrue();
        assertThat(array.startsWith("fo")).isTrue();
        assertThat(array.startsWith("foo")).isTrue();

        assertThat(array.startsWith("x")).isFalse();
        assertThat(array.startsWith("bar")).isFalse();
        assertThat(array.startsWith("foe")).isFalse();
        assertThat(array.startsWith("foo!")).isFalse();
        assertThat(array.startsWith("foobar")).isFalse();
    }

    @Test
    public void startsWith_char() {
        CharArray array = CharArray.of("foo");
        assertThat(array.startsWith('f')).isTrue();
        assertThat(array.startsWith('o')).isFalse();
        assertThat(array.startsWith('a')).isFalse();
        assertThat(array.startsWith('x')).isFalse();

        assertThat(CharArray.of("").startsWith(' ')).isFalse();
    }

    @Test
    public void endsWith() {
        CharArray array = CharArray.of("foo");
        assertThat(array.endsWith(CharArray.of(""))).isTrue();
        assertThat(array.endsWith(CharArray.of("o"))).isTrue();
        assertThat(array.endsWith(CharArray.of("oo"))).isTrue();
        assertThat(array.endsWith(CharArray.of("foo"))).isTrue();

        assertThat(array.endsWith(CharArray.of("x"))).isFalse();
        assertThat(array.endsWith(CharArray.of("bar"))).isFalse();
        assertThat(array.endsWith(CharArray.of("boo"))).isFalse();
        assertThat(array.endsWith(CharArray.of("!foo"))).isFalse();
        assertThat(array.endsWith(CharArray.of("barfoo"))).isFalse();
    }

    @Test
    public void endsWith_string() {
        CharArray array = CharArray.of("foo");
        assertThat(array.endsWith("")).isTrue();
        assertThat(array.endsWith("o")).isTrue();
        assertThat(array.endsWith("oo")).isTrue();
        assertThat(array.endsWith("foo")).isTrue();

        assertThat(array.endsWith("x")).isFalse();
        assertThat(array.endsWith("bar")).isFalse();
        assertThat(array.endsWith("boo")).isFalse();
        assertThat(array.endsWith("!foo")).isFalse();
        assertThat(array.endsWith("barfoo")).isFalse();
    }

    @Test
    public void endsWith_char() {
        CharArray array = CharArray.of("foo");
        assertThat(array.endsWith('o')).isTrue();
        assertThat(array.endsWith('f')).isFalse();
        assertThat(array.endsWith('a')).isFalse();
        assertThat(array.endsWith('x')).isFalse();

        assertThat(CharArray.of("").endsWith(' ')).isFalse();
    }

    @Test
    public void substring_valid() {
        assertThat(CharArray.of("foobar").substring(0, 3)).isEqualTo(CharArray.of("foo"));
        assertThat(CharArray.of("foobar").substring(3, 6)).isEqualTo(CharArray.of("bar"));
        assertThat(CharArray.of("foobar").substring(0, 1)).isEqualTo(CharArray.of("f"));
        assertThat(CharArray.of("foobar").substring(1, 1)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("foobar").substring(5, 6)).isEqualTo(CharArray.of("r"));
        assertThat(CharArray.of("foobar").substring(6, 6)).isEqualTo(CharArray.of(""));

        assertThat(CharArray.of("foobar").substring(0, -1)).isEqualTo(CharArray.of("fooba"));
        assertThat(CharArray.of("foobar").substring(0, -2)).isEqualTo(CharArray.of("foob"));
        assertThat(CharArray.of("foobar").substring(0, -3)).isEqualTo(CharArray.of("foo"));
        assertThat(CharArray.of("foobar").substring(-3, 6)).isEqualTo(CharArray.of("bar"));
        assertThat(CharArray.of("foobar").substring(-3, -1)).isEqualTo(CharArray.of("ba"));
        assertThat(CharArray.of("foobar").substring(-3, -2)).isEqualTo(CharArray.of("b"));
        assertThat(CharArray.of("foobar").substring(-3, -3)).isEqualTo(CharArray.of(""));

        assertThat(CharArray.of("foobar").substring(0, 1)).isEqualTo(CharArray.of("f"));
        assertThat(CharArray.of("foobar").substring(0, 0)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("foobar").substring(1, 1)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("foobar").substring(0, -6)).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("foobar").substring(1, -5)).isEqualTo(CharArray.of(""));
    }

    @Test
    public void substring_invalid() {
        assertThrows(AssertionError.class, () -> CharArray.of("foobar").substring(0, 7));
        assertThrows(AssertionError.class, () -> CharArray.of("foobar").substring(7, 7));
        assertThrows(AssertionError.class, () -> CharArray.of("foobar").substring(7, 8));
        assertThrows(AssertionError.class, () -> CharArray.of("foobar").substring(0, -10));
        assertThrows(AssertionError.class, () -> CharArray.of("foobar").substring(-10, 6));
        assertThrows(AssertionError.class, () -> CharArray.of("foobar").substring(1, 0));
    }

    @Test
    public void join_same_buffer() {
        CharArray array = CharArray.of("foobar");
        CharArray foo = array.substringUntil(3);
        CharArray bar = array.substringFrom(3);
        CharArray join = CharArray.join(foo, bar);

        assertThat(array.chars).isSameInstanceAs(foo.chars);
        assertThat(array.chars).isSameInstanceAs(bar.chars);
        assertThat(array.chars).isSameInstanceAs(join.chars);
        assertThat(array).isEqualTo(join);
    }

    @Test
    public void join_not_same_buffer() {
        CharArray array = CharArray.of("foobar");
        CharArray foo = CharArray.of("foo");
        CharArray bar = CharArray.of("bar");
        CharArray join = CharArray.join(foo, bar);

        assertThat(array.chars).isNotSameInstanceAs(foo.chars);
        assertThat(array.chars).isNotSameInstanceAs(bar.chars);
        assertThat(array.chars).isNotSameInstanceAs(join.chars);
        assertThat(array).isEqualTo(join);
    }

    @Test
    public void cutPrefix_array() {
        CharArray foobar = CharArray.of("foobar");
        CharArray foo = CharArray.of("foo");
        CharArray bar = CharArray.of("bar");
        CharArray empty = CharArray.of("");

        assertThat(foobar.cutPrefix(empty)).isEqualTo(foobar);
        assertThat(foobar.cutPrefix(foo)).isEqualTo(bar);
        assertThat(foobar.cutPrefix(foo)).isEqualTo(bar);
        assertThat(foobar.cutPrefix(foobar)).isEqualTo(empty);

        assertThat(foobar.substringFrom(3)).isEqualTo(bar);
        assertThat(foobar.substringFrom(3).cutPrefix(foo)).isEqualTo(bar);
        assertThat(foobar.substringFrom(3).cutPrefix(bar)).isEqualTo(empty);
        assertThat(foobar.substringFrom(3).cutPrefix(bar.substringUntil(0))).isEqualTo(bar);
        assertThat(foobar.substringFrom(3).cutPrefix(bar.substringUntil(1))).isEqualTo(CharArray.of("ar"));  // cut b
        assertThat(foobar.substringFrom(3).cutPrefix(bar.substringUntil(2))).isEqualTo(CharArray.of("r"));   // cut ba
    }

    @Test
    public void cutPrefix_str() {
        assertThat(CharArray.of("foobar").cutPrefix("")).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").cutPrefix("foo")).isEqualTo(CharArray.of("bar"));
        assertThat(CharArray.of("foobar").cutPrefix("bar")).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").cutPrefix("fooba")).isEqualTo(CharArray.of("r"));
        assertThat(CharArray.of("foobar").cutPrefix("foobar")).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("foobar").cutPrefix("foobarbaz")).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").cutPrefix('f')).isEqualTo(CharArray.of("oobar"));
        assertThat(CharArray.of("foobar").cutPrefix('o')).isEqualTo(CharArray.of("foobar"));
    }

    @Test
    public void cutSuffix_array() {
        CharArray foobar = CharArray.of("foobar");
        CharArray foo = CharArray.of("foo");
        CharArray bar = CharArray.of("bar");
        CharArray empty = CharArray.of("");

        assertThat(foobar.cutSuffix(empty)).isEqualTo(foobar);
        assertThat(foobar.cutSuffix(bar)).isEqualTo(foo);
        assertThat(foobar.cutSuffix(foo)).isEqualTo(foobar);
        assertThat(foobar.cutSuffix(foobar)).isEqualTo(empty);

        assertThat(foobar.substringFrom(3)).isEqualTo(bar);
        assertThat(foobar.substringFrom(3).cutSuffix(foo)).isEqualTo(bar);
        assertThat(foobar.substringFrom(3).cutSuffix(bar)).isEqualTo(empty);
        assertThat(foobar.substringFrom(3).cutSuffix(bar.substringFrom(1))).isEqualTo(CharArray.of("b"));   // cut ar
        assertThat(foobar.substringFrom(3).cutSuffix(bar.substringFrom(2))).isEqualTo(CharArray.of("ba"));  // cut r
        assertThat(foobar.substringFrom(3).cutSuffix(bar.substringFrom(3))).isEqualTo(bar);
    }

    @Test
    public void cutSuffix_str() {
        assertThat(CharArray.of("foobar").cutSuffix("")).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").cutSuffix("bar")).isEqualTo(CharArray.of("foo"));
        assertThat(CharArray.of("foobar").cutSuffix("foo")).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").cutSuffix("oobar")).isEqualTo(CharArray.of("f"));
        assertThat(CharArray.of("foobar").cutSuffix("foobar")).isEqualTo(CharArray.of(""));
        assertThat(CharArray.of("foobar").cutSuffix("foofoobar")).isEqualTo(CharArray.of("foobar"));
        assertThat(CharArray.of("foobar").cutSuffix('r')).isEqualTo(CharArray.of("fooba"));
        assertThat(CharArray.of("foobar").cutSuffix('a')).isEqualTo(CharArray.of("foobar"));
    }

    @Test
    public void chars() {
        int[] array = CharArray.of("foobar").chars().toArray();
        assertThat(array).isEqualTo(new int[] { 102, 111, 111, 98, 97, 114 });
    }

    @Test
    public void chars_empty() {
        int[] array = CharArray.of("").chars().toArray();
        assertThat(array).isEqualTo(new int[0]);
    }

    @Test
    public void chars_of_subbuffer() {
        int[] array = CharArray.of("foobar", 1, 3).chars().toArray();
        assertThat(array).isEqualTo(new int[] { 111, 111 });
    }

    @Test
    public void codepoints() {
        int[] array = CharArray.of("foobar").codePoints().toArray();
        assertThat(array).isEqualTo(new int[] { 102, 111, 111, 98, 97, 114 });
    }

    @Test
    public void codepoints_empty() {
        int[] array = CharArray.of("").codePoints().toArray();
        assertThat(array).isEqualTo(new int[0]);
    }

    @Test
    public void codepoints_of_subbuffer() {
        int[] array = CharArray.of("foobar", 1, 3).codePoints().toArray();
        assertThat(array).isEqualTo(new int[] { 111, 111 });
    }

    @Test
    public void compareTo_array_simple() {
        assertThat(CharArray.of("foo").compareTo(CharArray.of("bar"))).isAtLeast(1);
        assertThat(CharArray.of("foo").compareTo(CharArray.of("foo"))).isEqualTo(0);
        assertThat(CharArray.of("bar").compareTo(CharArray.of("foo"))).isAtMost(-1);
        assertThat(CharArray.of("foobar").compareTo(CharArray.of("foo"))).isAtLeast(1);
        assertThat(CharArray.of("foo").compareTo(CharArray.of("foobar"))).isAtMost(-1);
    }

    @Test
    public void compareTo_array_empty() {
        assertThat(CharArray.of("").compareTo(CharArray.of(""))).isEqualTo(0);
        assertThat(CharArray.of("foo").compareTo(CharArray.of(""))).isAtLeast(1);
        assertThat(CharArray.of("").compareTo(CharArray.of("foo"))).isAtMost(-1);
    }

    @Test
    public void compareTo_string_simple() {
        assertThat(CharArray.of("foo").compareTo("bar")).isAtLeast(1);
        assertThat(CharArray.of("foo").compareTo("foo")).isEqualTo(0);
        assertThat(CharArray.of("bar").compareTo("foo")).isAtMost(-1);
        assertThat(CharArray.of("foobar").compareTo("foo")).isAtLeast(1);
        assertThat(CharArray.of("foo").compareTo("foobar")).isAtMost(-1);
    }

    @Test
    public void compareTo_string_empty() {
        assertThat(CharArray.of("").compareTo("")).isEqualTo(0);
        assertThat(CharArray.of("foo").compareTo("")).isAtLeast(1);
        assertThat(CharArray.of("").compareTo("foo")).isAtMost(-1);
    }

    @Test
    public void contentEquals_char() {
        assertThat(CharArray.of("").contentEquals('a')).isFalse();
        assertThat(CharArray.of("a").contentEquals('a')).isTrue();
        assertThat(CharArray.of("ab").contentEquals('a')).isFalse();
        assertThat(CharArray.of("b").contentEquals('a')).isFalse();
    }

    @Test
    public void contentEquals_string() {
        assertThat(CharArray.of("").contentEquals("")).isTrue();
        assertThat(CharArray.of("").contentEquals("foo")).isFalse();

        assertThat(CharArray.of("a").contentEquals("")).isFalse();
        assertThat(CharArray.of("a").contentEquals("a")).isTrue();
        assertThat(CharArray.of("a").contentEquals("A")).isFalse();
        assertThat(CharArray.of("a").contentEquals("foo")).isFalse();

        assertThat(CharArray.of("foo").contentEquals("")).isFalse();
        assertThat(CharArray.of("foo").contentEquals("a")).isFalse();
        assertThat(CharArray.of("foo").contentEquals("foo")).isTrue();
        assertThat(CharArray.of("foo").contentEquals("Foo")).isFalse();
    }

    @Test
    public void contentEqualsIgnoreCase() {
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("foo")).isTrue();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("FOO")).isTrue();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("FoO")).isTrue();

        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("foo.")).isFalse();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("fooo")).isFalse();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("bar")).isFalse();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("oo")).isFalse();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("fof")).isFalse();
        assertThat(CharArray.of("foo").contentEqualsIgnoreCase("")).isFalse();
    }
}
