package io.spbx.util.extern.guice;

import com.google.common.flogger.FluentLogger;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.util.Providers;
import io.spbx.util.collect.BasicMaps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;

import static io.spbx.util.base.EasyCast.castAny;

public class MoreGuice {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Injector injector;
    private final ConcurrentMap<Key<?>, Binding<?>> singletons = BasicMaps.newConcurrentMap();

    public MoreGuice(@NotNull Injector injector) {
        this.injector = injector;
    }

    public @NotNull Injector injector() {
        return injector;
    }

    public boolean bindingExists(@NotNull Class<?> klass) {
        return bindingExists(Key.get(klass));
    }

    public boolean bindingExists(@NotNull TypeLiteral<?> typeLiteral) {
        return bindingExists(Key.get(typeLiteral));
    }

    public boolean bindingExists(@NotNull Key<?> key) {
        return injector.getExistingBinding(key) != null;
    }

    public <T> @NotNull T getInstance(@NotNull Class<? extends T> klass) {
        return injector.getInstance(klass);
    }

    public <T> @NotNull T getInstance(@NotNull TypeLiteral<? extends T> typeLiteral) {
        return injector.getInstance(Key.get(typeLiteral));
    }

    public <T> @NotNull T getInstance(@NotNull Key<T> key) {
        return injector.getInstance(key);
    }

    public <T> @Nullable T getInstanceOrNull(@NotNull Class<? extends T> klass) {
        return getInstanceOrNull(Key.get(klass));
    }

    public <T> @Nullable T getInstanceOrNull(@NotNull TypeLiteral<? extends T> typeLiteral) {
        return getInstanceOrNull(Key.get(typeLiteral));
    }

    public <T> @Nullable T getInstanceOrNull(@NotNull Key<T> key) {
        Binding<T> existingBinding = injector.getExistingBinding(key);
        return existingBinding != null ? existingBinding.getProvider().get() : null;
    }

    public <T> @NotNull T getInstanceOrDefault(@NotNull Class<? extends T> klass,
                                               @NotNull Supplier<? extends T> defaultSupplier) {
        return getInstanceOrDefault(Key.get(klass), defaultSupplier);
    }

    public <T> @NotNull T getInstanceOrDefault(@NotNull TypeLiteral<? extends T> typeLiteral,
                                               @NotNull Supplier<? extends T> defaultSupplier) {
        return getInstanceOrDefault(Key.get(typeLiteral), defaultSupplier);
    }

    public <T> @NotNull T getInstanceOrDefault(@NotNull Key<? extends T> key,
                                               @NotNull Supplier<? extends T> defaultSupplier) {
        try {
            if (injector.getExistingBinding(key) != null) {
                Provider<? extends T> provider = injector.getProvider(key);
                log.at(Level.FINE).log("Found explicit %s Guice provider: %s", key, provider);
                return provider.get();
            }
        } catch (ConfigurationException e) {
            log.at(Level.FINEST).withCause(e).log("Failed to find provider for %s", key);
        }

        log.at(Level.FINE).log("Applying default %s supplier", key);
        return defaultSupplier.get();
    }

    public <T> @NotNull T injectMembers(@NotNull T instance) {
        injector.injectMembers(instance);
        return instance;
    }

    // Replacement for `injector.getInstance()` which guarantees singleton result.
    // See https://github.com/google/guice/issues/357
    public <T> @NotNull Binding<T> lazySingleton(@NotNull Key<T> key) {
        Binding<?> existing = injector.getExistingBinding(key);
        if (existing == null) {
            return castAny(singletons.computeIfAbsent(key, __ -> constantBinding(key, injector.getBinding(key))));
        }
        return castAny(existing);
    }

    public <T> @NotNull T lazySingleton(@NotNull Class<T> klass) {
        return lazySingleton(Key.get(klass)).getProvider().get();
    }

    // Replacement for `injector.getInstance()` which guarantees singleton result.
    public <T> @NotNull Binding<T> ensureSingleton(@NotNull Key<T> key) {
        return castAny(singletons.computeIfAbsent(key, __ -> constantBinding(key, injector.getBinding(key))));
    }

    public <T> @NotNull T ensureSingleton(@NotNull Class<T> klass) {
        return ensureSingleton(Key.get(klass)).getProvider().get();
    }

    public static <T, P> Provider<T> asProvider(@NotNull Class<P> klass, @NotNull Function<P, T> getter) {
        return new Provider<>() {
            @Inject private Injector injector;

            @Override public T get() {
                P instance = injector.getInstance(klass);
                return getter.apply(instance);
            }
        };
    }

    private static @NotNull <T> Binding<T> constantBinding(@NotNull Key<T> key, @NotNull Binding<T> binding) {
        T instance = binding.getProvider().get();
        return new Binding<>() {
            @Override public Key<T> getKey() {
                return key;
            }

            @Override public Provider<T> getProvider() {
                return Providers.of(instance);
            }

            @Override public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
                return binding.acceptTargetVisitor(visitor);
            }

            @Override public <V> V acceptScopingVisitor(BindingScopingVisitor<V> visitor) {
                return binding.acceptScopingVisitor(visitor);
            }

            @Override public Object getSource() {
                return binding.getSource();
            }

            @Override public <TT> TT acceptVisitor(ElementVisitor<TT> visitor) {
                return binding.acceptVisitor(visitor);
            }

            @Override public void applyTo(Binder binder) {
                binding.applyTo(binder);
            }
        };
    }
}
