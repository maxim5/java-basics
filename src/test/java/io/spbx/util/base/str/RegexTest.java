package io.spbx.util.base.str;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class RegexTest {
    @Test
    public void regex_ops_matches() {
        assertThat(Regex.on("123").matches(Pattern.compile("\\d+"))).isTrue();
        assertThat(Regex.on("123").matches(Regex.defer("\\d+"))).isTrue();
        assertThat(Regex.on("123").matches(Regex.cache().cached("\\d+"))).isTrue();
        assertThat(Regex.cache().on("123").matches("\\d+")).isTrue();

        assertThat(Regex.on("foo").matches(Pattern.compile("\\d+"))).isFalse();
        assertThat(Regex.on("foo").matches(Regex.defer("\\d+"))).isFalse();
        assertThat(Regex.on("foo").matches(Regex.cache().cached("\\d+"))).isFalse();
        assertThat(Regex.cache().on("foo").matches("\\d+")).isFalse();
    }

    @Test
    public void regex_ops_finds() {
        assertThat(Regex.on("foo-123").finds(Pattern.compile("\\d+"))).isTrue();
        assertThat(Regex.on("foo-123").finds(Regex.defer("\\d+"))).isTrue();
        assertThat(Regex.on("foo-123").finds(Regex.cache().cached("\\d+"))).isTrue();
        assertThat(Regex.cache().on("foo-123").finds("\\d+")).isTrue();

        assertThat(Regex.on("foo-123").finds(Pattern.compile("[4-9]+"))).isFalse();
        assertThat(Regex.on("foo-123").finds(Regex.defer("[4-9]+"))).isFalse();
        assertThat(Regex.on("foo-123").finds(Regex.cache().cached("[4-9]+"))).isFalse();
        assertThat(Regex.cache().on("foo-123").finds("[4-9]+")).isFalse();
    }

    @Test
    public void regex_ops_replaceAll() {
        assertThat(Regex.on("foo-123-456").replaceAll(Pattern.compile("\\d+"), "bar")).isEqualTo("foo-bar-bar");
        assertThat(Regex.on("foo-123-456").replaceAll(Regex.defer("\\d+"), "bar")).isEqualTo("foo-bar-bar");
        assertThat(Regex.on("foo-123-456").replaceAll(Regex.cache().cached("\\d+"), "bar")).isEqualTo("foo-bar-bar");
        assertThat(Regex.cache().on("foo-123-456").replaceAll("\\d+", "bar")).isEqualTo("foo-bar-bar");
    }

    @Test
    public void regex_ops_replaceFirst() {
        assertThat(Regex.on("foo-123-456").replaceFirst(Pattern.compile("\\d+"), "bar")).isEqualTo("foo-bar-456");
        assertThat(Regex.on("foo-123-456").replaceFirst(Regex.defer("\\d+"), "bar")).isEqualTo("foo-bar-456");
        assertThat(Regex.on("foo-123-456").replaceFirst(Regex.cache().cached("\\d+"), "bar")).isEqualTo("foo-bar-456");
        assertThat(Regex.cache().on("foo-123-456").replaceFirst("\\d+", "bar")).isEqualTo("foo-bar-456");
    }

    @Test
    public void regex_ops_split() {
        assertThat(Regex.on("foo-12-34").split(Pattern.compile("-"))).asList().containsExactly("foo", "12", "34");
        assertThat(Regex.on("foo-12-34").split(Regex.defer("-"))).asList().containsExactly("foo", "12", "34");
        assertThat(Regex.on("foo-12-34").split(Regex.cache().cached("-"))).asList().containsExactly("foo", "12", "34");
        assertThat(Regex.cache().on("foo-12-34").split("-")).asList().containsExactly("foo", "12", "34");

        assertThat(Regex.on("foo-12-34").split(Pattern.compile("-"), 2)).asList().containsExactly("foo", "12-34");
        assertThat(Regex.on("foo-12-34").split(Regex.defer("-"), 2)).asList().containsExactly("foo", "12-34");
        assertThat(Regex.on("foo-12-34").split(Regex.cache().cached("-"), 2)).asList().containsExactly("foo", "12-34");
        assertThat(Regex.cache().on("foo-12-34").split("-", 2)).asList().containsExactly("foo", "12-34");
    }

    @Test
    public void regex_ops_splitWithDelimiters() {
        assertThat(Regex.on("f-1-9").splitWithDelimiters(Pattern.compile("-"), 3)).asList().containsExactly("f", "-", "1", "-", "9");
        assertThat(Regex.on("f-1-9").splitWithDelimiters(Regex.defer("-"), 3)).asList().containsExactly("f", "-", "1", "-", "9");
        assertThat(Regex.on("f-1-9").splitWithDelimiters(Regex.cache().cached("-"), 3)).asList().containsExactly("f", "-", "1", "-", "9");
        assertThat(Regex.cache().on("f-1-9").splitWithDelimiters("-", 3)).asList().containsExactly("f", "-", "1", "-", "9");

        assertThat(Regex.on("f-1-9").splitWithDelimiters(Pattern.compile("-"), 2)).asList().containsExactly("f", "-", "1-9");
        assertThat(Regex.on("f-1-9").splitWithDelimiters(Regex.defer("-"), 2)).asList().containsExactly("f", "-", "1-9");
        assertThat(Regex.on("f-1-9").splitWithDelimiters(Regex.cache().cached("-"), 2)).asList().containsExactly("f", "-", "1-9");
        assertThat(Regex.cache().on("f-1-9").splitWithDelimiters("-", 2)).asList().containsExactly("f", "-", "1-9");
    }

    @Test
    public void regex_ops_splitAsStream() {
        assertThat(Regex.on("foo-12-34").splitAsStream(Pattern.compile("-"))).containsExactly("foo", "12", "34");
        assertThat(Regex.on("foo-12-34").splitAsStream(Regex.defer("-"))).containsExactly("foo", "12", "34");
        assertThat(Regex.on("foo-12-34").splitAsStream(Regex.cache().cached("-"))).containsExactly("foo", "12", "34");
        assertThat(Regex.cache().on("foo-12-34").splitAsStream("-")).containsExactly("foo", "12", "34");
    }

    @Test
    public void regex_ops_match() {
        assertThat(Regex.on("123").match(Pattern.compile("(12)3?"), m -> m.group(1))).hasValue("12");
        assertThat(Regex.on("123").match(Regex.defer("(12)3?"), m -> m.group(1))).hasValue("12");
        assertThat(Regex.on("123").match(Regex.cache().cached("(12)3?"), m -> m.group(1))).hasValue("12");
        assertThat(Regex.cache().on("123").match("(12)3?", m -> m.group(1))).hasValue("12");

        assertThat(Regex.on("124").match(Pattern.compile("(12)3?"), m -> m.group(1))).isEmpty();
        assertThat(Regex.on("124").match(Regex.defer("(12)3?"), m -> m.group(1))).isEmpty();
        assertThat(Regex.on("124").match(Regex.cache().cached("(12)3?"), m -> m.group(1))).isEmpty();
        assertThat(Regex.cache().on("124").match("(12)3?", m -> m.group(1))).isEmpty();
    }

    @Test
    public void regex_ops_matchOrNull() {
        assertThat(Regex.on("123").<String>matchOrNull(Pattern.compile("(12)3?"), m -> m.group(1))).isEqualTo("12");
        assertThat(Regex.on("123").<String>matchOrNull(Regex.defer("(12)3?"), m -> m.group(1))).isEqualTo("12");
        assertThat(Regex.on("123").<String>matchOrNull(Regex.cache().cached("(12)3?"), m -> m.group(1))).isEqualTo("12");
        assertThat(Regex.cache().on("123").<String>matchOrNull("(12)3?", m -> m.group(1))).isEqualTo("12");

        assertThat(Regex.on("124").<String>matchOrNull(Pattern.compile("(12)3?"), m -> m.group(1))).isNull();
        assertThat(Regex.on("124").<String>matchOrNull(Regex.defer("(12)3?"), m -> m.group(1))).isNull();
        assertThat(Regex.on("124").<String>matchOrNull(Regex.cache().cached("(12)3?"), m -> m.group(1))).isNull();
        assertThat(Regex.cache().on("124").<String>matchOrNull("(12)3?", m -> m.group(1))).isNull();
    }

    @Test
    public void regex_ops_find() {
        assertThat(Regex.on("foo-123").find(Pattern.compile("(12)3?"), m -> m.group(1))).hasValue("12");
        assertThat(Regex.on("foo-123").find(Regex.defer("(12)3?"), m -> m.group(1))).hasValue("12");
        assertThat(Regex.on("foo-123").find(Regex.cache().cached("(12)3?"), m -> m.group(1))).hasValue("12");
        assertThat(Regex.cache().on("foo-123").find("(12)3?", m -> m.group(1))).hasValue("12");

        assertThat(Regex.on("foo-134").find(Pattern.compile("(12)3?"), m -> m.group(1))).isEmpty();
        assertThat(Regex.on("foo-134").find(Regex.defer("(12)3?"), m -> m.group(1))).isEmpty();
        assertThat(Regex.on("foo-134").find(Regex.cache().cached("(12)3?"), m -> m.group(1))).isEmpty();
        assertThat(Regex.cache().on("foo-134").find("(12)3?", m -> m.group(1))).isEmpty();
    }

    @Test
    public void regex_ops_findOrNull() {
        assertThat(Regex.on("foo-123").<String>findOrNull(Pattern.compile("(12)3?"), m -> m.group(1))).isEqualTo("12");
        assertThat(Regex.on("foo-123").<String>findOrNull(Regex.defer("(12)3?"), m -> m.group(1))).isEqualTo("12");
        assertThat(Regex.on("foo-123").<String>findOrNull(Regex.cache().cached("(12)3?"), m -> m.group(1))).isEqualTo("12");
        assertThat(Regex.cache().on("foo-123").<String>findOrNull("(12)3?", m -> m.group(1))).isEqualTo("12");

        assertThat(Regex.on("foo-134").<String>findOrNull(Pattern.compile("(12)3?"), m -> m.group(1))).isNull();
        assertThat(Regex.on("foo-134").<String>findOrNull(Regex.defer("(12)3?"), m -> m.group(1))).isNull();
        assertThat(Regex.on("foo-134").<String>findOrNull(Regex.cache().cached("(12)3?"), m -> m.group(1))).isNull();
        assertThat(Regex.cache().on("foo-134").<String>findOrNull("(12)3?", m -> m.group(1))).isNull();
    }
}
