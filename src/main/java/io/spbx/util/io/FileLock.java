package io.spbx.util.io;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static io.spbx.util.base.error.BasicExceptions.newInternalError;

// https://www.baeldung.com/java-lock-files
// https://stackoverflow.com/questions/128038/how-can-i-lock-a-file-using-java-if-possible
@ThreadSafe
public class FileLock implements UncheckedClosable {
    private static final boolean LOCK_SHARED = true;
    private static final boolean LOCK_EXCLUSIVE = false;

    private final FileChannel channel;
    private final boolean shared;
    private volatile java.nio.channels.FileLock lock = null;

    FileLock(@NotNull FileChannel channel, boolean shared) {
        this.channel = channel;
        this.shared = shared;
    }

    public static @NotNull FileLock read(@NotNull Path path) throws UncheckedIOException {
        return new FileLock(open(path, StandardOpenOption.READ), LOCK_SHARED);
    }

    public static @NotNull FileLock read(@NotNull File file) throws UncheckedIOException {
        return FileLock.read(file.toPath());
    }

    // Valid as long as the input is not closed
    public static @NotNull FileLock read(@NotNull FileInputStream input) {
        return new FileLock(input.getChannel(), LOCK_SHARED);
    }

    public static @NotNull FileLock write(@NotNull Path path) throws UncheckedIOException {
        return new FileLock(open(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND), LOCK_EXCLUSIVE);
    }

    public static @NotNull FileLock write(@NotNull File file) throws UncheckedIOException {
        return FileLock.write(file.toPath());
    }

    // Valid as long as the input is not closed
    public static @NotNull FileLock write(@NotNull FileOutputStream output) {
        return new FileLock(output.getChannel(), LOCK_EXCLUSIVE);
    }

    public void lock() throws UncheckedIOException {
        try {
            java.nio.channels.FileLock fileLock = channel.lock(0L, Long.MAX_VALUE, shared);
            assert fileLock != null : newInternalError("channel.lock() returned a null lock");
            lock = fileLock;
        } catch (IOException e) {
            Unchecked.rethrow(e);
        }
    }

    @CanIgnoreReturnValue
    public boolean tryLock() throws UncheckedIOException {
        try {
            java.nio.channels.FileLock fileLock = channel.tryLock(0L, Long.MAX_VALUE, shared);
            if (fileLock != null) {
                lock = fileLock;
                return true;
            }
            return false;
        } catch (OverlappingFileLockException e) {
            return false;   // Same process holds the lock
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }

    public void unlock() throws UncheckedIOException {
        try {
            java.nio.channels.FileLock fileLock = lock;
            if (fileLock != null) {
                fileLock.release();
            }
        } catch (IOException e) {
            Unchecked.rethrow(e);
        }
    }

    public boolean isLockedByAnyProcess() {
        return isLockedByThisProcess() || isLockedByOtherProcess();
    }

    public boolean isLockedByThisProcess() {
        java.nio.channels.FileLock fileLock = lock;
        return fileLock != null && fileLock.isValid();
    }

    private boolean isLockedByOtherProcess() {
        try (java.nio.channels.FileLock fileLock = channel.tryLock(0L, Long.MAX_VALUE, shared)) {
            return fileLock == null || !fileLock.isValid();
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }

    @Override
    public void close() throws UncheckedIOException {
        unlock();
    }

    private static @NotNull FileChannel open(@NotNull Path path, @NotNull StandardOpenOption... options) {
        try {
            return FileChannel.open(path, options);
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }
}
