package io.spbx.util.extern.trove;

import io.spbx.util.base.CharArray;
import io.spbx.util.extern.trove.TroveHashMaps.StringContentHashMap;
import io.spbx.util.extern.trove.TroveHashMaps.StringHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@SuppressWarnings("SuspiciousMethodCalls")
public class TroveHashMapsTest {
    @Test
    public void string_hash_map_simple() {
        StringHashMap<String> map = StringHashMap.newMap();
        map.put("foo", "bar");
        assertThat(map.get("foo")).isEqualTo("bar");
        assertThat(map.get("Foo")).isNull();
    }

    @Test
    public void string_hash_map_ignore_case() {
        StringHashMap<String> map = StringHashMap.newIgnoreCaseMap();
        map.put("foo", "bar");
        assertThat(map.get("foo")).isEqualTo("bar");
        assertThat(map.get("Foo")).isEqualTo("bar");
    }

    @Test
    public void string_hash_map_null_keys() {
        StringHashMap<String> map = StringHashMap.newAllowingNullsMap();
        map.put(null, "bar");
        assertThat(map.get(null)).isEqualTo("bar");
    }

    @Test
    public void string_content_hash_map_simple() {
        StringContentHashMap<String> map = StringContentHashMap.newMap();
        map.put("foo", "bar");
        assertThat(map.get((Object) "foo")).isEqualTo("bar");
        assertThat(map.get((CharSequence) "foo")).isEqualTo("bar");
        assertThat(map.get("foo".toCharArray())).isEqualTo("bar");
        assertThat(map.get("foo".getBytes())).isEqualTo("bar");
        assertThat(map.getAscii("foo".getBytes())).isEqualTo("bar");
        assertThat(map.get(CharArray.of("foo"))).isEqualTo("bar");
        assertThat(map.get(CharArray.of("foobar").substringUntil(3))).isEqualTo("bar");
        assertThat(map.get(new StringBuilder("foo"))).isEqualTo("bar");
        assertThat(map.get(dummyWithCustomToString("foo"))).isEqualTo("bar");
    }

    @Test
    public void string_content_hash_map_null_value() {
        StringContentHashMap<String> map = StringContentHashMap.newMap();
        map.put("foo", null);
        assertThat(map.get((Object) "foo")).isNull();
        assertThat(map.get((CharSequence) "foo")).isNull();
        assertThat(map.get("foo".toCharArray())).isNull();
        assertThat(map.get("foo".getBytes())).isNull();
        assertThat(map.getAscii("foo".getBytes())).isNull();
        assertThat(map.get(CharArray.of("foo"))).isNull();
        assertThat(map.get(CharArray.of("foobar").substringUntil(3))).isNull();
        assertThat(map.get(new StringBuilder("foo"))).isNull();
        assertThat(map.get(dummyWithCustomToString("foo"))).isNull();
    }

    @Test
    public void string_content_hash_map_null_key() {
        StringContentHashMap<String> map = StringContentHashMap.newAllowingNullsMap();
        map.put((CharSequence) null, "bar");
        assertThat(map.get((Object) null)).isEqualTo("bar");
        assertThat(map.get((CharSequence) null)).isEqualTo("bar");
        assertThat(map.get((char[]) null)).isEqualTo("bar");
        assertThat(map.get((byte[]) null)).isEqualTo("bar");
        assertThat(map.getAscii(null)).isEqualTo("bar");
        assertThat(map.get((CharArray) null)).isEqualTo("bar");
        assertThat(map.get((StringBuilder) null)).isEqualTo("bar");
        assertThat(map.get(dummyWithCustomToString(null))).isEqualTo("bar");
    }

    @Test
    public void string_content_hash_map_ignore_case() {
        StringContentHashMap<String> map = StringContentHashMap.newIgnoreCaseMap();
        map.put("foo", "bar");
        assertThat(map.get((Object) "Foo")).isEqualTo("bar");
        assertThat(map.get((CharSequence) "Foo")).isEqualTo("bar");
        assertThat(map.get("Foo".toCharArray())).isEqualTo("bar");
        assertThat(map.get("Foo".getBytes())).isEqualTo("bar");
        assertThat(map.getAscii("Foo".getBytes())).isEqualTo("bar");
        assertThat(map.get(CharArray.of("Foo"))).isEqualTo("bar");
        assertThat(map.get(CharArray.of("FooBar").substringUntil(3))).isEqualTo("bar");
        assertThat(map.get(new StringBuilder("Foo"))).isEqualTo("bar");
        assertThat(map.get(dummyWithCustomToString("Foo"))).isEqualTo("bar");
    }

    private static @NotNull Object dummyWithCustomToString(@Nullable String value) {
        return new Object() {
            @Override public String toString() {
                return value;
            }
        };
    }
}
