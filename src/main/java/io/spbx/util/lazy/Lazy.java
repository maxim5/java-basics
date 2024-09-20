package io.spbx.util.lazy;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Allows to store a lazy non-null value. The clients only provide a way to supply it.
 * <p>
 * Designed use-cases and comparison to other caching utils:
 * <ul>
 *     <li>
 *         {@link CacheCompute} is designed as a caching wrapper of the parameter-less method implementation.
 *         The value provider is known and can be specified at the init time,
 *         but logically it belongs to the call-site rather than construction.
 *         Hence, {@link CacheCompute#getOrCompute} usually has just one call-site.
 *         The caching class implementation ensures the supplier is only going to be called once, and
 *         the client is not responsible for the taking care of the uninitialized state.
 *     </li>
 *     <li>
 *         {@link Lazy} ({@link LazyBoolean}, etc) is designed as a deferring wrapper of the field init.
 *         The value provider is known at the init-time, but has to be deferred.
 *         Access methods like {@link Lazy#get()} can be used in multiple call-sites.
 *         The lazy class implementation ensures the supplier is only going to be called once, and
 *         the client is not responsible for the taking care of the uninitialized state.
 *         <p>
 *         A special case {@link WeakLazy} can be used if the value is big, can be garbage collected
 *         and provided on demand. In this case, the supplier may be called many times.
 *     </li>
 *     <li>
 *         {@link LazyInit} is designed to defer field init from an external input.
 *         The value provider can not be set at the init-time, but the value can be accessed via
 *         {@link LazyInit#getOrDie()} in multiple call-sites.
 *         Hence, it has dedicated methods like {@link LazyInit#initializeOrDie} instead of the supplier,
 *         and the client is responsible for calling those methods correctly and dealing with uninitialized state.
 *     </li>
 *     <li>
 *         {@link com.google.common.base.Suppliers#memoize(com.google.common.base.Supplier)} is very similar to
 *         {@link Lazy}. {@code Suppliers#memoize()} allows to store nullable values and to serialize the supplier,
 *         while {@link Lazy} is more convenient for chaining lazy instances in a functional style.
 *     </li>
 * </ul>
 *
 * @see CacheCompute
 * @see LazyInit
 * @see Lazy
 * @see LazyBoolean
 * @see com.google.common.base.Suppliers#memoize(com.google.common.base.Supplier)
 */
@ThreadSafe
public class Lazy<T> implements Supplier<T> {
    protected final Supplier<T> supplier;
    protected final AtomicReference<T> ref = new AtomicReference<>(null);

    public Lazy(@NotNull Supplier<@NotNull T> supplier) {
        this.supplier = supplier;
    }

    public static <T> @NotNull Lazy<T> of(@NotNull Supplier<@NotNull T> supplier) {
        return supplier instanceof Lazy<T> lazy ? lazy : new Lazy<>(supplier);
    }

    @Override
    public @NotNull T get() {
        T value = ref.get();
        if (value == null) {
            value = supplier.get();
            if (!ref.compareAndSet(null, value)) {
                return ref.get();
            }
        }
        return value;
    }

    /**
     * Transforms this {@link Lazy} to a new {@link Lazy} via {@code func} and returns the derived instance.
     * Example:
     * {@snippet lang="java" :
     *     Lazy<Path> root = Lazy.of(this::getRootPath);
     *     Lazy<Path> dir = root.transform(path -> path.resolve("dir"));
     * }
     */
    public <R> @NotNull Lazy<R> transform(@NotNull Function<T, R> func) {
        return new Lazy<>(() -> func.apply(Objects.requireNonNullElseGet(ref.get(), this)));
    }

    /**
     * Transforms this {@link Lazy} to a {@link LazyBoolean} via {@code predicate} and returns the derived instance.
     * Example:
     * {@snippet lang="java" :
     *     Lazy<Path> root = Lazy.of(this::getRootPath);
     *     LazyBoolean exists = root.transformBool(Files::exists);
     * }
     */
    public @NotNull LazyBoolean transformBool(@NotNull Predicate<T> predicate) {
        return new LazyBoolean(() -> predicate.test(Objects.requireNonNullElseGet(ref.get(), this)));
    }
}
