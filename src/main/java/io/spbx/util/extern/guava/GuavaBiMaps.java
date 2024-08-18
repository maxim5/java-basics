package io.spbx.util.extern.guava;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.spbx.util.base.BasicExceptions.IllegalArgumentExceptions;
import io.spbx.util.func.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.Collector;

import static io.spbx.util.func.ScopeFunctions.alsoRun;

public class GuavaBiMaps {
    public static <E, K, V> @NotNull Collector<E, ?, HashBiMap<K, V>> toHashBiMap(
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc,
            @NotNull BiMapPutMethod<K, V> putMethod) {
        return Collector.of(
            HashBiMap::create,
            (bimap, item) -> putMethod.accept(bimap, keyFunc.apply(item), valueFunc.apply(item)),
            (bimap1, bimap2) -> alsoRun(bimap1, () -> bimap2.forEach((key, val) -> putMethod.accept(bimap1, key, val)))
        );
    }

    public interface BiMapPutMethod<K, V> extends TriConsumer<BiMap<K, V>, K, V> {
        static <K, V> @NotNull BiMapPutMethod<K, V> overwrite() {
            return BiMap::forcePut;
        }

        static <K, V> @NotNull BiMapPutMethod<K, V> throwing() {
            return (biMap, k, v) -> {
                V existing = biMap.put(k, v);
                IllegalArgumentExceptions.assure(existing == null, "key already present: %s", k);
            };
        }
    }
}
