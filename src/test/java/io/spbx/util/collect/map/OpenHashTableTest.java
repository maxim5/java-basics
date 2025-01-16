package io.spbx.util.collect.map;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.mapOf;

@Tag("fast")
public class OpenHashTableTest {
    @Test
    public void ops_simple() {
        OpenHashTable<String, Integer> table = new OpenHashTable<>();
        assertTable(table).hasSize(0).isEmpty().hasContent("foo", null);

        table.put("foo", 1);
        assertTable(table).hasSize(1).hasContent("foo", 1, "bar", null);

        table.put("foo", 2);
        assertTable(table).hasSize(1).hasContent("foo", 2, "bar", null);

        table.put("bar", 3);
        assertTable(table).hasSize(2).hasContent("foo", 2, "bar", 3, "baz", null);

        table.remove("foo");
        assertTable(table).hasSize(1).hasContent("foo", null, "bar", 3);

        table.put(null, 4);
        assertTable(table).hasSize(2).hasContent("foo", null, "bar", 3, null, 4);

        table.put("baz", 5);
        assertTable(table).hasSize(3).hasContent("foo", null, "bar", 3, null, 4, "baz", 5);
    }

    @CheckReturnValue
    private static <K, V> @NotNull OpenHashTableSubject<K, V> assertTable(@NotNull OpenHashTable<K, V> table) {
        return new OpenHashTableSubject<>(table);
    }

    @CanIgnoreReturnValue
    private record OpenHashTableSubject<K, V>(@NotNull OpenHashTable<K, V> table) {
        public @NotNull OpenHashTableSubject<K, V> isEmpty() {
            return hasContent(mapOf());
        }

        public @NotNull OpenHashTableSubject<K, V> hasContent(K key, V val) {
            return hasContent(mapOf(key, val));
        }

        public @NotNull OpenHashTableSubject<K, V> hasContent(K key1, V val1, K key2, V val2) {
            return hasContent(mapOf(key1, val1, key2, val2));
        }

        public @NotNull OpenHashTableSubject<K, V> hasContent(K key1, V val1, K key2, V val2, K key3, V val3) {
            return hasContent(mapOf(key1, val1, key2, val2, key3, val3));
        }

        public @NotNull OpenHashTableSubject<K, V> hasContent(K key1, V val1, K key2, V val2,
                                                              K key3, V val3, K key4, V val4) {
            return hasContent(mapOf(key1, val1, key2, val2, key3, val3, key4, val4));
        }

        public @NotNull OpenHashTableSubject<K, V> hasSize(int size) {
            assertThat(table.size()).isEqualTo(size);
            assertThat(table.isEmpty()).isEqualTo(size == 0);
            assertThat(table.isNotEmpty()).isEqualTo(size > 0);
            return this;
        }

        public @NotNull OpenHashTableSubject<K, V> hasContent(@NotNull Map<K, V> map) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                assertThat(table.get(entry.getKey())).isEqualTo(entry.getValue());
            }
            return this;
        }
    }
}
