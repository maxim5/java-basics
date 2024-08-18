package io.spbx.util.collect;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ToJoinApiTest {
    @Test
    public void join_simple() {
        assertThat(ToJoinApi.of().join()).isEqualTo("");
        assertThat(ToJoinApi.of().join(':')).isEqualTo("");
        assertThat(ToJoinApi.of().join(":")).isEqualTo("");
        assertThat(ToJoinApi.of().join(":", "[", "]")).isEqualTo("[]");
        assertThat(ToJoinApi.of().join(':', '[', ']')).isEqualTo("[]");
        assertThat(ToJoinApi.of().joinLines()).isEqualTo("");

        assertThat(ToJoinApi.of(1, 2, 3).join()).isEqualTo("123");
        assertThat(ToJoinApi.of(1, 2, 3).join(':')).isEqualTo("1:2:3");
        assertThat(ToJoinApi.of(1, 2, 3).join(", ")).isEqualTo("1, 2, 3");
        assertThat(ToJoinApi.of(1, 2, 3).join(", ", "[", "]")).isEqualTo("[1, 2, 3]");
        assertThat(ToJoinApi.of(1, 2, 3).join(',', '[', ']')).isEqualTo("[1,2,3]");
        assertThat(ToJoinApi.of(1, 2, 3).joinLines()).isEqualTo("1\n2\n3");
    }
}
