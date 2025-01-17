package io.spbx.util.lifetime;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.error.BasicExceptions.IllegalArgumentExceptions;
import io.spbx.util.func.ThrowRunnable;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.Closeable;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Lifetime {
    // See https://stackoverflow.com/questions/48755164/referencing-subclass-in-static-variable
    // and https://stackoverflow.com/questions/50021182/java-static-initializers-referring-to-subclasses-avoid-class-loading-deadlock
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static final Lifetime Eternal = new Definition();

    public abstract @NotNull Status status();

    public @NotNull Lifetime.Definition createNested() {
        Definition child = new Definition();
        attach(child);
        return child;
    }

    public @NotNull Lifetime.Definition createNested(@NotNull Consumer<Definition> atomicAction) {
        Definition nested = createNested();
        try {
            nested.executeIfAlive(() -> atomicAction.accept(nested));
            return nested;
        } catch (Throwable throwable) {
            nested.terminate();
            throw throwable;
        }
    }

    public <T> @NotNull T usingNested(@NotNull Function<Lifetime, T> action) {
        Definition nested = createNested();
        try {
            return action.apply(nested);
        } finally {
            nested.terminate();
        }
    }

    public abstract void onTerminate(@NotNull Closeable closeable);

    protected abstract void attach(@NotNull Lifetime.Definition child);

    public static class Definition extends Lifetime {
        private static final Logger log = Logger.forEnclosingClass();

        private final ArrayDeque<Object> resources = new ArrayDeque<>();
        private final AtomicReference<Status> status = new AtomicReference<>(Status.Alive);

        @Override
        public @NotNull Status status() {
            return status.get();
        }

        @Override
        public void onTerminate(@NotNull Closeable closeable) {
            log.debug().log("Adding closeable for terminate: %s...", closeable);
            tryToAdd(closeable);
        }

        @Override
        protected void attach(@NotNull Lifetime.Definition child) {
            IllegalArgumentExceptions.failIf(child == Eternal, "Eternal lifetime can't be attached");
            tryToAdd(child);
        }

        @CanIgnoreReturnValue
        public boolean terminate() {
            if (this == Eternal) {
                return false;
            }
            if (status.compareAndSet(Status.Alive, Status.Terminating)) {
                deconstruct();
                status.set(Status.Terminated);
                return true;
            } else {
                if (status.get() == Status.Terminating) {
                    log.error().log("Lifetime is already terminating");
                }
                return false;
            }
        }

        public void terminateIfAlive() {
            if (status.get() == Status.Alive) {
                terminate();
            }
        }

        private void tryToAdd(@NotNull Object resource) {
            if (this == Eternal) {
                return;
            }
            if (status.get() == Status.Alive) {
                resources.addLast(resource);
            } else {
                log.warn().log("Failed to add a resource due to status=%s: %s", status.get(), resource);
            }
        }

        private void deconstruct() {
            while (!resources.isEmpty()) {
                Object resource = resources.pollLast();  // in reverse order: lowest on the stack added first
                log.info().log("Terminating %s...", resource);
                try {
                    deconstruct(resource);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    log.warn().withCause(throwable).log("Runnable failed: %s", throwable.getMessage());
                }
            }
        }

        @VisibleForTesting
        void deconstruct(@Nullable Object resource) throws Throwable {
            if (resource instanceof Closeable closeable) {
                closeable.close();
            } else if (resource instanceof ThrowRunnable<?> runnable) {
                runnable.run();
            } else if (resource instanceof Definition definition) {
                definition.terminate();
            } else {
                log.error().log("Failed to terminal unexpected resource: %s", resource);
            }
        }

        public <E extends Throwable> void executeIfAlive(@NotNull ThrowRunnable<E> runnable) throws E {
            if (status.compareAndSet(Status.Alive, Status.Alive)) {
                runnable.run();
            }
        }
    }

    public enum Status {
        Alive,
        Terminating,
        Terminated,
    }
}
