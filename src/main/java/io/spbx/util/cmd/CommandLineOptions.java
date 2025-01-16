package io.spbx.util.cmd;

import com.google.common.collect.ImmutableMap;
import io.spbx.util.collect.container.IntSize;
import io.spbx.util.props.StandardProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

@Immutable
public class CommandLineOptions implements IntSize, StandardProperties {
    static final CommandLineOptions EMPTY = new CommandLineOptions(ImmutableMap.of(), ImmutableMap.of());

    private final ImmutableMap<String, String> main;
    private final ImmutableMap<String, String> keys;

    CommandLineOptions(@NotNull Map<String, String> main, @NotNull Map<String, String> keys) {
        this.main = ImmutableMap.copyOf(main);
        this.keys = ImmutableMap.copyOf(keys);
    }

    public static @NotNull CommandLineOptions of(@NotNull Map<String, String> main, @NotNull Map<String, String> keys) {
        return main.isEmpty() && keys.isEmpty() ? EMPTY : new CommandLineOptions(main, keys);
    }

    @Override
    public int size() {
        return main.size();
    }

    @Override
    public @Nullable String getOrNull(@NotNull String name) {
        return main.get(name);
    }

    public @Nullable String getByKey(@NotNull String key) {
        return keys.get(key);
    }

    public @NotNull ImmutableMap<String, String> asMap() {
        return main;
    }

    public @NotNull ImmutableMap<String, String> allKeysMap() {
        return keys;
    }

    @Override
    public String toString() {
        return main.toString();
    }
}
