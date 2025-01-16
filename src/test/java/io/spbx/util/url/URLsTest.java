package io.spbx.util.url;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class URLsTest {
    @Test
    public void urlParamEncode_simple() {
        assertThat(URLs.urlParamEncode("")).isEqualTo("");
        assertThat(URLs.urlParamEncode("foobar")).isEqualTo("foobar");
        assertThat(URLs.urlParamEncode("foo,bar.baz")).isEqualTo("foo,bar.baz");
        assertThat(URLs.urlParamEncode("foo bar")).isEqualTo("foo+bar");
        assertThat(URLs.urlParamEncode("foo/bar")).isEqualTo("foo%2Fbar");
    }

    @Test
    public void urlParamDecode_simple() {
        assertThat(URLs.urlParamDecode("")).isEqualTo("");
        assertThat(URLs.urlParamDecode("foo")).isEqualTo("foo");
        assertThat(URLs.urlParamDecode(" ")).isEqualTo(" ");
        assertThat(URLs.urlParamDecode("%2F.+.%20.")).isEqualTo("/. . .");
    }

    @Test
    public void isSameTopLevelDomain_simple() {
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://www.foo.com"), URLs.of("https://www.foo.com"))).isTrue();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://www.foo.com"), URLs.of("ftp://www.foo.com"))).isTrue();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://www.foo.com"), URLs.of("http://foo.com"))).isTrue();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://www.foo.com"), URLs.of("http://bar.foo.com"))).isTrue();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://a.b.foo.com"), URLs.of("http://c.d.foo.com"))).isTrue();

        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.com/bar"), URLs.of("http://foo.com/qux"))).isTrue();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.com/bar?k=v"), URLs.of("http://foo.com/?v=k"))).isTrue();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.com/bar#baz"), URLs.of("http://foo.com"))).isTrue();

        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.com"), URLs.of("http://foobar.com"))).isFalse();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.com"), URLs.of("http://barfoo.com"))).isFalse();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.com"), URLs.of("http://foo.ru"))).isFalse();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.co.uk"), URLs.of("http://foo.co"))).isFalse();
        assertThat(URLs.isSameTopLevelDomain(URLs.of("http://foo.co.uk"), URLs.of("http://foo.uk"))).isFalse();
    }
}
