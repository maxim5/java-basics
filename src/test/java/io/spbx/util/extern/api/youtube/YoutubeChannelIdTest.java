package io.spbx.util.extern.api.youtube;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.extern.api.youtube.YoutubeChannelId.getChannelIdFromAnythingOrNull;
import static io.spbx.util.testing.AssertReverse.assertRoundtrip;

@Tag("fast")
public class YoutubeChannelIdTest {
    @Test
    public void of_id() {
        assertThat(YoutubeChannelId.of("UCc0YbtMkRdhcqwhu3Oad-lw").id()).isEqualTo("UCc0YbtMkRdhcqwhu3Oad-lw");
        assertThat(YoutubeChannelId.of("UCc0YbtMkRdhcqwhu3Oad-lw").internalId().highBits()).isEqualTo(8306357026012886551L);
        assertThat(YoutubeChannelId.of("UCc0YbtMkRdhcqwhu3Oad-lw").internalId().lowBits()).isEqualTo(3081055568835083927L);
    }

    @Test
    public void id_converter_roundtrip() {
        assertRoundtrip(YoutubeChannelId.CONVERTER, "UCc0YbtMkRdhcqwhu3Oad-lw");
        assertRoundtrip(YoutubeChannelId.CONVERTER, "UCgc00bfF_PvO_2AvqJZHXFg");
        assertRoundtrip(YoutubeChannelId.CONVERTER, "UC-UC6EOBtoI7H4ihy6g1ang");
    }

    @Test
    public void getChannelIdFromAnythingOrNull_channelId() {
        assertThat(getChannelIdFromAnythingOrNull("UCgc00bfF_PvO_2AvqJZHXFg")).isEqualTo("UCgc00bfF_PvO_2AvqJZHXFg");
        assertThat(getChannelIdFromAnythingOrNull("  UCgc00bfF_PvO_2AvqJZHXFg  \t")).isEqualTo("UCgc00bfF_PvO_2AvqJZHXFg");
        assertThat(getChannelIdFromAnythingOrNull("UC----------------------")).isEqualTo("UC----------------------");
        assertThat(getChannelIdFromAnythingOrNull("UC______________________")).isEqualTo("UC______________________");
    }

    @Test
    public void getChannelIdFromAnythingOrNull_channelUrl() {
        assertThat(getChannelIdFromAnythingOrNull("https://www.youtube.com/channel/UCgc00bfF_PvO_2AvqJZHXFg"))
            .isEqualTo("UCgc00bfF_PvO_2AvqJZHXFg");
        assertThat(getChannelIdFromAnythingOrNull("https://www.youtube.com/channel/UCgc00bfF_PvO_2AvqJZHXFg?foo=bar"))
            .isEqualTo("UCgc00bfF_PvO_2AvqJZHXFg");
    }
}
