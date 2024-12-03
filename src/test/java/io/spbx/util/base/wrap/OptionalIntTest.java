package io.spbx.util.base.wrap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class OptionalIntTest {
    @Test
    public void simple_present() {
        assertThat(OptionalInt.of(0).isPresent()).isTrue();
        assertThat(OptionalInt.of(0).isEmpty()).isFalse();

        assertThat(OptionalInt.of(1).isPresent()).isTrue();
        assertThat(OptionalInt.of(1).isEmpty()).isFalse();

        assertThat(OptionalInt.ofNonZero(1).isPresent()).isTrue();
        assertThat(OptionalInt.ofNonZero(1).isEmpty()).isFalse();

        assertThat(OptionalInt.ofNullable(1).isPresent()).isTrue();
        assertThat(OptionalInt.ofNullable(1).isEmpty()).isFalse();

        assertThat(OptionalInt.ofOptional(Optional.of(1)).isPresent()).isTrue();
        assertThat(OptionalInt.ofOptional(Optional.of(1)).isEmpty()).isFalse();
    }

    @Test
    public void simple_empty() {
        assertThat(OptionalInt.empty().isPresent()).isFalse();
        assertThat(OptionalInt.empty().isEmpty()).isTrue();

        assertThat(OptionalInt.ofNonZero(0).isPresent()).isFalse();
        assertThat(OptionalInt.ofNonZero(0).isEmpty()).isTrue();

        assertThat(OptionalInt.ofNullable(null).isPresent()).isFalse();
        assertThat(OptionalInt.ofNullable(null).isEmpty()).isTrue();

        assertThat(OptionalInt.ofOptional(Optional.empty()).isPresent()).isFalse();
        assertThat(OptionalInt.ofOptional(Optional.empty()).isEmpty()).isTrue();
    }
}
