package io.spbx.util.classpath;

import com.google.common.reflect.ClassPath;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A {@link ClasspathScanner} implementation based on Guava {@link ClassPath} utility.
 */
public class GuavaClasspathScanner implements ClasspathScanner {
    private static final Logger log = Logger.forEnclosingClass();

    protected final ClassPath classPath;

    protected GuavaClasspathScanner(@NotNull ClassPath classPath) {
        this.classPath = classPath;
    }

    public static @NotNull ClasspathScanner of(@NotNull ClassPath classPath) {
        return new GuavaClasspathScanner(classPath);
    }

    public static @NotNull ClasspathScanner fromClassLoader(@NotNull ClassLoader classLoader) {
        try {
            ClassPath classPath = ClassPath.from(classLoader);
            return GuavaClasspathScanner.of(classPath);
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }

    public static @NotNull ClasspathScanner fromSystemClassLoader() {
        return fromClassLoader(ClassLoader.getSystemClassLoader());
    }

    public static @NotNull ClasspathScanner fromDefaultClassLoader() {
        return fromClassLoader(GuavaClasspathScanner.class.getClassLoader());
    }

    @Override
    public void scan(@NotNull ClassNamePredicate classNamePredicate,
                     @NotNull ClassPredicate classPredicate,
                     @NotNull Consumer<Class<?>> consumer) {
        scanToSet(classNamePredicate, classPredicate).forEach(consumer);
    }

    @Override
    public @NotNull Set<Class<?>> scanToSet(@NotNull ClassNamePredicate classNamePredicate, @NotNull ClassPredicate classPredicate) {
        return classPath
            .getAllClasses()
            .stream()
            .filter(classInfo -> classNamePredicate.test(classInfo.getPackageName(), classInfo.getSimpleName()))
            .map((ClassPath.ClassInfo classInfo) -> {
                try {
                    return classInfo.load();
                } catch (NoClassDefFoundError e) {
                    log.warn().withCause(e).log("Failed to load class: %s.%s", classInfo.getPackageName(), classInfo.getName());
                    return null;
                }
            })
            .filter(klass -> klass != null && classPredicate.test(klass))
            .collect(Collectors.toSet());
    }
}
