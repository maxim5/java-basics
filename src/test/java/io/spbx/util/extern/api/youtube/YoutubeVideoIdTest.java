package io.spbx.util.extern.api.youtube;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.extern.api.youtube.YoutubeVideoId.getVideoIdFromAnythingOrNull;
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
    public void getVideoIdFromAnythingOrNull_videoId() {
        assertThat(getVideoIdFromAnythingOrNull("iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("  iwGFalTRHDA  \t")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("-----------")).isEqualTo("-----------");
        assertThat(getVideoIdFromAnythingOrNull("___________")).isEqualTo("___________");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_watch_v_param() {
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/watch?v=0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/watch?v=MoBL33GT9S8&feature=share")).isEqualTo("MoBL33GT9S8");
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/watch?v=0zM3nApSvMg&feature=feedrec_grec_index")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/watch?v=0zM3nApSvMg#t=0m10s")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("www.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_watch() {
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/watch/iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/watch/iwGFalTRHDA?foo=bar")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/watch/iwGFalTRHDA#foo=bar")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_v() {
        assertThat(getVideoIdFromAnythingOrNull("http://youtube.com/v/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("https://youtube.com/v/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/v/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/v/0zM3nApSvMg?fs=1&amp;hl=en_US&amp;rel=0")).isEqualTo("0zM3nApSvMg");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_embed() {
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/embed/0zM3nApSvMg?rel=0")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/embed/v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/embed/watch?feature=player_embedded&v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/embed/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_youtu_be() {
        assertThat(getVideoIdFromAnythingOrNull("http://youtu.be/0zM3nApSvMg")).isEqualTo("0zM3nApSvMg");
        assertThat(getVideoIdFromAnythingOrNull("youtu.be/iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_m_watch() {
        assertThat(getVideoIdFromAnythingOrNull("https://m.youtube.com/watch?v=iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
        assertThat(getVideoIdFromAnythingOrNull("https://m.youtube.com/watch/iwGFalTRHDA")).isEqualTo("iwGFalTRHDA");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_misc() {
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/attribution_link?u=/watch?v=aGmiw_rrNxk&feature=share")).isEqualTo("aGmiw_rrNxk");
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/user/IngridMichaelsonVEVO#p/a/u/1/QdK8U-VIH_o")).isEqualTo("QdK8U-VIH_o");
    }

    @Test
    public void getVideoIdFromAnythingOrNull_null() {
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com")).isNull();
        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/")).isNull();
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/")).isNull();

        assertThat(getVideoIdFromAnythingOrNull("http://www.youtube.com/user/IngridMichaelsonVEVO")).isNull();
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/feed/subscriptions")).isNull();
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/channel/UCgc00bfF_PvO_2AvqJZHXFg")).isNull();
        assertThat(getVideoIdFromAnythingOrNull("https://www.youtube.com/c/NatGeoEdOrg/videos")).isNull();
    }
}
