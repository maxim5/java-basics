package io.spbx.util.base.cmd;

import com.google.common.collect.ImmutableList;
import io.spbx.util.collect.container.IntSize;
import io.spbx.util.collect.iter.BasicIterables;
import io.spbx.util.props.PropertyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

@Immutable
public class CommandLineArgs implements IntSize, PropertyList {
    static final CommandLineArgs EMPTY = new CommandLineArgs(ImmutableList.of());

    private final ImmutableList<String> list;

    CommandLineArgs(@NotNull ImmutableList<String> list) {
        this.list = list;
    }

    public static @NotNull CommandLineArgs of(@NotNull Iterable<String> args) {
        return BasicIterables.isEmpty(args) ? EMPTY : new CommandLineArgs(ImmutableList.copyOf(args));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public @Nullable String getOrNull(int key) {
        return list.get(key);
    }

    public @NotNull ImmutableList<String> asList() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
