package io.spbx.util.io;

import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public class URLs {
    public static @NotNull URL of(@NotNull String url) {
        return of(URI.create(url));
    }

    public static @NotNull URL of(@NotNull Path path) {
        return of(path.toFile());
    }

    public static @NotNull URL of(@NotNull File file) {
        return of(file.toURI());
    }

    public static @NotNull URL of(@NotNull URI uri) {
        return Unchecked.Suppliers.runRethrow(uri::toURL);
    }
}
