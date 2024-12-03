package io.spbx.util.base.cmd;

import com.google.common.collect.ImmutableMap;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.base.tuple.Pair;
import io.spbx.util.collect.list.ListBuilder;
import io.spbx.util.collect.stream.Streamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.spbx.util.base.error.BasicExceptions.newIllegalArgumentException;
import static io.spbx.util.collect.stream.BasicStreams.single;

@Stateless
@Pure
@CheckReturnValue
class CommandLineParser {
    static @NotNull Result parse(@NotNull String @NotNull[] input, @NotNull CommandLineSpec spec)
            throws InvalidCommandLineException {
        List<String> args = Streamer.of(input).skipIf(CommandLineParser::isOption).toGuavaImmutableList();
        List<String> options = Streamer.of(input).filter(CommandLineParser::isOption).toList();

        List<ParsedOption> parsedOptions = options.stream().map(ParsedOption::parseFrom).toList();
        Map<String, MappedOption> mappedOptions = Streamer.of(parsedOptions)
            .map(parsed -> new MappedOption(parsed, spec.getOrNull(parsed.key())))
            .toMapBy(option -> option.parsed().key());

        List<Throwable> errors = validateOptions(spec, mappedOptions);
        InvalidCommandLineException.assureNoErrors(errors);

        ImmutableMap<String, String> optionsMain = Streamer.of(mappedOptions.values())
            .split(MappedOption::toKeyValuePair)
            .toGuavaImmutableMap();
        ImmutableMap<String, String> optionsKeys = Streamer.of(mappedOptions.values())
            .split()
            .flatMapKeys(MappedOption::allKeys)
            .mapValues(MappedOption::value)
            .toGuavaImmutableMap();

        return new Result(CommandLineArgs.of(args), CommandLineOptions.of(optionsMain, optionsKeys));
    }

    static @NotNull List<Throwable> validateOptions(@NotNull CommandLineSpec spec, @NotNull Map<String, MappedOption> options) {
        ListBuilder<Throwable> builder = ListBuilder.builder();

        Streamer.of(options.values())
            .filter(MappedOption::hasSpec)
            .skipIf(MappedOption::validate)
            .map(option -> newIllegalArgumentException("Option `%s` value is invalid: `%s`",
                                                       option.parsed().key(), option.parsed().value()))
            .forEach(builder::add);

        if (!spec.isAllowArbitraryOptions()) {
            Streamer.of(options.values())
                .filter(MappedOption::hasNoSpec)
                .map(option -> newIllegalArgumentException("Unrecognized option:", option.parsed().key()))
                .forEach(builder::add);
        }

        Streamer.of(spec.mandatoryArgSpecs())
            .skipIf(arg -> arg.isMatch(options::containsKey))
            .map(arg -> newIllegalArgumentException("Mandatory option is missing:", arg.toHumanDescription()))
            .forEach(builder::add);

        Streamer.of(options.values())
            .filter(MappedOption::hasNoSpec)
            .filter(option -> spec.isExposedKey(option.exposedKey()))
            .map(option -> newIllegalArgumentException("Option misspelt: `%s`", option.key()))
            .forEach(builder::add);

        return builder.toGuavaImmutableList();
    }

    public record Result(@NotNull CommandLineArgs args, @NotNull CommandLineOptions options) {}

    static boolean isOption(@NotNull String arg) {
        return arg.startsWith("-");
    }

    static @NotNull String toExposedKey(@NotNull String key) {
        return BasicStrings.stripLeft(key, ch -> ch == '-');
    }

    record ParsedOption(@NotNull String raw, @NotNull String key, @Nullable String value) {
        public static @NotNull ParsedOption parseFrom(@NotNull String raw) {
            assert isOption(raw) : "Must be an option: " + raw;
            Pair<String, String> pair = raw.indexOf('=') >= 0 ? Pair.from(raw.split("=", 2)) : Pair.of(raw, null);
            return new ParsedOption(raw, pair.getKey(), pair.getValue());
        }
    }

    record MappedOption(@NotNull ParsedOption parsed, @Nullable CommandLineSpec.ArgSpec spec) {
        public boolean hasSpec() {
            return spec != null;
        }

        public boolean hasNoSpec() {
            return spec == null;
        }

        public boolean validate() {
            return spec != null && spec.validator().test(parsed.value());
        }

        public @NotNull Pair<String, String> toKeyValuePair() {
            return Pair.of(exposedKey(), value());
        }

        public @NotNull String exposedKey() {
            return toExposedKey(key());
        }

        public @NotNull Stream<String> allKeys() {
            return spec != null ? spec.allKeys() : single(parsed.key());
        }

        private @NotNull String key() {
            return spec != null ? spec.key() : parsed.key();
        }

        private @NotNull String value() {
            return parsed.value != null ? parsed.value : "";
        }
    }
}
