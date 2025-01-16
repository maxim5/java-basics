package io.spbx.util.url;

import com.google.common.net.InternetDomainName;
import com.google.common.net.PercentEscaper;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Stateless
public class URLs {
    private static final PercentEscaper URL_PARAM_ESCAPER = new PercentEscaper("-_.,*", true);

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

    public static @NotNull String urlEncode(@NotNull String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static @NotNull String urlDecode(@NotNull String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public static @NotNull String urlParamEncode(@NotNull String value) {
        // https://stackoverflow.com/questions/4737841/urlencoder-not-able-to-translate-space-character
        return URL_PARAM_ESCAPER.escape(value);
    }

    public static @NotNull String urlParamDecode(@NotNull String value) {
        return urlDecode(value);
    }

    public static @NotNull InternetDomainName domainName(@NotNull String url) {
        return URLs.domainName(URLs.of(url));
    }

    public static @NotNull InternetDomainName domainName(@NotNull URL url) {
        return InternetDomainName.from(url.getHost());
    }

    public static @NotNull InternetDomainName domainName(@NotNull URI uri) {
        return InternetDomainName.from(uri.getHost());
    }

    public static boolean isSameTopLevelDomain(@NotNull URL lhs, @NotNull URL rhs) {
        return domainName(lhs).topPrivateDomain().equals(domainName(rhs).topPrivateDomain());
    }
}
