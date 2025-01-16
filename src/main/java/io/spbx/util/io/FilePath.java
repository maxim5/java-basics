package io.spbx.util.io;

import io.spbx.util.collect.iter.BasicIterables;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public record FilePath(@NotNull Path path) implements Path {
    public static @NotNull FilePath of(@NotNull Path path) {
        return path instanceof FilePath filePath ? filePath : new FilePath(path);
    }

    public static @NotNull FilePath of(@NotNull String first, @NotNull String @NotNull... more) {
        return FilePath.of(Path.of(first, more));
    }

    public static @NotNull FilePath of(@NotNull URI uri) {
        return FilePath.of(Path.of(uri));
    }

    public boolean exists(@NotNull LinkOption @NotNull... options) {
        return Files.exists(path, options);
    }

    public boolean notExists(@NotNull LinkOption @NotNull... options) {
        return Files.notExists(path, options);
    }

    public boolean isRegularFile(@NotNull LinkOption @NotNull... options) {
        return Files.isRegularFile(path, options);
    }

    public boolean isDirectory(@NotNull LinkOption @NotNull... options) {
        return Files.isDirectory(path, options);
    }

    public boolean isReadable() {
        return Files.isReadable(path);
    }

    public @NotNull FilePath createDir() {
        BasicFiles.createDir(path);
        return this;
    }

    public @NotNull FilePath createDirs() {
        BasicFiles.createDirs(path);
        return this;
    }

    public @NotNull FilePath createFile() {
        BasicFiles.createFile(path);
        return this;
    }

    public @NotNull FilePath createFileIfNotExists() {
        BasicFiles.createFileIfNotExists(path);
        return this;
    }

    public void delete() throws UncheckedIOException {
        BasicFiles.delete(path);
    }

    public void deleteIfExists() throws UncheckedIOException {
        BasicFiles.deleteIfExists(path);
    }

    public void deleteDirectory() throws UncheckedIOException {
        BasicFiles.deleteDirectory(path);
    }

    public long fileSize() throws UncheckedIOException {
        return BasicFiles.fileSize(path);
    }

    @Override
    public @NotNull FileSystem getFileSystem() {
        return new FileSystemImpl(path.getFileSystem());
    }

    @Override
    public boolean isAbsolute() {
        return path.isAbsolute();
    }

    @Override
    public @NotNull FilePath getRoot() {
        return FilePath.of(path.getRoot());
    }

    @Override
    public @NotNull FilePath getFileName() {
        return FilePath.of(path.getFileName());
    }

    @Override
    public @NotNull FilePath getParent() {
        return FilePath.of(path.getParent());
    }

    @Override
    public int getNameCount() {
        return path.getNameCount();
    }

    @Override
    public @NotNull FilePath getName(int index) {
        return FilePath.of(path.getName(index));
    }

    @Override
    public @NotNull FilePath subpath(int beginIndex, int endIndex) {
        return FilePath.of(path.subpath(beginIndex, endIndex));
    }

    @Override
    public boolean startsWith(@NotNull Path other) {
        return path.startsWith(other);
    }

    @Override
    public boolean startsWith(@NotNull String other) {
        return path.startsWith(other);
    }

    @Override
    public boolean endsWith(@NotNull Path other) {
        return path.endsWith(other);
    }

    @Override
    public boolean endsWith(@NotNull String other) {
        return path.endsWith(other);
    }

    @Override
    public @NotNull FilePath normalize() {
        return FilePath.of(path.normalize());
    }

    @Override
    public @NotNull FilePath resolve(@NotNull Path other) {
        return FilePath.of(path.resolve(other));
    }

    @Override
    public @NotNull FilePath resolve(@NotNull String other) {
        return FilePath.of(path.resolve(other));
    }

    @Override
    public @NotNull FilePath resolveSibling(@NotNull Path other) {
        return FilePath.of(path.resolveSibling(other));
    }

    @Override
    public @NotNull FilePath resolveSibling(@NotNull String other) {
        return FilePath.of(path.resolveSibling(other));
    }

    @Override
    public @NotNull FilePath relativize(@NotNull Path other) {
        return FilePath.of(path.relativize(other));
    }

    @Override
    public @NotNull URI toUri() {
        return path.toUri();
    }

    @Override
    public @NotNull FilePath toAbsolutePath() {
        return FilePath.of(path.toAbsolutePath());
    }

    @Override
    public @NotNull FilePath toRealPath(@NotNull LinkOption @NotNull... options) throws IOException {
        return FilePath.of(path.toRealPath(options));
    }

    @Override
    public @NotNull File toFile() {
        return path.toFile();
    }

    @Override
    public @NotNull WatchKey register(@NotNull WatchService watcher,
                                      @NotNull WatchEvent.Kind<?> @NotNull[] events,
                                      @NotNull WatchEvent.Modifier @NotNull... modifiers) throws IOException {
        return path.register(watcher, events, modifiers);
    }

    @Override
    public @NotNull WatchKey register(@NotNull WatchService watcher,
                                      @NotNull WatchEvent.Kind<?> @NotNull... events) throws IOException {
        return path.register(watcher, events);
    }

    @Override
    public @NotNull Iterator<Path> iterator() {
        return BasicIterables.map(path.iterator(), FilePath::new);
    }

    @Override
    public int compareTo(@NotNull Path other) {
        return path.compareTo(other);
    }

    @Override
    public @NotNull String toString() {
        return path.toString();
    }

    static final class FileSystemImpl extends FileSystem {
        private final FileSystem delegate;

        FileSystemImpl(@NotNull FileSystem delegate) {
            this.delegate = delegate;
        }

        @Override
        public FileSystemProvider provider() {
            return new FileSystemProviderImpl(delegate.provider());
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        @Override
        public boolean isOpen() {
            return delegate.isOpen();
        }

        @Override
        public boolean isReadOnly() {
            return delegate.isReadOnly();
        }

        @Override
        public String getSeparator() {
            return delegate.getSeparator();
        }

        @Override
        public Iterable<Path> getRootDirectories() {
            return BasicIterables.map(delegate.getRootDirectories(), FilePath::new);
        }

        @Override
        public Iterable<FileStore> getFileStores() {
            return delegate.getFileStores();
        }

        @Override
        public Set<String> supportedFileAttributeViews() {
            return delegate.supportedFileAttributeViews();
        }

        @Override
        public @NotNull FilePath getPath(@NotNull String first, @NotNull String @NotNull... more) {
            return FilePath.of(delegate.getPath(first, more));
        }

        @Override
        public PathMatcher getPathMatcher(String syntaxAndPattern) {
            return delegate.getPathMatcher(syntaxAndPattern);
        }

        @Override
        public UserPrincipalLookupService getUserPrincipalLookupService() {
            return delegate.getUserPrincipalLookupService();
        }

        @Override
        public WatchService newWatchService() throws IOException {
            return delegate.newWatchService();
        }
    }

    static final class FileSystemProviderImpl extends FileSystemProvider {
        private final FileSystemProvider delegate;

        FileSystemProviderImpl(@NotNull FileSystemProvider delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getScheme() {
            return delegate.getScheme();
        }

        @Override
        public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
            return new FileSystemImpl(delegate.newFileSystem(uri, env));
        }

        @Override
        public FileSystem getFileSystem(URI uri) {
            return new FileSystemImpl(delegate.getFileSystem(uri));
        }

        @Override
        public @NotNull FilePath getPath(@NotNull URI uri) {
            return FilePath.of(delegate.getPath(uri));
        }

        @Override
        public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
            return new FileSystemImpl(delegate.newFileSystem(unwrap(path), env));
        }

        @Override
        public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
            return delegate.newInputStream(unwrap(path), options);
        }

        @Override
        public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
            return delegate.newOutputStream(unwrap(path), options);
        }

        @Override
        public FileChannel newFileChannel(Path path,
                                          Set<? extends OpenOption> options,
                                          FileAttribute<?>... attrs) throws IOException {
            return delegate.newFileChannel(unwrap(path), options, attrs);
        }

        @Override
        public AsynchronousFileChannel newAsynchronousFileChannel(Path path,
                                                                  Set<? extends OpenOption> options,
                                                                  ExecutorService executor,
                                                                  FileAttribute<?>... attrs) throws IOException {
            return delegate.newAsynchronousFileChannel(unwrap(path), options, executor, attrs);
        }

        @Override
        public SeekableByteChannel newByteChannel(Path path,
                                                  Set<? extends OpenOption> options,
                                                  FileAttribute<?>... attrs) throws IOException {
            return delegate.newByteChannel(unwrap(path), options, attrs);
        }

        @Override
        public DirectoryStream<Path> newDirectoryStream(Path dir,
                                                        DirectoryStream.Filter<? super Path> filter) throws IOException {
            return delegate.newDirectoryStream(unwrap(dir), filter);
        }

        @Override
        public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
            delegate.createDirectory(unwrap(dir), attrs);
        }

        @Override
        public void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
            delegate.createSymbolicLink(unwrap(link), unwrap(target), attrs);
        }

        @Override
        public void createLink(Path link, Path existing) throws IOException {
            delegate.createLink(unwrap(link), unwrap(existing));
        }

        @Override
        public void delete(Path path) throws IOException {
            delegate.delete(unwrap(path));
        }

        @Override
        public boolean deleteIfExists(Path path) throws IOException {
            return delegate.deleteIfExists(unwrap(path));
        }

        @Override
        public Path readSymbolicLink(Path link) throws IOException {
            return delegate.readSymbolicLink(unwrap(link));
        }

        @Override
        public void copy(Path source, Path target, CopyOption... options) throws IOException {
            delegate.copy(unwrap(source), unwrap(target), options);
        }

        @Override
        public void move(Path source, Path target, CopyOption... options) throws IOException {
            delegate.move(unwrap(source), unwrap(target), options);
        }

        @Override
        public boolean isSameFile(Path path, Path path2) throws IOException {
            return delegate.isSameFile(unwrap(path), unwrap(path2));
        }

        @Override
        public boolean isHidden(Path path) throws IOException {
            return delegate.isHidden(unwrap(path));
        }

        @Override
        public FileStore getFileStore(Path path) throws IOException {
            return delegate.getFileStore(unwrap(path));
        }

        @Override
        public void checkAccess(Path path, AccessMode... modes) throws IOException {
            delegate.checkAccess(unwrap(path), modes);
        }

        @Override
        public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
            return delegate.getFileAttributeView(unwrap(path), type, options);
        }

        @Override
        public <A extends BasicFileAttributes> A readAttributes(Path path,
                                                                Class<A> type,
                                                                LinkOption... options) throws IOException {
            return delegate.readAttributes(unwrap(path), type, options);
        }

        @Override
        public Map<String, Object> readAttributes(Path path,
                                                  String attributes,
                                                  LinkOption... options) throws IOException {
            return delegate.readAttributes(unwrap(path), attributes, options);
        }

        @Override
        public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
            delegate.setAttribute(unwrap(path), attribute, value, options);
        }

        @Override
        public boolean exists(Path path, LinkOption... options) {
            return delegate.exists(unwrap(path), options);
        }

        @Override
        public <A extends BasicFileAttributes> A readAttributesIfExists(Path path,
                                                                        Class<A> type,
                                                                        LinkOption... options) throws IOException {
            return delegate.readAttributesIfExists(unwrap(path), type, options);
        }
    }

    private static @NotNull Path unwrap(@NotNull Path path) {
        return path instanceof FilePath filePath ? filePath.path() : path;
    }
}
