package io.spbx.util.text;

import com.google.common.truth.MapSubject;
import io.spbx.util.array.CharArray;
import io.spbx.util.array.MutableCharArray;
import io.spbx.util.base.BasicParsing;
import io.spbx.util.collect.Streamer;
import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.ext.FluentLoggingCapture;
import io.spbx.util.text.TextExtractor.Extracted;
import io.spbx.util.text.TextExtractor.ExtractedMap;
import io.spbx.util.text.TextExtractor.Fallback;
import io.spbx.util.text.TextExtractor.MatchCapture;
import io.spbx.util.text.TextExtractor.MoveIntoRegion;
import io.spbx.util.text.TextExtractor.MoveTo;
import io.spbx.util.text.TextExtractor.RegionMoveTo;
import io.spbx.util.text.TextExtractor.Sanitizer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.ext.FluentLoggingCapture.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("slow")
public class TextExtractorTest {
    @RegisterExtension static final FluentLoggingCapture LOGGING = new FluentLoggingCapture(TextExtractor.class);

    /** {@link TextExtractor#extract} */
    
    @Test
    public void extract_simple_spaces() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("<section>"),
            TextExtractor.captureBetween("<name>", "</name>"),
            TextExtractor.captureBetween("<title>", "</title>")
        );

        ExtractedMap map = extractor.extract("""
           <body> <div><div>  <section>
               <name> Foo</name>  <age> 30 </age>  <title>Mr</title>
           </section> </body>
        """);

        assertExtractedMap(map).toCaptured().containsExactly("<name>", "Foo", "<title>", "Mr");

        assertThat(map.getCapturedTextOrNull("<name>")).isEqualTo(CharArray.of("Foo"));
        assertThat(map.getCapturedTextOrDie("<name>")).isEqualTo(CharArray.of("Foo"));
        assertThat(map.getConvertedValueOrNull("<name>")).isNull();

        assertThat(map.getCapturedTextOrNull("<title>")).isEqualTo(CharArray.of("Mr"));
        assertThat(map.getCapturedTextOrDie("<title>")).isEqualTo(CharArray.of("Mr"));
        assertThat(map.getConvertedValueOrNull("<title>")).isNull();
    }

    @Test
    public void extract_capture_sequence() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("[", "]"),
            TextExtractor.captureBetween("(", ")"),
            TextExtractor.captureBetween("{", "}")
        );

        assertExtractedMap(extractor.extract("[](){}")).toCaptured().containsExactly("[", "", "(", "", "{", "");
        assertExtractedMap(extractor.extract("[1] (2) {3}")).toCaptured().containsExactly("[", "1", "(", "2", "{", "3");
        assertExtractedMap(extractor.extract("(1) [2] {3}")).toCaptured().containsExactly("[", "2", "{", "3");
        assertExtractedMap(extractor.extract("(1) {2} [3]")).toCaptured().containsExactly("[", "3");

        assertExtractedMap(extractor.extract("[")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("]")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("][")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("[[[")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("]]]")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("]]]]][[[[[")).toCaptured().isEmpty();

        assertExtractedMap(extractor.extract("() {} ()")).toCaptured().containsExactly("(", "", "{", "");
        assertExtractedMap(extractor.extract("() {} () ][")).toCaptured().containsExactly("(", "", "{", "");
        assertExtractedMap(extractor.extract("][ () {} () [")).toCaptured().containsExactly("(", "", "{", "");
        assertExtractedMap(extractor.extract("] ()()()(){}{}{} [")).toCaptured().containsExactly("(", "", "{", "");

        assertExtractedMap(extractor.extract("[1][2][3]")).toCaptured().containsExactly("[", "1");
        assertExtractedMap(extractor.extract("[1][[2]][3][[[4][5]]][6][7]")).toCaptured().containsExactly("[", "1");
        assertExtractedMap(extractor.extract("(1)(2)(3)")).toCaptured().containsExactly("(", "1");
        assertExtractedMap(extractor.extract("{1}{2}{3}")).toCaptured().containsExactly("{", "1");
        assertExtractedMap(extractor.extract("[[[[1]]]]")).toCaptured().containsExactly("[", "[[[1");

        assertExtractedMap(extractor.extract("[(1){2}]")).toCaptured().containsExactly("[", "(1){2}");
        assertExtractedMap(extractor.extract("(1){[2}]] (3}{4) {[5)}"))
            .toCaptured().containsExactly("[", "2}", "(", "3}{4", "{", "[5)");
        assertExtractedMap(extractor.extract("(1){2} [3] ((4)) ](}[) {[5]} (6)[(7)]"))
            .toCaptured().containsExactly("[", "3", "(", "(4", "{", "[5]");
    }

    @Test
    public void extract_similar_and_duplicate_substrings() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("aaa", "aaa"),
            TextExtractor.captureBetween("aa", "aa")
        );

        assertExtractedMap(extractor.extract("aa")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("aaa")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("aaaa")).toCaptured().containsExactly("aa", "");
        assertExtractedMap(extractor.extract("aaaaa")).toCaptured().containsExactly("aa", "");
        assertExtractedMap(extractor.extract("aaaaaa")).toCaptured().containsExactly("aaa", "");
        assertExtractedMap(extractor.extract("aaaaaaa")).toCaptured().containsExactly("aaa", "");
        assertExtractedMap(extractor.extract("aaaaaaaa")).toCaptured().containsExactly("aaa", "");
        assertExtractedMap(extractor.extract("aaaaaaaaa")).toCaptured().containsExactly("aaa", "");
        assertExtractedMap(extractor.extract("aaaaaaaaaa")).toCaptured().containsExactly("aaa", "", "aa", "");
        assertExtractedMap(extractor.extract("aaaaaaaaaaa")).toCaptured().containsExactly("aaa", "", "aa", "");
        assertExtractedMap(extractor.extract("aaaaaaaaaaaaa")).toCaptured().containsExactly("aaa", "", "aa", "");
    }

    /** {@link TextExtractor#skipTo(CharSequence)} */

    @Test
    public void skip_to_simple() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("The"),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "Body</body>");
    }

    @Test
    public void skip_to_move_include_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("The").moveVia(MoveTo.INCLUDE_MATCH),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "TheBody</body>");
    }

    @Test
    public void skip_to_not_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("The")
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body> A Body</body>"));
    }

    @Test
    public void skip_to_not_found_or_else_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("The").orElseIgnore(),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> A Body</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body> A Body</body>");
    }

    /** {@link TextExtractor#skipTo(Pattern)} */

    @Test
    public void skip_to_pattern() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo(Pattern.compile("<\\w+>")),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "TheBody</body>");
    }

    @Test
    public void skip_to_pattern_move_include_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo(Pattern.compile("T.e")).moveVia(MoveTo.INCLUDE_MATCH),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "TheBody</body>");
    }

    @Test
    public void skip_to_pattern_not_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo(Pattern.compile("\\d+"))
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body> A Body</body>"));
    }

    @Test
    public void skip_to_pattern_not_found_or_else_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo(Pattern.compile("\\d+")).orElseIgnore(),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> A Body</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body> A Body</body>");
    }

    /** {@link TextExtractor#skipBackwardTo} */

    @Test
    public void skip_backward_to_simple() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipBackwardTo("The"),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body>");
    }

    @Test
    public void skip_backward_to_move_include_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipBackwardTo("The").moveVia(MoveTo.INCLUDE_MATCH),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body> The");
    }

    @Test
    public void skip_backward_to_not_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipBackwardTo("The")
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body> A Body</body>"));
    }

    @Test
    public void skip_backward_to_not_found_or_else_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipBackwardTo("The").orElseIgnore(),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body> A Body</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body> A Body</body>");
    }

    /** {@link TextExtractor#narrowDownTo(CharSequence, CharSequence)} */

    @Test
    public void narrow_down_to_simple() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>"),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body>TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "TheBody");
    }

    @Test
    public void narrow_down_to_move_include_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>").moveVia(MoveIntoRegion.MOVE_INSIDE_INCLUDE_MATCH),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body>TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body>TheBody</body>");
    }

    @Test
    public void narrow_down_to_start_not_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>")
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body id='id'>TheBody</body>"));
    }

    @Test
    public void narrow_down_to_start_not_found_or_else_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>").orElseIgnore(),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body id='id'>TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body id='id'>TheBody</body>");
    }

    @Test
    public void narrow_down_to_only_start_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>")
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body>TheBody</body"));
    }

    @Test
    public void narrow_down_to_only_start_found_or_else_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>").orElseIgnore(),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body>TheBody</body");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body>TheBody</body");
    }

    @Test
    public void narrow_down_to_multiple() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo("<body>", "</body>"),
            captureCurrent("[a]"),
            TextExtractor.narrowDownTo("<div>", "</div>"),
            captureCurrent("[b]"),
            TextExtractor.narrowDownTo("<section>", "</section>"),
            captureCurrent("[c]")
        );

        ExtractedMap map = extractor.extract("""
           <body> <div><div>  <section> Section </section> </div></body>
        """);

        assertExtractedMap(map).toCaptured().containsExactly(
            "[a]", "<div><div>  <section> Section </section> </div>",
            "[b]", "<div>  <section> Section </section>",
            "[c]", "Section"
        );
    }

    /** {@link TextExtractor#narrowDownTo(Pattern)} */

    @Test
    public void narrow_down_to_pattern() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo(Pattern.compile(">(.*)<")),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body>TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "TheBody");
    }

    @Test
    public void narrow_down_to_pattern_move_include_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo(Pattern.compile(">(.+)<")).moveVia(MoveIntoRegion.MOVE_INSIDE_INCLUDE_MATCH),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body>TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", ">TheBody<");
    }

    @Test
    public void narrow_down_to_pattern_not_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo(Pattern.compile(">(.+)<"))
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body>TheBody"));
    }

    @Test
    public void narrow_down_to_pattern_no_such_group() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo(Pattern.compile(">.+<"))
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("<body>TheBody</body>"));
    }

    @Test
    public void narrow_down_to_pattern_not_found_or_else_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.narrowDownTo(Pattern.compile(">(.+)<")).orElseIgnore(),
            captureCurrent("[a]")
        );
        ExtractedMap map = extractor.extract("<body id='id'>TheBody");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body id='id'>TheBody");
    }

    /** {@link TextExtractor#captureBetween} */

    @Test
    public void capture_between() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>"),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("  <body>TheBody</body> ");
        assertExtractedMap(map).toCaptured().containsExactly("<body>", "TheBody", "[1]", "");
    }

    @Test
    public void capture_between_move_after_start_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>").moveVia(RegionMoveTo.AFTER_START_MATCH),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("  <body>TheBody</body> ");
        assertExtractedMap(map).toCaptured().containsExactly("<body>", "TheBody", "[1]", "TheBody</body>");
    }

    @Test
    public void capture_between_start_not_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>")
        );
        LOGGING.withCustomLog4jLevel(WARN, () -> {
            ExtractedMap map = extractor.extract("  <body id='id'>TheBody</body>");
            assertExtractedMap(map).toCaptured().isEmpty();
            assertThat(LOGGING.logRecordsContaining("start mark not found `<body>`")).isNotEmpty();
        });
    }

    @Test
    public void capture_between_start_not_found_or_else_throw() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>").orElseThrow()
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("  <body id='id'>TheBody</body>"));
    }

    @Test
    public void capture_between_only_start_found_default() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>")
        );
        LOGGING.withCustomLog4jLevel(WARN, () -> {
            ExtractedMap map = extractor.extract("  <body>TheBody</body");
            assertExtractedMap(map).toCaptured().isEmpty();
            assertThat(LOGGING.logRecordsContaining("end mark not found `</body>`")).isNotEmpty();
        });
    }

    @Test
    public void capture_between_only_start_found_or_else_throw() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>").orElseThrow()
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract("  <body>TheBody</body"));
    }

    @Test
    public void capture_between_next_position() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<span>", "</span>"),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("<body>X<span>Span</span>Y</body>");
        assertExtractedMap(map).toCaptured().containsExactly("<span>", "Span", "[1]", "Y</body>");
    }

    @Test
    public void capture_between_next_position_at_the_end() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<span>", "</span>").moveVia(RegionMoveTo.AT_END_MATCH),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("<body>X<span>Span</span>Y</body>");
        assertExtractedMap(map).toCaptured().containsExactly("<span>", "Span", "[1]", "</span>Y</body>");
    }

    /** {@link TextExtractor#capturePattern} */

    @Test
    public void capture_pattern() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.capturePattern(Pattern.compile(">(.+)<")).named("regex"),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("  <body>TheBody</body> ");
        assertExtractedMap(map).toCaptured().containsExactly("regex:0", ">TheBody<", "regex:1", "TheBody", "[1]", "/body>");
    }

    @Test
    public void capture_pattern_move_after_start_match() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.capturePattern(Pattern.compile(">(.+)<")).named("regex").moveVia(MoveTo.INCLUDE_MATCH),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("  <body>TheBody</body> ");
        assertExtractedMap(map).toCaptured().containsExactly("regex:0", ">TheBody<", "regex:1", "TheBody", "[1]", ">TheBody</body>");
    }

    @Test
    public void capture_pattern_with_converter() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.capturePattern(Pattern.compile("\\w+-(\\d+)")).named("regex").convertVia(1, BasicParsing::parseIntSafe),
            captureCurrent("[1]")
        );
        ExtractedMap map = extractor.extract("  foo-123 bar  ");
        assertExtractedMap(map).toMap()
            .containsExactly("regex:0", Extracted.of("foo-123"), "regex:1", Extracted.of("123", 123), "[1]", Extracted.of("bar"));
        assertExtractedMap(map).toConverted().containsExactly("regex:0", null, "regex:1", 123, "[1]", null);
    }

    /** {@link TextExtractor.Capture#convertVia} */

    @Test
    public void capture_between_with_converter() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<span>", "</span>").convertVia(BasicParsing::parseIntSafe)
        );
        ExtractedMap map = extractor.extract("<body> 111 <span> 222 </span> 333 </body>");
        assertExtractedMap(map).toMap().containsExactly("<span>", Extracted.of("222", 222));
    }

    @Test
    public void with_converter_double_call() {
        assertThrows(AssertionError.class, () -> TextExtractor.captureBetween("", "").convertVia(s -> 0).convertVia(s -> 1));
    }

    /** {@link TextExtractor.Capture#onCapture} */

    @Test
    public void capture_between_on_capture() {
        try (MockConsumer.Tracker ignored = MockConsumer.trackAllConsumersDone()) {
            TextExtractor extractor = TextExtractor.of(
                TextExtractor.captureBetween("<span>", "</span>").onCapture(MockConsumer.expecting(CharArray.of("bar")))
            );
            ExtractedMap map = extractor.extract("<body> foo <span> bar </span> baz </body>");
            assertExtractedMap(map).toCaptured().containsExactly("<span>", "bar");
        }
    }

    @Test
    public void on_capture_double_call() {
        assertThrows(AssertionError.class, () -> TextExtractor.captureBetween("", "").onCapture(x -> {}).onCapture(x -> {}));
    }

    /** {@link TextExtractor#repeat} */

    @Test
    public void repeat_one_action() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.repeat(
                TextExtractor.captureBetween("[", "]").named("A")
            )
        );

        assertExtractedMap(extractor.extract("][")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("[i]")).toCaptured().containsExactly("A:0", "i");
        assertExtractedMap(extractor.extract("[i][j]")).toCaptured().containsExactly("A:0", "i", "A:1", "j");
        assertExtractedMap(extractor.extract("[i](j)[k]")).toCaptured().containsExactly("A:0", "i", "A:1", "k");
    }

    @Test
    public void repeat_two_actions() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.repeat(
                TextExtractor.captureBetween("[", "]").named("A"),
                TextExtractor.captureBetween("(", ")").named("B")
            )
        );

        assertExtractedMap(extractor.extract("[i](j)")).toCaptured().containsExactly("A:0", "i", "B:0", "j");
        assertExtractedMap(extractor.extract("[i](j)[k]")).toCaptured().containsExactly("A:0", "i", "B:0", "j", "A:1", "k");
        assertExtractedMap(extractor.extract("[i][j]")).toCaptured().containsExactly("A:0", "i", "A:1", "j");
        assertExtractedMap(extractor.extract("(i)(j)")).toCaptured().containsExactly("B:0", "i", "B:1", "j");
        assertExtractedMap(extractor.extract("[i](j)(k)[l][m](n)")).toCaptured()
            .containsExactly("A:0", "i", "B:0", "j", "A:1", "l", "B:1", "n");
    }

    @Test
    public void repeat_capture_regex() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.repeat(
                TextExtractor.capturePattern(Pattern.compile("(\\w+)-(\\d+)")).named("A")
            )
        );

        assertExtractedMap(extractor.extract("")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("foo-00")).toCaptured()
            .containsExactly("A:0:0", "foo-00", "A:0:1", "foo", "A:0:2", "00");
        assertExtractedMap(extractor.extract("foo-1 bar-2")).toCaptured()
            .containsExactly("A:0:0", "foo-1", "A:0:1", "foo", "A:0:2", "1",
                             "A:1:0", "bar-2", "A:1:1", "bar", "A:1:2", "2");
    }

    @Test
    public void repeat_max_iterations() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.repeat(
                TextExtractor.captureBetween("[", "]").named("A")
            ).maxIterations(1)
        );

        assertExtractedMap(extractor.extract("][")).toCaptured().isEmpty();
        assertExtractedMap(extractor.extract("[i]")).toCaptured().containsExactly("A:0", "i");
        assertExtractedMap(extractor.extract("[i][j]")).toCaptured().containsExactly("A:0", "i");
        assertExtractedMap(extractor.extract("[i](j)[k]")).toCaptured().containsExactly("A:0", "i");
    }

    /** {@link TextExtractor.Action#named(String)} */

    @Test
    public void named_simple() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.captureBetween("<body>", "</body>").named("capture")
        );
        ExtractedMap map = extractor.extract("  <body>TheBody</body>");
        assertExtractedMap(map).toCaptured().containsExactly("capture", "TheBody");
    }

    @Test
    public void named_double_call() {
        assertThrows(AssertionError.class, () -> TextExtractor.captureBetween("", "").named("foo").named("bar"));
    }

    /** {@link TextExtractor.Action#orElse(Fallback)} */

    @Test
    public void fallback_ignore() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("a").orElseIgnore()
        );
        LOGGING.withCustomLog4jLevel(TRACE, () -> {
            extractor.extract("");
            assertThat(LOGGING.logRecordsContaining("mark not found `a`")).isEmpty();
        });
    }

    @Test
    public void fallback_log_fine() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("a").orElse(Fallback.LOG_FINE)
        );
        LOGGING.withCustomLog4jLevel(TRACE, () -> {
            extractor.extract("");
            assertThat(LOGGING.logRecordsContaining("mark not found `a`")).isNotEmpty();
        });
    }

    @Test
    public void fallback_log_info() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("a").orElse(Fallback.LOG_INFO)
        );
        LOGGING.withCustomLog4jLevel(INFO, () -> {
            extractor.extract("");
            assertThat(LOGGING.logRecordsContaining("mark not found `a`")).isNotEmpty();
        });
    }

    @Test
    public void fallback_log_warn() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("a").orElse(Fallback.LOG_WARN)
        );
        extractor.extract("");
        LOGGING.withCustomLog4jLevel(WARN, () -> {
            extractor.extract("");
            assertThat(LOGGING.logRecordsContaining("mark not found `a`")).isNotEmpty();
        });
    }

    @Test
    public void fallback_log_severe() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("a").orElse(Fallback.LOG_SEVERE)
        );
        LOGGING.withCustomLog4jLevel(ERROR, () -> {
            extractor.extract("");
            assertThat(LOGGING.logRecordsContaining("mark not found `a`")).isNotEmpty();
        });
    }

    @Test
    public void fallback_throw() {
        TextExtractor extractor = TextExtractor.of(
            TextExtractor.skipTo("a").orElseThrow()
        );
        assertThrows(IllegalStateException.class, () -> extractor.extract(""));
    }

    @Test
    public void fallback_double_call() {
        assertThrows(AssertionError.class, () -> TextExtractor.skipTo("").orElseIgnore().orElseThrow());
    }

    /** {@link TextExtractor#with(Sanitizer)} */

    @Test
    public void sanitizer_trim_spaces() {
        TextExtractor extractor = TextExtractor.of(
            captureCurrent("[a]")
        ).with(Sanitizer.TRIM_SPACES);
        ExtractedMap map = extractor.extract("  <body>TheBody</body> \n");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "<body>TheBody</body>");
    }

    @Test
    public void sanitizer_no_nothing() {
        TextExtractor extractor = TextExtractor.of(
            captureCurrent("[a]")
        ).with(Sanitizer.DO_NOTHING);
        ExtractedMap map = extractor.extract("  <body>TheBody</body> \n");
        assertExtractedMap(map).toCaptured().containsExactly("[a]", "  <body>TheBody</body> \n");
    }

    /* Testing Helpers */

    private static @NotNull TextExtractor.MatchCapture captureCurrent(@NotNull String name) {
        return new MatchCapture(name) {
            @Override @NotNull CharArray captureFrom(@NotNull MutableCharArray array) {
                return array;
            }
        };
    }

    private static @NotNull ExtractedMapSubject assertExtractedMap(@NotNull ExtractedMap map) {
        return new ExtractedMapSubject(map);
    }

    private record ExtractedMapSubject(@NotNull ExtractedMap map) {
        public @NotNull MapSubject toMap() {
            return assertThat(map.map());
        }

        public @NotNull MapSubject toCaptured() {
            return assertThat(Streamer.of(map.map()).mapValues(extr -> extr.captured().toString()).toOrderedMap());
        }

        public @NotNull MapSubject toConverted() {
            return assertThat(Streamer.of(map.map()).mapValues(Extracted::convertedValue).toOrderedMap());
        }
    }
}
