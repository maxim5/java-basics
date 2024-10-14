package io.spbx.util.extern.api.youtube;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.extern.api.youtube.YoutubeVideoId.getVideoIdOrNull;
import static io.spbx.util.testing.AssertReverse.assertRoundtrip;

@Tag("fast")
public class YoutubeVideoIdTest {
    @Test
    public void of_id() {
        assertThat(YoutubeVideoId.of("8OQIW_K9MMc").id()).isEqualTo("8OQIW_K9MMc");
        assertThat(YoutubeVideoId.of("8OQIW_K9MMc").internalId()).isEqualTo(-1088736018909286201L);
    }

    @Test
    public void id_converter_roundtrip() {
        assertRoundtrip(YoutubeVideoId.CONVERTER, "8OQIW_K9MMc");
        assertRoundtrip(YoutubeVideoId.CONVERTER, "iwGFalTRHDA");
        assertRoundtrip(YoutubeVideoId.CONVERTER, "0zM3nApSvMg");
        // https://stackoverflow.com/questions/29941270/why-do-base64-decode-produce-same-byte-array-for-different-strings
        assertRoundtrip(YoutubeVideoId.CONVERTER, "----------8");
        assertRoundtrip(YoutubeVideoId.CONVERTER, "__________8");
    }

    @Test
    public void getVideoIdOrNull_videoId() {
        assertThat(getVideoIdOrNull("iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("  iwGFalTRHDA  \t")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("-----------")).isEqualTo("-----------");
        assertThat(getVideoIdOrNull("___________")).isEqualTo("___________");
    }

    @Test
    public void getVideoIdOrNull_watch_v_param() {
        assertThat(getVideoIdOrNull("http://www.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("http://www.youtube.com/watch?v=0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("https://www.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("https://www.youtube.com/watch?v=MoBL33GT9S8&feature=share")).isEqualTo("MoBL33GT9S8");
        assertThat(getVideoIdOrNull("http://www.youtube.com/watch?v=0zM3nApSvMg&feature=feedrec_grec_index")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("http://www.youtube.com/watch?v=0zM3nApSvMg#t=0m10s")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("www.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdOrNull_watch() {
        assertThat(getVideoIdOrNull("https://www.youtube.com/watch/iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("https://www.youtube.com/watch/iwGFalTRHDA?foo=bar")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("https://www.youtube.com/watch/iwGFalTRHDA#foo=bar")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdOrNull_v() {
        assertThat(getVideoIdOrNull("http://youtube.com/v/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("https://youtube.com/v/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("http://www.youtube.com/v/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("http://www.youtube.com/v/0zM3nApSvMg?fs=1&amp;hl=en_US&amp;rel=0")).isEqualTo("0zM3nApSvMg");
    }

    @Test
    public void getVideoIdOrNull_embed() {
        assertThat(getVideoIdOrNull("http://www.youtube.com/embed/0zM3nApSvMg?rel=0")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("https://www.youtube.com/embed/v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("https://www.youtube.com/embed/watch?feature=player_embedded&v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("https://www.youtube.com/embed/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdOrNull_youtu_be() {
        assertThat(getVideoIdOrNull("http://youtu.be/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdOrNull("youtu.be/iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdOrNull_m_watch() {
        assertThat(getVideoIdOrNull("https://m.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdOrNull("https://m.youtube.com/watch/iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdOrNull_misc() {
        assertThat(getVideoIdOrNull("http://www.youtube.com/attribution_link?u=/watch?v=aGmiw_rrNxk&feature=share")).isEqualTo("aGmiw_rrNxk");
        assertThat(getVideoIdOrNull("http://www.youtube.com/user/IngridMichaelsonVEVO#p/a/u/1/QdK8U-VIH_o")).isEqualTo("QdK8U-VIH_o");
    }

    @Test
    public void getVideoIdOrNull_null() {
        assertThat(getVideoIdOrNull("http://www.youtube.com")).isNull();
        assertThat(getVideoIdOrNull("http://www.youtube.com/")).isNull();
        assertThat(getVideoIdOrNull("https://www.youtube.com/")).isNull();

        assertThat(getVideoIdOrNull("http://www.youtube.com/user/IngridMichaelsonVEVO")).isNull();
        assertThat(getVideoIdOrNull("https://www.youtube.com/feed/subscriptions")).isNull();
        assertThat(getVideoIdOrNull("https://www.youtube.com/channel/UCgc00bfF_PvO_2AvqJZHXFg")).isNull();
        assertThat(getVideoIdOrNull("https://www.youtube.com/c/NatGeoEdOrg/videos")).isNull();
    }
}
