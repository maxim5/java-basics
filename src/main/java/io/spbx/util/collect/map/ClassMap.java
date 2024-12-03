package io.spbx.util.collect.map;

import com.google.common.collect.ImmutableMap;
import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.collect.container.IntSize;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import static io.spbx.util.base.error.BasicExceptions.runOnlyInDev;
import static io.spbx.util.collect.map.BasicMaps.*;

public class ClassMap<T> extends AbstractMap<Class<?>, T> implements Map<Class<?>, T>, IntSize {
    private static final Logger log = Logger.forEnclosingClass();

    private final Map<Class<?>, T> map;

    public ClassMap(@NotNull Map<Class<?>, T> map) {
        this.map = map;
    }

    public static <T> @NotNull ClassMap<T> mutable() {
        return new ClassMap<>(newMutableMap());
    }

    public static <T> @NotNull ClassMap<T> ordered() {
        return new ClassMap<>(newOrderedMap());
    }

    public static <T> @NotNull ClassMap<T> concurrent() {
        return new ClassMap<>(newConcurrentMap());
    }

    public static <T> @NotNull ClassMap<T> immutableOf(@NotNull Map<Class<?>, T> map) {
        return new ClassMap<>(ImmutableMap.copyOf(map));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public @Nullable T get(Object key) {
        return map.get(key);
    }

    public @Nullable T getClass(@NotNull Class<?> key) {
        return map.get(key);
    }

    public @Nullable T getSuper(@NotNull Class<?> key) {
        return getClassOrSuper(key);
    }

    // https://stackoverflow.com/questions/32229528/inheritance-aware-class-to-value-map-to-replace-series-of-instanceof
    private @Nullable T getClassOrSuper(@Nullable Class<?> key) {
        T value = key != null ? map.get(key) : null;
        return value == null && key != null ? getClassOrSuper(key.getSuperclass()) : value;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @CanIgnoreReturnValue
    public T put(Class<?> key, T value) {
        T existing = map.put(key, value);
        assert existing != null || runOnlyInDev(() -> reportDifferentClassVersionsIfExist(key));
        return existing;
    }

    @CanIgnoreReturnValue
    public <V extends T> T putInstance(V value) {
        T existing = map.put(value.getClass(), value);
        assert existing != null || runOnlyInDev(() -> reportDifferentClassVersionsIfExist(value.getClass()));
        return existing;
    }

    @Override
    public @NotNull Set<Entry<Class<?>, T>> entrySet() {
        return map.entrySet();
    }

    private void reportDifferentClassVersionsIfExist(@NotNull Class<?> key) {
        map.keySet().stream().filter(klass -> klass != key && isSameName(klass, key)).forEach(klass -> {
            log.warn().log(
                "Found a different Class<?> instance with the identical name. " +
                "Could be a different Class<?> version or different class-loader: " +
                "exists=[name=%s id=%s classloader=%s] missing=[name=%s id=%s classloader=%s]",
                klass.getName(), System.identityHashCode(klass), klass.getClassLoader(),
                key.getName(), System.identityHashCode(key), key.getClassLoader()
            );
        });
    }

    private static boolean isSameName(@NotNull Class<?> first, @NotNull Class<?> second) {
        return first.getName().equals(second.getName());
    }
}
