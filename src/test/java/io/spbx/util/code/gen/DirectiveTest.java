package io.spbx.util.code.gen;

import io.spbx.util.base.Pair;
import io.spbx.util.code.gen.Directive.Modifier;
import io.spbx.util.code.gen.Directive.Predefined;
import io.spbx.util.code.gen.Directive.Type;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.AssertTuples.assertOneOf;
import static io.spbx.util.code.gen.DirectiveBuilder.block;
import static io.spbx.util.code.gen.DirectiveBuilder.inline;

public class DirectiveTest {
    @Test
    public void parse_directive() {
        assertThat(Directive.parseDirective("if foo=bar", Type.BLOCK))
            .isEqualTo(block().predef(Predefined.IF).attrs("foo=bar").build());
        assertThat(Directive.parseDirective("placeholder foo", Type.BLOCK))
            .isEqualTo(block().predef(Predefined.PLACEHOLDER).attrs("foo").build());
        assertThat(Directive.parseDirective("assume foo=bar", Type.INLINE))
            .isEqualTo(inline().predef(Predefined.ASSUME).attrs("foo=bar").build());
    }

    @Test
    public void parse_key_predefined() {
        assertOneOf(Directive.parseKey("if")).holdsFirst(Predefined.IF);
        assertOneOf(Directive.parseKey("else")).holdsFirst(Predefined.ELSE);
        assertOneOf(Directive.parseKey("placeholder")).holdsFirst(Predefined.PLACEHOLDER);
        assertOneOf(Directive.parseKey("remove")).holdsFirst(Predefined.REMOVE);
        assertOneOf(Directive.parseKey("EOT")).holdsFirst(Predefined.EOD_OF_TEMPLATE);
        assertOneOf(Directive.parseKey("import")).holdsFirst(Predefined.IMPORT);
        assertOneOf(Directive.parseKey("assume")).holdsFirst(Predefined.ASSUME);
    }

    @Test
    public void parse_key_predefined_case_sensitive() {
        assertOneOf(Directive.parseKey("If")).holdsSecond(Pair.of("If", Modifier.NONE));
        assertOneOf(Directive.parseKey("IF")).holdsSecond(Pair.of("IF", Modifier.NONE));
        assertOneOf(Directive.parseKey("Remove")).holdsSecond(Pair.of("Remove", Modifier.NONE));
        assertOneOf(Directive.parseKey("eot")).holdsSecond(Pair.of("eot", Modifier.NONE));
    }

    @Test
    public void parse_key_modifier() {
        assertOneOf(Directive.parseKey("foo")).holdsSecond(Pair.of("foo", Modifier.NONE));
        assertOneOf(Directive.parseKey("foo-start")).holdsSecond(Pair.of("foo", Modifier.START));
        assertOneOf(Directive.parseKey("foo-end")).holdsSecond(Pair.of("foo", Modifier.END));

        assertOneOf(Directive.parseKey("start")).holdsSecond(Pair.of("", Modifier.START));
        assertOneOf(Directive.parseKey("end")).holdsSecond(Pair.of("", Modifier.END));
    }

    @Test
    public void parse_key_predefined_modified() {
        assertOneOf(Directive.parseKey("if-start")).holdsSecond(Pair.of("if", Modifier.START));
        assertOneOf(Directive.parseKey("if-end")).holdsSecond(Pair.of("if", Modifier.END));

        assertOneOf(Directive.parseKey("else-start")).holdsSecond(Pair.of("else", Modifier.START));
        assertOneOf(Directive.parseKey("else-end")).holdsSecond(Pair.of("else", Modifier.END));

        assertOneOf(Directive.parseKey("remove-start")).holdsSecond(Pair.of("remove", Modifier.START));
        assertOneOf(Directive.parseKey("remove-end")).holdsSecond(Pair.of("remove", Modifier.END));

        assertOneOf(Directive.parseKey("placeholder-start")).holdsSecond(Pair.of("placeholder", Modifier.START));
        assertOneOf(Directive.parseKey("placeholder-end")).holdsSecond(Pair.of("placeholder", Modifier.END));

        assertOneOf(Directive.parseKey("import-start")).holdsSecond(Pair.of("import", Modifier.START));
        assertOneOf(Directive.parseKey("import-end")).holdsSecond(Pair.of("import", Modifier.END));

        assertOneOf(Directive.parseKey("EOT-start")).holdsSecond(Pair.of("EOT", Modifier.START));
        assertOneOf(Directive.parseKey("EOT-end")).holdsSecond(Pair.of("EOT", Modifier.END));

        assertOneOf(Directive.parseKey("assume-start")).holdsSecond(Pair.of("assume", Modifier.START));
        assertOneOf(Directive.parseKey("assume-end")).holdsSecond(Pair.of("assume", Modifier.END));
    }
}
