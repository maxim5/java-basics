package io.spbx.util.base.str;

import io.spbx.util.collect.stream.BasicStreams;
import io.spbx.util.collect.stream.ToListApi;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.RegEx;
import javax.annotation.concurrent.Immutable;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.spbx.util.base.error.BasicExceptions.newInternalError;
import static io.spbx.util.base.error.BasicExceptions.runOnlyInDev;

@Immutable
public class BasicSplit {
    private final CharSequence input;
    private final int limit /* = -1 */;
    private final int exact /* = -1 */;
    private final boolean skipRestAfterLimit /* = true */;
    private final boolean skipEmpty /* = false */;

    private BasicSplit(@NotNull CharSequence input, int limit, int exact, boolean skipRestAfterLimit, boolean skipEmpty) {
        this.input = input;
        this.limit = limit;
        this.exact = exact;
        this.skipRestAfterLimit = skipRestAfterLimit;
        this.skipEmpty = skipEmpty;
    }

    public static @NotNull BasicSplit of(@Nullable CharSequence input) {
        return new BasicSplit(BasicStrings.nonNull(input), -1, -1, true, false);
    }
    
    public @NotNull BasicSplit exactly(int exact) {
        assert exact > 0 : "Invalid exact value: " + exact;
        return new BasicSplit(input, -1, exact, skipRestAfterLimit, skipEmpty);
    }

    public @NotNull BasicSplit limit(int limit) {
        assert limit >= 0 : "Invalid limit value: " + limit;
        return new BasicSplit(input, limit, -1, skipRestAfterLimit, skipEmpty);
    }

    public @NotNull BasicSplit skipRestAfterLimit() {
        assert limit >= 0 : "Limit must be set first";
        return new BasicSplit(input, limit, exact, true, skipEmpty);
    }

    public @NotNull BasicSplit includeRestAfterLimit() {
        assert limit >= 0 : "Limit must be set first";
        return new BasicSplit(input, limit, exact, false, skipEmpty);
    }
    
    public @NotNull BasicSplit skipEmpty() {
        assert exact < 0 : "Skipping empty values is incompatible with exact split";
        return new BasicSplit(input, limit, exact, skipRestAfterLimit, true);
    }

    public @NotNull BasicSplit includeEmpty() {
        return new BasicSplit(input, limit, exact, skipRestAfterLimit, false);
    }
    
    public @NotNull ToListApi<String> on(char separator) {
        return this.on(String.valueOf(separator));
    }

    public @NotNull ToListApi<String> on(@NotNull String separator) {
        Pattern pattern = Pattern.compile(separator, Pattern.LITERAL);
        Stream<String> stream = splitToStream(pattern, s -> s.endsWith(separator));
        return ToListApi.of(stream);
    }

    public @NotNull ToListApi<String> onRegex(@NotNull @RegEx @Language("RegExp") String regex) {
        Pattern pattern = Pattern.compile(regex);
        Stream<String> stream = splitToStream(pattern, s -> false);
        return ToListApi.of(stream);
    }

    public @NotNull ToListApi<String> onRegex(@NotNull Pattern pattern) {
        Stream<String> stream = splitToStream(pattern, s -> false);
        return ToListApi.of(stream);
    }

    private @NotNull Stream<String> splitToStream(@NotNull Pattern pattern, @NotNull Predicate<String> failureDetector) {
        if (exact >= 0) {
            assert exact > 0 : newInternalError("`exact=%s` is negative", exact);
            assert limit < 0 : newInternalError("Both `exact=%s` and `limit=%s` are set: `%s`", exact, limit, input);
            assert !skipEmpty : newInternalError("Both `exact=%s` and `skipEmpty` are set: `%s`", exact, input);
            assert skipRestAfterLimit : newInternalError("Both `exact=%s` and `includeRestAfterLimit` are set: `%s`", exact, input);

            String[] split = pattern.split(input, -1);
            if (split.length == exact) {
                return BasicStreams.streamOf(split);
            }

            assert split.length == exact + 1 : throwInvalidInput(input, exact);
            assert split[exact].isEmpty() : throwInvalidInput(input, exact);
            assert !split[exact - 1].isEmpty() : throwInvalidInput(input, exact);
            assert runOnlyInDev(() -> {
                String[] splitExact = pattern.split(input, exact);
                assert splitExact.length == exact : throwInvalidInput(input, exact);
                assert !failureDetector.test(splitExact[exact - 1]) : throwInvalidInput(input, exact);
            });

            return BasicStreams.streamOf(split, 0, exact);
        }

        int limitForCall;
        if (skipRestAfterLimit) {
            limitForCall = -1;
        } else {
            assert limit >= 0 : newInternalError("If `includeRestAfterLimit` is set, `limit` must as well: `%s`", input);
            limitForCall = limit + 1;
        }

        String[] split = pattern.split(input, limitForCall);
        Stream<String> stream = BasicStreams.streamOf(split);

        if (skipEmpty) {
            stream = stream.filter(BasicStrings::isNotEmpty);
        }
        if (limit >= 0) {
            if (skipRestAfterLimit) {
                stream = stream.limit(limit);
            } else {
                stream = stream.limit(limit + 1);
            }
        }
        return stream;
    }

    private static @NotNull String throwInvalidInput(@NotNull CharSequence input, int exact) {
        return "Input can't be split into exactly %s parts: `%s`".formatted(exact, input);
    }
}
