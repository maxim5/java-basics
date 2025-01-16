package io.spbx.util.extern.api.youtube;

import io.spbx.util.base.math.Int128;
import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.base.str.Regex;
import io.spbx.util.func.IntMatcher;
import io.spbx.util.func.Reversible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Base64;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@Immutable
public record YoutubeChannelId(@NotNull String id) {
    public YoutubeChannelId {
        assert isValidId(id) : "YoutubeChannelId is invalid: " + id;
    }

    public static @NotNull YoutubeChannelId of(@NotNull String id) {
        return new YoutubeChannelId(id);
    }

    public static @NotNull YoutubeVideoId fromUrl(@NotNull String addr) {
        String id = getChannelIdFromUrlOrDie(addr);
        return YoutubeVideoId.of(id);
    }

    public static @NotNull YoutubeVideoId detectFrom(@NotNull String input) {
        return YoutubeVideoId.of(getVideoIdOrDie(input));
    }

    public @NotNull Int128 internalId() {
        return decodeToInt128(id);
    }

    public @NotNull String toUrl() {
        return "https://www.youtube.com/channel/%s".formatted(id);
    }

    @Override
    public String toString() {
        return id;
    }

    // Supports all kinds of inputs: URL or raw id.
    public static @Nullable String getChannelIdFromAnythingOrNull(@NotNull String input) {
        input = input.trim();
        if (isValidId(input)) {
            return input;
        }
        if (input.contains("youtu")) {
            return getChannelIdFromUrlOrNull(input);
        }
        return null;
    }

    public static @NotNull String getVideoIdOrDie(@NotNull String input) {
        return requireNonNull(getChannelIdFromAnythingOrNull(input), () -> "Failed to recognize Youtube channel id: " + input);
    }

    private static final Pattern YOUTUBE_VIDEO_URL = Pattern.compile(
        "(?:https?://)?" +
        "(?:www\\.|m\\.)?" +
        "youtu" +
        "(?:\\.be/|be.com)/\\S*" +
        "channel/" +
        "([A-Za-z0-9_\\-]{24})"
    );

    // Supports only the URL.
    public static @Nullable String getChannelIdFromUrlOrNull(@NotNull String url) {
        return Regex.on(url).findOrNull(YOUTUBE_VIDEO_URL, matcher -> matcher.group(1));
    }

    public static @NotNull String getChannelIdFromUrlOrDie(@NotNull String addr) {
        return requireNonNull(getChannelIdFromUrlOrNull(addr), () -> "Failed to parse Youtube url: " + addr);
    }

    private static @NotNull Int128 decodeToInt128(@NotNull String id) {
        return Int128.fromBits(Base64.getUrlDecoder().decode(id.substring(2)));
    }

    private static @NotNull String encodeToString(@NotNull Int128 internalId) {
        return "UC" + Base64.getUrlEncoder().withoutPadding().encodeToString(internalId.toByteArray());
    }

    public static final Reversible<String, Int128> CONVERTER = new Reversible<>() {
        @Override public @NotNull Int128 forward(@NotNull String s) {
            return decodeToInt128(s);
        }
        @Override public @NotNull String backward(@NotNull Int128 int128) {
            return encodeToString(int128);
        }
    };

    private static boolean isValidId(@NotNull String id) {
        return id.length() == 24 && id.startsWith("UC") && BasicStrings.matchChars(id, IntMatcher.BASE64_URL);
    }
}
