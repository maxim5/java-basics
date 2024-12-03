package io.spbx.util.collect.stream;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.func.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.base.error.BasicExceptions.newIllegalStateException;
import static io.spbx.util.base.lang.EasyCast.castAny;
import static java.util.Objects.requireNonNull;

@Stateless
@Pure
@CheckReturnValue
public class BasicCollectors {
    /* `Map` collectors */

    public static <T, K, V, M extends Map<K, V>> @NotNull Collector<T, ?, M> toMap(
            @NotNull Function<? super T, ? extends K> keyFunc,
            @NotNull Function<? super T, ? extends V> valueFunc,
            @NotNull Supplier<? extends M> mapSupplier) {
        return toMap(keyFunc, valueFunc, mapSupplier, BasicCollectors::throwingMergerExtended);
    }

    public static <T, K, V, M extends Map<K, V>> @NotNull Collector<T, ?, M> toMap(
            @NotNull Function<? super T, ? extends K> keyFunc,
            @NotNull Function<? super T, ? extends V> valueFunc,
            @NotNull BinaryOperator<V> mergeFunc,
            @NotNull Supplier<? extends M> mapSupplier) {
        return toMap(keyFunc, valueFunc, mapSupplier, (key, val1, val2) -> mergeFunc.apply(val1, val2));
    }

    private static <K, V> V throwingMergerExtended(K key, V val1, V val2) {
        throw newIllegalStateException("Duplicate values `%s` and `%s` for the same key `%s`", val1, val2, key);
    }

    private static <T, K, V, M extends Map<K, V>> @NotNull Collector<T, ?, M> toMap(
            @NotNull Function<? super T, ? extends K> keyFunc,
            @NotNull Function<? super T, ? extends V> valueFunc,
            @NotNull Supplier<? extends M> mapSupplier,
            @NotNull TriFunction<K, V, V, V> mergeFuncExtended) {
        final class Builder {
            private final M map = requireNonNull(mapSupplier.get(), "mapSupplier must not return null");
            private boolean hasNull;

            void add(K key, V value) {
                V existing;
                if (hasNull) {  // Existence of null values requires 2 lookups to check for duplicates.
                    if ((existing = map.get(key)) != null) {
                        value = mergeFuncExtended.apply(key, existing, value);
                    }
                    map.put(key, value);
                } else {  // The Map doesn't have null. putIfAbsent() == null means no duplicates.
                    if ((existing = map.putIfAbsent(key, value)) != null) {
                        value = mergeFuncExtended.apply(key, existing, value);
                    }
                    if (value == null) {
                        hasNull = true;
                    }
                }
            }

            void add(T input) {
                add(keyFunc.apply(input), valueFunc.apply(input));
            }

            Builder addAll(Builder that) {
                that.map.forEach(this::add);
                return this;
            }

            M build() {
                return map;
            }
        }
        return Collector.of(Builder::new, Builder::add, Builder::addAll, Builder::build);
    }

    public static class MapMergers {
        public static <V> @NotNull BinaryOperator<V> ignoreDuplicates() {
            return (u, v) -> u;
        }

        public static <V> @NotNull BinaryOperator<V> overwrite() {
            return (u, v) -> v;
        }

        public static <V> @NotNull BinaryOperator<V> throwing() {
            return (u, v) -> {
                throw newIllegalStateException("Duplicate values `%s` and `%s` for the same key", u, v);
            };
        }
    }

    /* Object collectors */

    private static final Object MORE_THAN_ONE_ITEM = new Object();
    private static final Collector<Object, ?, Optional<Object>> ONLY_ITEM_OR_EMPTY = Collector.of(
        AtomicReference::new,
        (ref, obj) -> {
            if (obj != null) {
                ref.updateAndGet(cur -> cur != null ? MORE_THAN_ONE_ITEM : obj);
            }
        },
        (ref1, ref2) -> {
            Object o2 = ref2.get();
            ref1.updateAndGet(o1 -> o1 == null ? o2 : (o1 == MORE_THAN_ONE_ITEM || o2 != null ? MORE_THAN_ONE_ITEM : o1));
            return ref1;
        },
        ref -> Optional.ofNullable(ref.get() == MORE_THAN_ONE_ITEM ? null : ref.get())
    );

    /**
     * Returns a stream collector that extracts a single non-null item from the stream or empty optional otherwise.
     * Treats null items as if they didn't appear in the stream.
     * <p>
     * Unlike {@link com.google.common.collect.MoreCollectors#onlyElement}, it doesn't fail in case of stream mismatch
     * (too few or too many elements in the stream).
     *
     * @see com.google.common.collect.MoreCollectors#onlyElement
     */
    public static <E> @NotNull Collector<E, ?, Optional<E>> toOnlyNonNullOrEmpty() {
        return castAny(ONLY_ITEM_OR_EMPTY);
    }

    public static <E> boolean allEqual(@NotNull Stream<E> stream) {
        return stream.distinct().limit(2).count() <= 1;
    }

    /* Primitive collectors */

    public static @NotNull Collector<Integer, ?, byte[]> toByteArray() {
        // https://stackoverflow.com/questions/44708532/how-to-map-and-collect-primitive-return-type-using-java-8-stream
        return Collector.of(ByteArrayOutputStream::new, ByteArrayOutputStream::write, (baos1, baos2) -> {
            Unchecked.Runnables.runRethrow(() -> baos2.writeTo(baos1));
            return baos1;
        }, ByteArrayOutputStream::toByteArray);
    }
}
