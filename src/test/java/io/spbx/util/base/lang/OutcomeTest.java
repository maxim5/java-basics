package io.spbx.util.base.lang;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;

@Tag("fast")
public class OutcomeTest {
    @Test
    public void success_non_null() {
        Outcome<String> outcome = Outcome.success("foo");
        assertThat(outcome.isOK()).isTrue();
        assertThat(outcome.isError()).isFalse();
        assertThat(outcome.hasException()).isFalse();
        assertThat(outcome.value()).isEqualTo("foo");
        assertThat(outcome.successValue()).isEqualTo("foo");
        assertThat(outcome.valueOrDefault(null)).isEqualTo("foo");
        assertThat(outcome.valueOrDefault("bar")).isEqualTo("foo");
        assertThat(outcome.get()).hasValue("foo");
        assertThat(outcome.error()).isNull();
        assertThat(outcome.errorMessage()).isNull();
    }

    @Test
    public void failure_with_exception() {
        Outcome<String> outcome = Outcome.failure(new NullPointerException("Fail!"));
        assertThat(outcome.isOK()).isFalse();
        assertThat(outcome.isError()).isTrue();
        assertThat(outcome.hasException()).isTrue();
        assertThat(outcome.value()).isNull();
        assertFailure(outcome::successValue).throwsAssertion();
        assertThat(outcome.valueOrDefault(null)).isEqualTo(null);
        assertThat(outcome.valueOrDefault("bar")).isEqualTo("bar");
        assertThat(outcome.get()).isEmpty();
        assertThat(outcome.error()).isInstanceOf(NullPointerException.class);
        assertThat(outcome.errorMessage()).isEqualTo("Fail!");
    }

    @Test
    public void failure_with_only_error_message() {
        Outcome<String> outcome = Outcome.failure("Error");
        assertThat(outcome.isOK()).isFalse();
        assertThat(outcome.isError()).isTrue();
        assertThat(outcome.hasException()).isFalse();
        assertThat(outcome.value()).isNull();
        assertFailure(outcome::successValue).throwsAssertion();
        assertThat(outcome.valueOrDefault(null)).isEqualTo(null);
        assertThat(outcome.valueOrDefault("bar")).isEqualTo("bar");
        assertThat(outcome.get()).isEmpty();
        assertThat(outcome.error()).isNull();
        assertThat(outcome.errorMessage()).isEqualTo("Error");
    }
}
