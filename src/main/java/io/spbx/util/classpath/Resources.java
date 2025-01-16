package io.spbx.util.classpath;

import io.spbx.util.base.annotate.MustBeClosed;
import io.spbx.util.base.error.BasicExceptions.IllegalArgumentExceptions;
import io.spbx.util.base.error.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.io.BasicFiles;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Resources {
    public static final Predicate<String> IGNORE_CLASSES = s -> !s.endsWith(".class");

    private final Class<?> klass;

    Resources(@NotNull Class<?> klass) {
        this.klass = klass;
    }

    // FIX[minor]: support ofCaller()
    public static @NotNull Resources of(@NotNull Class<?> klass) {
        return new Resources(klass);
    }

    public @NotNull List<String> listAll() {
        return listAll(IGNORE_CLASSES);
    }

    public @NotNull List<String> listAll(@NotNull Predicate<String> filter) {
        try (Location location = resourcesOf(klass);
             Stream<Path> walk = Files.walk(location.path)) {
            return walk
                .filter(Files::isRegularFile)
                .map(location::relativize)
                .map(BasicFiles::forceUnixSlashes)
                .filter(filter)
                .toList();
        } catch (Exception e) {
            return Unchecked.rethrow(e);
        }
    }

    @MustBeClosed
    public @NotNull InputStream open(@NotNull String name) {
        try (Location location = resourcesOf(klass)) {
            if (location.isJar()) {
                String absolute = toAbsolute(name);
                return IllegalArgumentExceptions.assureNonNull(klass.getResourceAsStream(absolute),
                                                               "Resource not found:", absolute);
            }
            return Files.newInputStream(location.resolve(name));
        } catch (Exception e) {
            return Unchecked.rethrow(e);
        }
    }

    public byte @NotNull[] read(@NotNull String name) {
        try (Location location = resourcesOf(klass);
             InputStream inputStream = Files.newInputStream(location.resolve(name))) {
            return inputStream.readAllBytes();
        } catch (Exception e) {
            return Unchecked.rethrow(e);
        }
    }

    private static @NotNull Location resourcesOf(@NotNull Class<?> klass) throws IOException, URISyntaxException {
        // https://stackoverflow.com/questions/1429172/how-to-list-the-files-inside-a-jar-file
        CodeSource codeSource = IllegalStateExceptions.assureNonNull(klass.getProtectionDomain().getCodeSource(),
                                                                     "No code source for class %s available", klass);
        URL location = IllegalStateExceptions.assureNonNull(codeSource.getLocation(),
                                                            "No location URL for class %s available", klass);
        if (isJar(location)) {
            // https://stackoverflow.com/questions/25396918/how-to-construct-a-path-from-a-jarfile-url
            URI uri = new URI("jar", location.toString(), null);
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Map.of(), klass.getClassLoader());
            Path path = fileSystem.getPath("/");
            return new Location(location, path, fileSystem);
        } else {
            Path path = Paths.get(location.toURI());
            return new Location(location, path, () -> {});
        }
    }

    private record Location(@NotNull URL location, @NotNull Path path, @NotNull Closeable closeable) implements Closeable {
        public @NotNull Path resolve(@NotNull String path) {
            return this.path.resolve(Resources.toRelative(path));
        }

        public @NotNull String relativize(@NotNull Path path) {
            return this.path.relativize(path).toString();
        }

        public boolean isJar() {
            return Resources.isJar(location);
        }

        @Override public void close() throws IOException {
            closeable.close();
        }
    }

    private static boolean isJar(@NotNull URL location) {
        return location.getPath().endsWith(".jar");
    }

    private static @NotNull String toRelative(@NotNull String path) {
        return BasicStrings.removePrefix(BasicFiles.forceUnixSlashes(path), '/');
    }

    private static @NotNull String toAbsolute(@NotNull String name) {
        return BasicStrings.ensurePrefix(BasicFiles.forceUnixSlashes(name), '/');
    }
}
