package io.spbx.util.text;

import io.spbx.util.base.Pair;
import io.spbx.util.base.Triple;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class BasicJoinTest {
    private static final Object NULL = null;

    @Test
    public void single_string() {
        assertThat(BasicJoin.singleOf("").join()).isEqualTo("");
        assertThat(BasicJoin.singleOf("").join("_")).isEqualTo("");
        assertThat(BasicJoin.singleOf("").onlyNonEmpty().join("_")).isEqualTo("");
        assertThat(BasicJoin.singleOf("a").join()).isEqualTo("a");
        assertThat(BasicJoin.singleOf("a").join("_")).isEqualTo("a");
        assertThat(BasicJoin.singleOf("a").onlyNonEmpty().join("_")).isEqualTo("a");
    }

    @Test
    public void single_object() {
        assertThat(BasicJoin.singleOf(1).join()).isEqualTo("1");
        assertThat(BasicJoin.singleOf(1).join("_")).isEqualTo("1");
        assertThat(BasicJoin.singleOf(1).onlyNonEmpty().join("_")).isEqualTo("1");
    }

    @Test
    public void single_null() {
        assertThat(BasicJoin.singleOf(NULL).join()).isEqualTo("");
        assertThat(BasicJoin.singleOf(NULL).join()).isEqualTo("");
        assertThat(BasicJoin.singleOf(NULL).onlyNonEmpty().join("")).isEqualTo("");
    }

    @Test
    public void only_nulls() {
        assertThat(BasicJoin.of(NULL, NULL).join()).isEqualTo("");
        assertThat(BasicJoin.of(NULL, NULL, NULL).join()).isEqualTo("");
        assertThat(BasicJoin.of(NULL, NULL, NULL, NULL).join()).isEqualTo("");

        assertThat(BasicJoin.of(NULL, NULL).join(".")).isEqualTo(".");
        assertThat(BasicJoin.of(NULL, NULL, NULL).join(".")).isEqualTo("..");
        assertThat(BasicJoin.of(NULL, NULL, NULL, NULL).join(".")).isEqualTo("...");
    }

    @Test
    public void only_empty_strings() {
        assertThat(BasicJoin.of("", "").join()).isEqualTo("");
        assertThat(BasicJoin.of("", "", "").join()).isEqualTo("");
        assertThat(BasicJoin.of("", "", "", "").join()).isEqualTo("");

        assertThat(BasicJoin.of("", "").join("_")).isEqualTo("_");
        assertThat(BasicJoin.of("", "", "").join("_")).isEqualTo("__");
        assertThat(BasicJoin.of("", "", "", "").join("_")).isEqualTo("___");
    }

    @Test
    public void nulls_mixed() {
        assertThat(BasicJoin.of("a", NULL).join()).isEqualTo("a");
        assertThat(BasicJoin.of("", "a", NULL).join()).isEqualTo("a");
        assertThat(BasicJoin.of(NULL, "a", "a", "").join()).isEqualTo("aa");

        assertThat(BasicJoin.of("a", NULL).join("_")).isEqualTo("a_");
        assertThat(BasicJoin.of("", "a", NULL).join("_")).isEqualTo("_a_");
        assertThat(BasicJoin.of(NULL, "a", "", "a").join("_")).isEqualTo("_a__a");

        assertThat(BasicJoin.of(1, 2).join()).isEqualTo("12");
        assertThat(BasicJoin.of(1, NULL, 2, 3).join()).isEqualTo("123");
        assertThat(BasicJoin.of("", NULL, 1, "").join()).isEqualTo("1");

        assertThat(BasicJoin.of(1, 2).join("_")).isEqualTo("1_2");
        assertThat(BasicJoin.of(1, NULL, 2, 3).join("_")).isEqualTo("1__2_3");
        assertThat(BasicJoin.of("", "", 1, NULL).join("_")).isEqualTo("__1_");
    }

    @Test
    public void empty_strings_mixed() {
        assertThat(BasicJoin.of("a", "").join()).isEqualTo("a");
        assertThat(BasicJoin.of("", "a", "").join()).isEqualTo("a");
        assertThat(BasicJoin.of("", "a", "a", "").join()).isEqualTo("aa");

        assertThat(BasicJoin.of("a", "").join("_")).isEqualTo("a_");
        assertThat(BasicJoin.of("", "a", "").join("_")).isEqualTo("_a_");
        assertThat(BasicJoin.of("", "a", "", "a").join("_")).isEqualTo("_a__a");

        assertThat(BasicJoin.of(1, 2).join()).isEqualTo("12");
        assertThat(BasicJoin.of(1, "", 2, 3).join()).isEqualTo("123");
        assertThat(BasicJoin.of("", "", 1, "").join()).isEqualTo("1");

        assertThat(BasicJoin.of(1, 2).join("_")).isEqualTo("1_2");
        assertThat(BasicJoin.of(1, "", 2, 3).join("_")).isEqualTo("1__2_3");
        assertThat(BasicJoin.of("", "", 1, "").join("_")).isEqualTo("__1_");
    }

    @Test
    public void array() {
        assertThat(BasicJoin.of(arrayOf()).join("_")).isEqualTo("");
        assertThat(BasicJoin.of(arrayOf(0)).join("_")).isEqualTo("0");
        assertThat(BasicJoin.of(arrayOf(0, 1, 2)).join("_")).isEqualTo("0_1_2");
        assertThat(BasicJoin.of(arrayOf(0, 1, "")).join("_")).isEqualTo("0_1_");
        assertThat(BasicJoin.of(arrayOf(0, NULL, "")).join("_")).isEqualTo("0__");
        assertThat(BasicJoin.of(arrayOf(0, NULL, "")).onlyNonEmpty().join("_")).isEqualTo("0");
    }

    @Test
    public void iterable() {
        assertThat(BasicJoin.of(iterableOf()).join("_")).isEqualTo("");
        assertThat(BasicJoin.of(iterableOf(0)).join("_")).isEqualTo("0");
        assertThat(BasicJoin.of(iterableOf(0, 1, 2)).join("_")).isEqualTo("0_1_2");
        assertThat(BasicJoin.of(iterableOf(0, 1, "")).join("_")).isEqualTo("0_1_");
        assertThat(BasicJoin.of(iterableOf(0, NULL, "")).join("_")).isEqualTo("0__");
        assertThat(BasicJoin.of(iterableOf(0, NULL, "")).onlyNonEmpty().join("_")).isEqualTo("0");
    }

    @Test
    public void pair() {
        assertThat(BasicJoin.of(Pair.of(1, 2)).join()).isEqualTo("12");
        assertThat(BasicJoin.of(Pair.of(NULL, 2)).join()).isEqualTo("2");
        assertThat(BasicJoin.of(Pair.of(1, NULL)).join()).isEqualTo("1");
        assertThat(BasicJoin.of(Pair.of(NULL, NULL)).join()).isEqualTo("");

        assertThat(BasicJoin.of(Pair.of(1, 2)).join(".")).isEqualTo("1.2");
        assertThat(BasicJoin.of(Pair.of(NULL, 2)).join(".")).isEqualTo(".2");
        assertThat(BasicJoin.of(Pair.of(1, NULL)).join(".")).isEqualTo("1.");
        assertThat(BasicJoin.of(Pair.of(NULL, NULL)).join(".")).isEqualTo(".");

        assertThat(BasicJoin.of(Pair.of(1, 2)).onlyNonEmpty().join(".")).isEqualTo("1.2");
        assertThat(BasicJoin.of(Pair.of(NULL, 2)).onlyNonEmpty().join(".")).isEqualTo("2");
        assertThat(BasicJoin.of(Pair.of(1, NULL)).onlyNonEmpty().join(".")).isEqualTo("1");
        assertThat(BasicJoin.of(Pair.of(NULL, NULL)).onlyNonEmpty().join(".")).isEqualTo("");
    }

    @Test
    public void triple() {
        assertThat(BasicJoin.of(Triple.of(1, 2, 3)).join()).isEqualTo("123");
        assertThat(BasicJoin.of(Triple.of(1, 2, NULL)).join()).isEqualTo("12");
        assertThat(BasicJoin.of(Triple.of(1, NULL, 3)).join()).isEqualTo("13");
        assertThat(BasicJoin.of(Triple.of(NULL, 2, 3)).join()).isEqualTo("23");
        assertThat(BasicJoin.of(Triple.of(NULL, NULL, NULL)).join()).isEqualTo("");

        assertThat(BasicJoin.of(Triple.of(1, 2, 3)).join(".")).isEqualTo("1.2.3");
        assertThat(BasicJoin.of(Triple.of(1, 2, NULL)).join(".")).isEqualTo("1.2.");
        assertThat(BasicJoin.of(Triple.of(1, NULL, 3)).join(".")).isEqualTo("1..3");
        assertThat(BasicJoin.of(Triple.of(NULL, 2, 3)).join(".")).isEqualTo(".2.3");
        assertThat(BasicJoin.of(Triple.of(NULL, NULL, NULL)).join(".")).isEqualTo("..");

        assertThat(BasicJoin.of(Triple.of(1, 2, 3)).onlyNonEmpty().join(".")).isEqualTo("1.2.3");
        assertThat(BasicJoin.of(Triple.of(1, 2, NULL)).onlyNonEmpty().join(".")).isEqualTo("1.2");
        assertThat(BasicJoin.of(Triple.of(1, NULL, 3)).onlyNonEmpty().join(".")).isEqualTo("1.3");
        assertThat(BasicJoin.of(Triple.of(NULL, 2, 3)).onlyNonEmpty().join(".")).isEqualTo("2.3");
        assertThat(BasicJoin.of(Triple.of(NULL, NULL, NULL)).onlyNonEmpty().join(".")).isEqualTo("");
    }

    @Test
    public void only_non_empty() {
        assertThat(BasicJoin.of("a", "").onlyNonEmpty().join("_")).isEqualTo("a");
        assertThat(BasicJoin.of("a", NULL).onlyNonEmpty().join("_")).isEqualTo("a");
        assertThat(BasicJoin.of("", "a", "").onlyNonEmpty().join("_")).isEqualTo("a");
        assertThat(BasicJoin.of("", "a", NULL).onlyNonEmpty().join("_")).isEqualTo("a");
        assertThat(BasicJoin.of("", "a", "", "a").onlyNonEmpty().join("_")).isEqualTo("a_a");
        assertThat(BasicJoin.of(NULL, "a", "", "a").onlyNonEmpty().join("_")).isEqualTo("a_a");

        assertThat(BasicJoin.of(1, 2).onlyNonEmpty().join("_")).isEqualTo("1_2");
        assertThat(BasicJoin.of(1, "", 2, 3).onlyNonEmpty().join("_")).isEqualTo("1_2_3");
        assertThat(BasicJoin.of(1, NULL, 2, 3).onlyNonEmpty().join("_")).isEqualTo("1_2_3");
        assertThat(BasicJoin.of("", "", 1, "").onlyNonEmpty().join("_")).isEqualTo("1");
        assertThat(BasicJoin.of("", NULL, 1, "").onlyNonEmpty().join("_")).isEqualTo("1");
        assertThat(BasicJoin.of(NULL, NULL, 1, NULL).onlyNonEmpty().join("_")).isEqualTo("1");
    }

    @Test
    public void custom_to_string() {
        assertThat(BasicJoin.of(listOf(0, 1, -1), Integer::toHexString).join("_")).isEqualTo("0_1_ffffffff");
        assertThat(BasicJoin.of(iterableOf(0, 1, -1), Integer::toHexString).join("_")).isEqualTo("0_1_ffffffff");
        assertThat(BasicJoin.of(listOf(0, 1, null), Integer::toHexString).join("_")).isEqualTo("0_1_");
        assertThat(BasicJoin.of(iterableOf(0, 1, null), Integer::toHexString).join("_")).isEqualTo("0_1_");
    }

    @Test
    public void join_with_prefix_suffix() {
        assertThat(BasicJoin.singleOf(1).join(",", "[", "]")).isEqualTo("[1]");
        assertThat(BasicJoin.singleOf(NULL).join(",", "[", "]")).isEqualTo("[]");
        assertThat(BasicJoin.of(1, 2).join(",", "[", "]")).isEqualTo("[1,2]");
        assertThat(BasicJoin.of(1, NULL, 2).join(",", "[", "]")).isEqualTo("[1,,2]");
    }
}
