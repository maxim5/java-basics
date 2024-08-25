package io.spbx.util.array;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class MutableCharArrayTest {
    @Test
    public void create_empty_string() {
        MutableCharArray array = MutableCharArray.of("");
        assertThat(array.start()).isEqualTo(0);
        assertThat(array.end()).isEqualTo(0);
        assertThat(array.length()).isEqualTo(0);
    }

    @Test
    public void create_empty_same_pointers() {
        MutableCharArray array = MutableCharArray.of("foo", 3, 3);
        assertThat(array.start()).isEqualTo(3);
        assertThat(array.end()).isEqualTo(3);
        assertThat(array.length()).isEqualTo(0);
    }

    @Test
    public void immutable_to_others() {
        CharArray array = CharArray.of("foo");

        assertThat(array.mutable()).isEqualTo(array);
        assertThat(array.mutableCopy()).isEqualTo(array);

        assertThat(array.immutable()).isSameInstanceAs(array);
        assertThat(array.immutableCopy()).isEqualTo(array);
        assertThat(array.immutable() instanceof MutableCharArray).isFalse();
        assertThat(array.immutableCopy() instanceof MutableCharArray).isFalse();
    }

    @Test
    public void mutable_to_others() {
        MutableCharArray array = MutableCharArray.of("foo");

        assertThat(array.mutable()).isSameInstanceAs(array);
        assertThat(array.mutableCopy()).isEqualTo(array);
        assertThat(array.mutableCopy()).isNotSameInstanceAs(array);

        assertThat(array.immutable()).isEqualTo(array);
        assertThat(array.immutableCopy()).isEqualTo(array);
        assertThat(array.immutable() instanceof MutableCharArray).isFalse();
        assertThat(array.immutableCopy() instanceof MutableCharArray).isFalse();
    }

    @Test
    public void mutableSubstring() {
        MutableCharArray array = MutableCharArray.of("foobar");

        assertThat(array.substring(0, 3) instanceof MutableCharArray).isFalse();
        assertThat(array.substring(3, 6) instanceof MutableCharArray).isFalse();
        assertThat(array.substring(3, -1) instanceof MutableCharArray).isFalse();

        assertThat(array.mutableSubstring(0, 3)).isEqualTo(array.substring(0, 3));
        assertThat(array.mutableSubstring(1, 4)).isEqualTo(array.substring(1, 4));
        assertThat(array.mutableSubstring(3, 6)).isEqualTo(array.substring(3, 6));
        assertThat(array.mutableSubstring(0, -1)).isEqualTo(array.substring(0, -1));
        assertThat(array.mutableSubstring(-2, -1)).isEqualTo(array.substring(-2, -1));
    }

    @Test
    public void offset_start_and_end() {
        MutableCharArray array = MutableCharArray.of("foobar");

        array.offsetStart(1);
        assertThat(array.toString()).isEqualTo("oobar");

        array.offsetEnd(1);
        assertThat(array.toString()).isEqualTo("ooba");

        array.offsetStart(2);
        assertThat(array.toString()).isEqualTo("ba");

        array.offsetEnd(2);
        assertThat(array.toString()).isEqualTo("");
    }

    @Test
    public void offset_prefix() {
        MutableCharArray array = MutableCharArray.of("foobar");

        array.offsetPrefix(CharArray.of("food"));
        assertThat(array.toString()).isEqualTo("foobar");

        array.offsetPrefix(CharArray.of("foo"));
        assertThat(array.toString()).isEqualTo("bar");

        array.offsetPrefix(CharArray.of("a"));
        assertThat(array.toString()).isEqualTo("bar");

        array.offsetPrefix(CharArray.of("b"));
        assertThat(array.toString()).isEqualTo("ar");

        array.offsetPrefix('a');
        assertThat(array.toString()).isEqualTo("r");

        array.offsetPrefix('r');
        assertThat(array.toString()).isEqualTo("");
    }

    @Test
    public void offset_suffix() {
        MutableCharArray array = MutableCharArray.of("foobar");

        array.offsetSuffix(CharArray.of("var"));
        assertThat(array.toString()).isEqualTo("foobar");

        array.offsetSuffix(CharArray.of("bar"));
        assertThat(array.toString()).isEqualTo("foo");

        array.offsetSuffix(CharArray.of("a"));
        assertThat(array.toString()).isEqualTo("foo");

        array.offsetSuffix(CharArray.of("o"));
        assertThat(array.toString()).isEqualTo("fo");

        array.offsetSuffix('o');
        assertThat(array.toString()).isEqualTo("f");

        array.offsetSuffix('f');
        assertThat(array.toString()).isEqualTo("");
    }

    @Test
    public void reset() {
        MutableCharArray array = MutableCharArray.of("foobar", 3, 5);

        array.resetStart();
        assertThat(array.toString()).isEqualTo("fooba");

        array.resetEnd();
        assertThat(array.toString()).isEqualTo("foobar");

        array.offsetStart(2);
        array.offsetEnd(2);
        assertThat(array.toString()).isEqualTo("ob");

        array.reset();
        assertThat(array.toString()).isEqualTo("foobar");
    }

    @Test
    public void join_same_buffer() {
        MutableCharArray foobar = MutableCharArray.of("foobar");
        CharArray foo = foobar.substring(0, 3);
        CharArray bar = foobar.substring(3, 6);

        assertThat(MutableCharArray.join(foo, bar)).isEqualTo(foobar);
        assertThat(MutableCharArray.join(foo, bar)._chars()).isSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.immutableCopy(), bar)._chars()).isSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo, bar.immutableCopy())._chars()).isSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.immutableCopy(), bar.immutableCopy())._chars()).isSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.mutable(), bar)._chars()).isSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo, bar.mutable())._chars()).isSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.mutable(), bar.mutable())._chars()).isSameInstanceAs(foobar._chars());
    }

    @Test
    public void join_new_buffer() {
        MutableCharArray foobar = MutableCharArray.of("foobar");
        CharArray foo = CharArray.of("foo");
        CharArray bar = CharArray.of("bar");

        assertThat(MutableCharArray.join(foo, bar)).isEqualTo(foobar);
        assertThat(MutableCharArray.join(foo, bar)._chars()).isNotSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.immutableCopy(), bar)._chars()).isNotSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo, bar.immutableCopy())._chars()).isNotSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.immutableCopy(), bar.immutableCopy())._chars()).isNotSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.mutable(), bar)._chars()).isNotSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo, bar.mutable())._chars()).isNotSameInstanceAs(foobar._chars());
        assertThat(MutableCharArray.join(foo.mutable(), bar.mutable())._chars()).isNotSameInstanceAs(foobar._chars());
    }
}
