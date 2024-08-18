package io.spbx.util.testing;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;
import java.nio.file.Path;

@SuppressWarnings("resource")
public class TestingFiles {
    public static @NotNull FileSystem newFileSystem(@NotNull Os os) {
        return switch (os) {
            case UNIX -> Jimfs.newFileSystem(Configuration.unix());
            case WINDOWS -> Jimfs.newFileSystem(Configuration.windows());
            case OSX -> Jimfs.newFileSystem(Configuration.osX());
        };
    }

    public static @NotNull Path newFileSystemRootPath(@NotNull Os os) {
        FileSystem fileSystem = newFileSystem(os);
        return switch (os) {
            case UNIX -> fileSystem.getPath("/home/foo/");
            case WINDOWS -> fileSystem.getPath("C:/foo/");
            case OSX -> fileSystem.getPath("/Users/foo/");
        };
    }

    public enum Os {
        UNIX,
        WINDOWS,
        OSX,
    }
}
