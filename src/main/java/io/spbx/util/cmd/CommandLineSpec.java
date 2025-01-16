package io.spbx.util.cmd;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.spbx.util.collect.list.ListBuilder;
import io.spbx.util.collect.stream.BasicStreams;
import io.spbx.util.collect.stream.Streamer;
import io.spbx.util.func.Predicates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.spbx.util.collect.iter.BasicIterables.duplicatesOf;
import static io.spbx.util.collect.iter.BasicIterables.isAllDistinct;

@Immutable
public class CommandLineSpec {
    static final String HELP = "help";
    static final ArgSpec HELP_SPEC = ArgSpec.of("--help", List.of("-h")).validator(Predicates.isNull()).help("Prints help").optional();
    static final CommandLineSpec EMPTY = of(List.of());
    static final CommandLineSpec EMPTY_ALLOW_ALL = of(List.of()).allowArbitraryOptions(true);

    private final ImmutableList<ArgSpec> originalSpecs;
    private final ImmutableMap<String, ArgSpec> specs;
    private final ImmutableSet<String> exposedKeys;
    private final boolean allowArbitraryOptions;

    CommandLineSpec(@NotNull ImmutableList<ArgSpec> originalSpecs,
                    @NotNull ImmutableMap<String, ArgSpec> specs,
                    @NotNull ImmutableSet<String> exposedKeys,
                    boolean allowArbitraryOptions) {
        this.originalSpecs = originalSpecs;
        this.specs = specs;
        this.exposedKeys = exposedKeys;
        this.allowArbitraryOptions = allowArbitraryOptions;
    }

    public static @NotNull CommandLineSpec of() {
        return EMPTY;
    }

    public static @NotNull CommandLineSpec of(@NotNull ArgSpec @NotNull... specs) {
        return CommandLineSpec.buildFrom(ListBuilder.of(specs).add(HELP_SPEC).toGuavaImmutableList());
    }

    public static @NotNull CommandLineSpec of(@NotNull Iterable<ArgSpec> specs) {
        return CommandLineSpec.buildFrom(ListBuilder.copyOf(specs).add(HELP_SPEC).toGuavaImmutableList());
    }

    public static @NotNull CommandLineSpec withoutHelp(@NotNull ArgSpec @NotNull... specs) {
        return CommandLineSpec.buildFrom(ImmutableList.copyOf(specs));
    }

    public static @NotNull CommandLineSpec withoutHelp(@NotNull Iterable<ArgSpec> specs) {
        return CommandLineSpec.buildFrom(ImmutableList.copyOf(specs));
    }

    private static @NotNull CommandLineSpec buildFrom(@NotNull ImmutableList<ArgSpec> specs) {
        List<String> exposedKeys = Streamer.of(specs)
            .flatMap(spec -> BasicStreams.prependToStream(spec.key, spec.aliases))
            .map(CommandLineParser::toExposedKey)
            .toList();
        assert isAllDistinct(exposedKeys) : "Duplicate command line keys found: " + duplicatesOf(exposedKeys);

        ImmutableMap<String, ArgSpec> map = Streamer.of(specs)
            .split()
            .flatMapKeys(ArgSpec::allKeys)
            .toGuavaImmutableMap();
        ImmutableSet<String> set = ImmutableSet.copyOf(exposedKeys);
        return new CommandLineSpec(specs, map, set, false);
    }

    public @NotNull CommandLineSpec allowArbitraryOptions(boolean allowArbitraryOptions) {
        return this.allowArbitraryOptions == allowArbitraryOptions ? this :
            new CommandLineSpec(originalSpecs, specs, exposedKeys, allowArbitraryOptions);
    }

    public @NotNull CommandLineSpec allowArbitraryOptions() {
        return this.allowArbitraryOptions(true);
    }

    public boolean isAllowArbitraryOptions() {
        return allowArbitraryOptions;
    }

    public @Nullable ArgSpec getOrNull(@NotNull String key) {
        return specs.get(key);
    }

    @NotNull ImmutableList<ArgSpec> allArgSpecs() {
        return originalSpecs;
    }

    @NotNull ImmutableList<ArgSpec> mandatoryArgSpecs() {
        return originalSpecs.stream().filter(ArgSpec::isMandatory).collect(ImmutableList.toImmutableList());
    }

    boolean isExposedKey(@NotNull String exposedKey) {
        return exposedKeys.contains(exposedKey);
    }

    boolean isHelpEnabled() {
        return isExposedKey(HELP);
    }

    @Immutable
    public record ArgSpec(@NotNull String key,
                          @NotNull ImmutableList<String> aliases,
                          @NotNull Predicate<@Nullable String> validator,
                          boolean isMandatory,
                          @NotNull String help) {
        public ArgSpec {
            assert CommandLineParser.isOption(key) : "Key must be an option, e.g. `--foo` or `-bar`";
            assert aliases.stream().allMatch(CommandLineParser::isOption) : "Alias keys must be options, e.g. `--foo` or `-bar`";
        }

        public static @NotNull ArgSpec of(@NotNull String key) {
            return ArgSpec.of(key, ImmutableList.of());
        }

        public static @NotNull ArgSpec of(@NotNull String key, @NotNull String alias) {
            return ArgSpec.of(key, ImmutableList.of(alias));
        }

        public static @NotNull ArgSpec of(@NotNull String key, @NotNull Iterable<String> aliases) {
            return new ArgSpec(key, ImmutableList.copyOf(aliases), Predicates.alwaysTrue(), true, "");
        }

        public boolean isMatch(@NotNull Predicate<String> predicate) {
            return predicate.test(key) || aliases().stream().anyMatch(predicate);
        }

        public @NotNull ArgSpec aliases(@NotNull Iterable<String> aliases) {
            return new ArgSpec(key, ImmutableList.copyOf(aliases), validator, isMandatory, help);
        }

        public @NotNull ArgSpec validator(@NotNull Predicate<@Nullable String> validator) {
            return new ArgSpec(key, aliases, validator, isMandatory, help);
        }

        public @NotNull ArgSpec mandatory(boolean mandatory) {
            return new ArgSpec(key, aliases, validator, mandatory, help);
        }

        public @NotNull ArgSpec mandatory() {
            return this.mandatory(true);
        }

        public @NotNull ArgSpec optional() {
            return this.mandatory(false);
        }

        public @NotNull ArgSpec help(@NotNull String help) {
            return new ArgSpec(key, aliases, validator, isMandatory, help);
        }

        @NotNull Stream<String> allKeys() {
            return BasicStreams.prependToStream(key, aliases);
        }

        public @NotNull String toHumanDescription() {
            if (aliases.isEmpty()) {
                return "`%s`".formatted(key);
            }
            return "`%s` %s".formatted(key, aliases.toString());
        }
    }
}
