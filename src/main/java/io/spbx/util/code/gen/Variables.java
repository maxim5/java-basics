package io.spbx.util.code.gen;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.BasicExceptions;
import io.spbx.util.base.BasicStrings;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.collect.Streamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Immutable
public class Variables {
    public static final Variables EMPTY = Variables.of(ImmutableMap.of());

    private final @NotNull ImmutableMap<String, String> map;

    private Variables(@NotNull ImmutableMap<String, String> map) {
        assert BasicExceptions.runOnlyInDev(() -> {
            map.forEach((key, val) -> {
                assert key.startsWith("$") : "Invalid variable name. Must start with a `$`: " + key;
            });
        });
        this.map = map;
    }

    public static @NotNull Variables of(@NotNull Map<String, String> map) {
        return new Variables(ImmutableMap.copyOf(map));
    }

    public static @NotNull Variables of(@NotNull String key, @Nullable Object val) {
        return new Variables(ImmutableMap.of(key, BasicStrings.toStringOrEmpty(val)));
    }

    public static @NotNull Variables of(@NotNull String key1, @Nullable Object val1,
                                        @NotNull String key2, @Nullable Object val2) {
        return new Variables(ImmutableMap.of(key1, BasicStrings.toStringOrEmpty(val1),
                                             key2, BasicStrings.toStringOrEmpty(val2)));
    }

    public static @NotNull Variables fixUpKeys(@NotNull Map<String, ?> map) {
        ImmutableMap<String, String> fixed = Streamer.of(map)
            .mapKeys(key -> BasicStrings.ensurePrefix(key, "$"))
            .mapKeys(key -> BasicStrings.ensureSuffix(key, "$"))
            .mapValues(val -> BasicStrings.toStringOrEmpty(val))
            .toGuavaImmutableMap();
        return new Variables(fixed);
    }

    public @Nullable String get(@NotNull CharSequence key) {
        return map.get(key.toString());
    }

    public @NotNull String getOrDefault(@NotNull CharSequence key, @NotNull CharSequence def) {
        return map.getOrDefault(key.toString(), def.toString());
    }

    public @NotNull ImmutableMap<String, String> toMap() {
        return map;
    }

    @NotNull Variables mergeAndOverwriteBy(@NotNull Variables vars) {
        return Variables.of(BasicMaps.mergeToMap(map, vars.map));
    }

    @NotNull String interpolate(@NotNull String input) {
        if (hasNoVars(input)) {
            return input;
        }
        String result = input;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    /*
    private static boolean isValidKey(@NotNull CharSequence key) {
        return key instanceof CharArray charArray ? charArray.startsWith('$') : key.toString().startsWith("$");
    }
    */

    private static boolean hasNoVars(@NotNull String input) {
        return input.indexOf('$') < 0;
    }
}
