package io.spbx.util.code.gen;

import com.google.common.collect.ImmutableList;
import io.spbx.util.collect.BasicIterables;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.base.EasyCast.castAny;

public abstract class LinesBuilder<B extends LinesBuilder<?>> implements Iterable<String> {
    protected final ArrayList<String> lines;
    private final AtomicBoolean isSealed = new AtomicBoolean(false);

    protected LinesBuilder() {
        lines = new ArrayList<>();
    }

    protected LinesBuilder(int size) {
        lines = new ArrayList<>(size);
    }

    protected LinesBuilder(@NotNull List<String> list) {
        lines = BasicIterables.asArrayList(list);
    }

    public int linesNumber() {
        return lines.size();
    }

    public @NotNull ImmutableList<String> lines() {
        return ImmutableList.copyOf(lines);
    }

    @Override
    public @NotNull Iterator<String> iterator() {
        return lines.iterator();
    }

    public @NotNull B appendLine(@NotNull String line) {
        assert !isSealed() : "This instance is already sealed. Line: " + line;
        assert !containsNewLineChars(line) : "Not a single line: " + line;
        this.lines.add(line);
        return castAny(this);
    }

    public @NotNull B appendLine(@NotNull String linePart1, @NotNull String linePart2) {
        return appendLine(linePart1 + linePart2);
    }

    public @NotNull B appendLine(@NotNull String linePart1, @NotNull String linePart2, @NotNull String linePart3) {
        return appendLine(linePart1 + linePart2 + linePart3);
    }

    public @NotNull B appendLine(@NotNull String @NotNull ... lineParts) {
        return appendLine(String.join("", lineParts));
    }

    public @NotNull B appendFormattedLine(@NotNull String line, @Nullable Object @NotNull ... args) {
        return appendLine(line.formatted(args));
    }

    public @NotNull B appendLines(@NotNull Iterable<String> lines) {
        lines.forEach(this::appendLine);
        return castAny(this);
    }

    public @NotNull B appendLines(@NotNull Stream<String> lines) {
        lines.forEach(this::appendLine);
        return castAny(this);
    }

    public @NotNull B appendLines(@NotNull String @NotNull[] lines) {
        return appendLines(Stream.of(lines));
    }

    public @NotNull B appendMultiline(@NotNull String multiline) {
        return appendLines(multiline.lines());
    }

    public @NotNull B appendFormattedMultiline(@NotNull String multiline, @Nullable Object @NotNull ... args) {
        return appendMultiline(multiline.formatted(args));
    }

    public @NotNull B appendMultilines(@NotNull Iterable<String> multilines) {
        multilines.forEach(this::appendMultiline);
        return castAny(this);
    }

    public @NotNull B appendMultilines(@NotNull Stream<String> multilines) {
        multilines.forEach(this::appendMultiline);
        return castAny(this);
    }

    public @NotNull B appendMultilines(@NotNull String @NotNull[] multilines) {
        return appendMultilines(Stream.of(multilines));
    }

    public @NotNull B transform(@NotNull Function<String, String> convert) {
        assert !isSealed() : "This instance is already sealed";
        List<String> converted = lines.stream().map(convert).toList();
        lines.clear();
        lines.addAll(converted);
        return castAny(this);
    }

    @Pure
    public @NotNull String joinLines() {
        return join("\n");
    }

    @Pure
    public @NotNull String join(@NotNull CharSequence delimiter) {
        return String.join(delimiter, lines);
    }

    @Pure
    public @NotNull String join(@NotNull Collector<CharSequence, ?, String> collector) {
        return lines.stream().collect(collector);
    }

    @Override
    public String toString() {
        return joinLines();
    }

    private boolean isSealed() {
        return isSealed.get();
    }

    public void seal() {
        boolean success = isSealed.compareAndSet(false, true);
        assert success : "Already sealed";
    }

    protected static boolean containsNewLineChars(@NotNull String text) {
        return text.contains("\n") || text.contains("\r");
    }
}
