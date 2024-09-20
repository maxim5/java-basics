package io.spbx.util.collect;

import io.spbx.util.array.CharArray;
import io.spbx.util.base.BasicStrings;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The {@link CharSequence}-key {@link Map} based on the key <em>content equivalence</em> rather than key equality.
 * <p>
 * If the {@link StringContentMap} implementation is hash-based, this means that it computes custom
 * {@code #hashCode()} and {@code #equals()} values
 * for the keys so that it only by takes into account the chars contents and ignores the key type.
 * <p>
 * For example, the following keys are <em>content</em> equivalent:
 * <ul>
 *     <li>{@code "foo"}</li>
 *     <li>{@code new StringBuilder("foo")}</li>
 *     <li>{@code CharArray.of("foo")}</li>
 *     <li>{@code CharArray.of("foobar").substring(0, 3)}</li>
 *     <li>{@code "foo".toCharArray()}</li>
 *     <li>etc ...</li>
 * </ul>
 *
 * <p>
 *
 * The implementation may or may not permit {@code null} keys or values,
 * in addition support case ignore/insensitivity, {@code intern} stored keys or do other optimizations.
 *
 * @see String#contentEquals(CharSequence)
 * @see String#equalsIgnoreCase(String)
 * @see String#intern()
 */
public interface StringContentMap<V> extends Map<CharSequence, V> {
    default @Nullable V get(char[] key) {
        return get(toCharSequence(key));
    }

    default @Nullable V get(CharSequence key) {
        return get((Object) key);
    }

    default @Nullable V getAscii(byte[] key) {
        return get(toCharSequence(key));
    }

    default @Nullable V put(char[] key, V value) {
        return put(BasicStrings.intern(toCharSequence(key)), value);
    }

    default @Nullable V putAscii(byte[] key, V value) {
        return put(BasicStrings.intern(toCharSequence(key)), value);
    }

    default V getOrDefault(CharSequence key, V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    default boolean containsKey(CharSequence key) {
        return containsKey((Object) key);
    }

    default @Nullable V remove(CharSequence key) {
        return remove((Object) key);
    }

    default boolean remove(CharSequence key, Object value) {
        return Map.super.remove(key, value);
    }

    static @Nullable CharSequence toCharSequence(@Nullable Object object) {
        return object == null ? null
            : object instanceof CharSequence sequence ? sequence
            : object instanceof char[] charArray ? CharArray.wrap(charArray)  // does not copy since it's not stored
            : object instanceof byte[] byteArray ? new String(byteArray)      // assume ASCII
            : object.toString();
    }

    static boolean contentEqual(@Nullable Object obj1, @Nullable Object obj2) {
        CharSequence seq1 = toCharSequence(obj1);
        CharSequence seq2 = toCharSequence(obj2);
        return seq1 == null || seq2 == null ? seq1 == seq2 : BasicStrings.contentEquals(seq1, seq2);
    }
}
