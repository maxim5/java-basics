package io.spbx.util.classpath;

import io.spbx.util.logging.Logger;
import io.spbx.util.time.TimeIt;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

/**
 * A {@link ClasspathScanner} adapter which times and logs time consumed for each operation.
 */
public class TimedClasspathScanner implements ClasspathScanner {
    private static final Logger log = Logger.forEnclosingClass();

    private final ClasspathScanner delegate;
    private final String name;

    protected TimedClasspathScanner(@NotNull ClasspathScanner delegate, @NotNull String name) {
        this.delegate = delegate;
        this.name = name;
    }

    @Override
    public void scan(@NotNull ClassNamePredicate classNamePredicate,
                     @NotNull ClassPredicate classPredicate,
                     @NotNull Consumer<Class<?>> consumer) {
        var counter = new Consumer<Class<?>>() {
            int count = 0;
            @Override
            public void accept(Class<?> klass) {
                count++;
            }
        };

        TimeIt
            .timeIt(() -> delegate.scan(classNamePredicate, classPredicate, consumer.andThen(counter)))
            .onDone(millis -> log.info().log("Found %d %s classes in %d ms", counter.count, name, millis));
    }

    @Override
    public @NotNull Set<Class<?>> scanToSet(@NotNull ClassNamePredicate classNamePredicate,
                                            @NotNull ClassPredicate classPredicate) {
        return TimeIt
            .timeIt(() -> delegate.scanToSet(classNamePredicate, classPredicate))
            .onDone((set, millis) -> log.info().log("Found %d %s classes in %d ms", set.size(), name, millis));
    }

    @Override
    public @NotNull ClasspathScanner timed(@NotNull String name) {
        return new TimedClasspathScanner(delegate, name);
    }
}
