package io.spbx.util.extern.api.youtube;

import com.google.common.primitives.Longs;
import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.base.str.Regex;
import io.spbx.util.func.IntMatcher;
import io.spbx.util.func.LongReversible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Base64;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@Immutable
public record YoutubeVideoId(@NotNull String id) {
    public YoutubeVideoId {
        assert isValidId(id) : "YoutubeVideoId is invalid: " + id;
    }

    public static @NotNull YoutubeVideoId of(@NotNull String id) {
        return new YoutubeVideoId(id);
    }

    public static @NotNull YoutubeVideoId of(long internalId) {
        return new YoutubeVideoId(encodeToString(internalId));
    }

    public static @NotNull YoutubeVideoId fromUrl(@NotNull String addr) {
        return YoutubeVideoId.of(getVideoIdFromUrlOrDie(addr));
    }

    public static @NotNull YoutubeVideoId detectFrom(@NotNull String input) {
        return YoutubeVideoId.of(getVideoIdFromAnythingOrDie(input));
    }

    public long internalId() {
        return decodeToLong(id);
    }

    public @NotNull String toUrl() {
        return toUrl(UrlType.MAIN_HTTPS);
    }

    public @NotNull String toUrl(@NotNull UrlType type) {
        return switch (type) {
            case MAIN_HTTPS -> "https://www.youtube.com/watch?v=" + id;
            case MAIN_DOMAIN -> "www.youtube.com/watch?v=" + id;
            case MOBILE_HTTPS -> "https://m.youtube.com/watch?v=" + id;
            case MOBILE_DOMAIN -> "m.youtube.com/watch?v=" + id;
            case SHORT_LINK_HTTPS -> "https://youtu.be/" + id;
            case SHORT_LINK_DOMAIN -> "youtu.be/" + id;
        };
    }

    @Override
    public String toString() {
        return id;
    }

    // Supports all kinds of inputs: URL or raw id.
    public static @Nullable String getVideoIdFromAnythingOrNull(@NotNull String input) {
        input = input.trim();
        if (isValidId(input)) {
            return input;
        }
        if (input.contains("youtu")) {
            return getVideoIdFromUrlOrNull(input);
        }
        return null;
    }

    public static @NotNull String getVideoIdFromAnythingOrDie(@NotNull String input) {
        return requireNonNull(getVideoIdFromAnythingOrNull(input), () -> "Failed to recognize Youtube id: " + input);
    }

    // Inspired by
    // https://stackoverflow.com/questions/3717115/regular-expression-for-youtube-links
    // https://stackoverflow.com/questions/3452546/how-do-i-get-the-youtube-video-id-from-a-url
    private static final Pattern YOUTUBE_VIDEO_URL = Pattern.compile(
        "(?:https?://)?" +
        "(?:www\\.|m\\.)?" +
        "youtu" +
        "(?:\\.be/|be.com/\\S*" +
        "(?:watch|embed|)" +
        "(?:(?=/[^&\\s?]+(?!\\S))/|(?:\\S*v=|v/)|/))" +
        "([A-Za-z0-9_\\-]{11})" +
        "(?:&|\\?|#|\\s|$)"
    );

    // Supports only the URL.
    public static @Nullable String getVideoIdFromUrlOrNull(@NotNull String addr) {
        return Regex.on(addr).findOrNull(YOUTUBE_VIDEO_URL, matcher -> matcher.group(1));
    }

    public static @NotNull String getVideoIdFromUrlOrDie(@NotNull String addr) {
        return requireNonNull(getVideoIdFromUrlOrNull(addr), () -> "Failed to parse Youtube url: " + addr);
    }

    public static final LongReversible<String> CONVERTER = new LongReversible<>() {
        @Override public long forwardToLong(@NotNull String s) {
            return decodeToLong(s);
        }
        @Override public @NotNull String backward(long i) {
            return encodeToString(i);
        }
    };

    private static long decodeToLong(@NotNull String id) {
        return Longs.fromByteArray(Base64.getUrlDecoder().decode(id));
    }

    private static @NotNull String encodeToString(long internalId) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(Longs.toByteArray(internalId));
    }

    private static boolean isValidId(@NotNull String id) {
        // Same as regex `[A-Za-z0-9_\\-]{11}`, micro-optimized (~ 2-3x faster).
        return id.length() == 11 && BasicStrings.matchChars(id, IntMatcher.BASE64_URL);
    }

    public enum UrlType {
        MAIN_HTTPS,
        MAIN_DOMAIN,
        MOBILE_HTTPS,
        MOBILE_DOMAIN,
        SHORT_LINK_HTTPS,
        SHORT_LINK_DOMAIN,
    }
}
