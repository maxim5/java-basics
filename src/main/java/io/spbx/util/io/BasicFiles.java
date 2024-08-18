package io.spbx.util.io;

import com.google.errorprone.annotations.MustBeClosed;
import io.spbx.util.base.DataSize;
import io.spbx.util.base.BasicHash;
import io.spbx.util.func.Chains;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.spbx.util.base.Unchecked.LongSuppliers.runRethrow;
import static io.spbx.util.base.Unchecked.Runnables.runRethrow;
import static io.spbx.util.base.Unchecked.Suppliers.runRethrow;

public class BasicFiles {
    /* `String` and `Path` manipulations */

    public static @NotNull String getFileExtension(@NotNull String filename) {
        int dotIndex = filename.indexOf('.');
        return dotIndex == -1 ? "" : filename.substring(dotIndex + 1);
    }

    public static @NotNull String cutOffFileExtension(@NotNull String filename) {
        int dotIndex = filename.indexOf('.');
        return dotIndex == -1 ? filename : filename.substring(0, dotIndex);
    }

    public static @NotNull Path commonPath(@NotNull Path path1, @NotNull Path path2) {
        // https://stackoverflow.com/questions/54595752/find-the-longest-path-common-to-two-paths-in-java
        assert path1.isAbsolute() == path2.isAbsolute() : "Paths have different types: %s and %s".formatted(path1, path2);
        Path relative = path1.relativize(path2).normalize();
        while (relative != null && !relative.endsWith("..")) {
            relative = relative.getParent();
        }
        return relative != null ? path1.resolve(relative).normalize() : path1;
    }

    /* Read and write files */

    @MustBeClosed
    public static @NotNull InputStream newInputStream(@NotNull Path path) {
        return runRethrow(() -> Files.newInputStream(path));
    }

    public static byte @NotNull[] readAllBytesOrDie(@NotNull Path path) {
        return runRethrow(() -> Files.readAllBytes(path));
    }

    public static byte @Nullable[] readAllBytesOrNull(@NotNull Path path) {
        return Files.exists(path) ? BasicFiles.readAllBytesOrDie(path) : null;
    }

    public static void writeBytes(@NotNull Path path, byte @NotNull[] bytes) {
        runRethrow(() -> Files.write(path, bytes));
    }

    public static @NotNull List<String> readAllLinesOrDie(@NotNull Path path) {
        return runRethrow(() -> Files.readAllLines(path));
    }

    public static @Nullable List<String> readAllLinesOrNull(@NotNull Path path) {
        return Files.exists(path) ? BasicFiles.readAllLinesOrDie(path) : null;
    }

    public static @NotNull String readStringOrDie(@NotNull Path path) {
        return runRethrow(() -> Files.readString(path));
    }

    public static @Nullable String readStringOrNull(@NotNull Path path) {
        return Files.exists(path) ? BasicFiles.readStringOrDie(path) : null;
    }

    public static void writeAllLines(@NotNull Path path, @NotNull Iterable<String> lines) {
        runRethrow(() -> Files.write(path, lines));
    }

    public static void writeAllLines(@NotNull Path path, @NotNull Iterable<String> lines, @NotNull Charset charset) {
        runRethrow(() -> Files.write(path, lines, charset));
    }

    /**
     * Returns the hash of the file content using specified algorithm.
     * @throws UncheckedIOException if I/O error occurs
     */
    public static byte @NotNull[] digest(@NotNull Path path, @NotNull MessageDigest digest) {
        byte[] bytes = BasicFiles.readAllBytesOrNull(path);
        return digest.digest(bytes != null ? bytes : new byte[0]);
    }

    /**
     * Returns the MD5 hash of the file content.
     * @throws UncheckedIOException if I/O error occurs
     */
    public static byte @NotNull[] md5(@NotNull Path path) {
        return digest(path, BasicHash.md5());
    }

    /* File attributes and metadata */

    public static long fileSize(@NotNull Path path) {
        return runRethrow(() -> Files.size(path));
    }

    public static @NotNull DataSize dataSize(@NotNull Path path) {
        return DataSize.ofBytes(fileSize(path));
    }

    public static @NotNull FileTime lastModifiedTime(@NotNull Path path) {
        return runRethrow(() -> Files.getLastModifiedTime(path));
    }

    public static long lastModifiedMillis(@NotNull Path path) {
        return lastModifiedTime(path).toMillis();
    }

    /* File operations */

    public static @NotNull Path createDir(@NotNull Path path) {
        return runRethrow(() -> Files.createDirectory(path));
    }

    public static @NotNull Path createDirs(@NotNull Path path) {
        return runRethrow(() -> Files.createDirectories(path));
    }

    public static void copy(@NotNull Path source, @NotNull Path target) {
        runRethrow(() -> Files.copy(source, target));
    }

    public static void copyForceOverwrite(@NotNull Path source, @NotNull Path target) {
        runRethrow(() -> Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING));
    }

    public static void delete(@NotNull Path path) {
        runRethrow(() -> Files.delete(path));
    }

    /* File walk */

    public static void walkDirectories(@NotNull Path root, @NotNull Consumer<Path> callback) {
        walk(root, Chains.chain(Files::isDirectory, callback));
    }

    public static void walkRegularFiles(@NotNull Path root, @NotNull Consumer<Path> callback) {
        walk(root, Chains.chain(Files::isRegularFile, callback));
    }

    public static void walk(@NotNull Path root, @NotNull WalkCallback callback) {
        walk(root, path -> {
            if (Files.isDirectory(path)) {
                callback.onDirectory(path);
            }
            if (Files.isRegularFile(path)) {
                callback.onFile(path);
            }
        });
    }

    public static void walk(@NotNull Path root, @NotNull Consumer<Path> callback) {
        try (Stream<Path> walk = walk(root)) {
            walk.forEach(callback);
        }
    }

    public static @NotNull List<Path> walkAndFilter(@NotNull Path root, @NotNull Predicate<Path> predicate) {
        try (Stream<Path> walk = walk(root)) {
            return walk.filter(predicate).toList();
        }
    }

    public static @NotNull Stream<Path> walk(@NotNull Path root) {
        return runRethrow(() -> Files.walk(root));
    }

    public static @Nullable Path walkUp(@NotNull Path root, @NotNull Predicate<Path> predicate) {
        Path current = root;
        while (current != null && !predicate.test(current)) {
            current = current.getParent();
        }
        return current;
    }

    public interface WalkCallback {
        void onDirectory(@NotNull Path path);
        void onFile(@NotNull Path path);
    }

    /* Parents and children */

    public static boolean isParentOf(@NotNull Path parent, @NotNull Path child) {
        return child.normalize().startsWith(parent.normalize());
    }

    public static @NotNull Stream<Path> listChildren(@NotNull Path path) {
        return runRethrow(() -> Files.list(path));
    }
}
