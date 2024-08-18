package io.spbx.util.text;

import org.junit.jupiter.api.Test;

import static io.spbx.util.testing.AssertStreamer.assertStream;
import static io.spbx.util.testing.AssertFailure.assertFailure;

public class BasicSplitTest {
    @Test
    public void empty_input() {
        String input = "";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).isEmpty();

        assertStream(BasicSplit.of(input).exactly(1).on(',')).containsExactly("");
        assertFailure(() -> BasicSplit.of(input).exactly(0).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(2).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(9).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).isEmpty();
    }

    @Test
    public void single_separator() {
        String input = ",";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("", "");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).isEmpty();

        assertStream(BasicSplit.of(input).exactly(2).on(',')).containsExactly("", "");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(3).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("", "");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("", "");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("", "");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("", "");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("", "");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).isEmpty();
    }

    @Test
    public void only_separators() {
        String input = ",,,";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("", "", "", "");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).isEmpty();

        assertStream(BasicSplit.of(input).exactly(4).on(',')).containsExactly("", "", "", "");
        assertFailure(() -> BasicSplit.of(input).exactly(2).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(3).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(5).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("", "");
        assertStream(BasicSplit.of(input).limit(3).on(',')).containsExactly("", "", "");
        assertStream(BasicSplit.of(input).limit(4).on(',')).containsExactly("", "", "", "");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("", "", "", "");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("", ",,");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("", "", ",");
        assertStream(BasicSplit.of(input).limit(3).includeRestAfterLimit().on(',')).containsExactly("", "", "", "");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("", "", "", "");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).isEmpty();
    }

    @Test
    public void single_value() {
        String input = "foo";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo");

        assertStream(BasicSplit.of(input).exactly(1).on(',')).containsExactly("foo");
        assertFailure(() -> BasicSplit.of(input).exactly(0).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(2).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("foo");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("foo");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo");
    }

    @Test
    public void value_with_trailing_separator() {
        String input = "foo,";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("foo", "");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo");

        assertStream(BasicSplit.of(input).exactly(2).on(',')).containsExactly("foo", "");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(3).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("foo", "");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("foo", "");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("foo", "");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("foo", "");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("foo", "");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo");
    }

    @Test
    public void value_with_leading_separator() {
        String input = ",foo";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("", "foo");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo");

        assertStream(BasicSplit.of(input).exactly(2).on(',')).containsExactly("", "foo");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(3).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("", "foo");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("", "foo");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("", "foo");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("", "foo");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("", "foo");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo");
    }

    @Test
    public void two_values_separated() {
        String input = "foo,bar";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo", "bar");

        assertStream(BasicSplit.of(input).exactly(2).on(',')).containsExactly("foo", "bar");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(3).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("foo", "bar");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("foo", "bar");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo", "bar");
    }

    @Test
    public void two_values_separated_and_trailing() {
        String input = "foo,bar,";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("foo", "bar", "");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo", "bar");

        assertStream(BasicSplit.of(input).exactly(3).on(',')).containsExactly("foo", "bar", "");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(2).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(4).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).on(',')).containsExactly("foo", "bar", "");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("foo", "bar", "");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("foo", "bar,");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("foo", "bar", "");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("foo", "bar", "");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo", "bar");
    }

    @Test
    public void two_values_separated_and_leading() {
        String input = ",foo,bar";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("", "foo", "bar");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo", "bar");

        assertStream(BasicSplit.of(input).exactly(3).on(',')).containsExactly("", "foo", "bar");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(2).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(4).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("", "foo");
        assertStream(BasicSplit.of(input).limit(3).on(',')).containsExactly("", "foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("", "foo", "bar");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("", "foo,bar");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("", "foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).includeRestAfterLimit().on(',')).containsExactly("", "foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("", "foo", "bar");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo", "bar");
    }

    @Test
    public void values_with_empty_separated() {
        String input = "foo,,bar";

        assertStream(BasicSplit.of(input).on(',')).containsExactly("foo", "", "bar");
        assertStream(BasicSplit.of(input).skipEmpty().on(',')).containsExactly("foo", "bar");

        assertStream(BasicSplit.of(input).exactly(3).on(',')).containsExactly("foo", "", "bar");
        assertFailure(() -> BasicSplit.of(input).exactly(1).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(2).on(',').toArrayList()).throwsAssertion();
        assertFailure(() -> BasicSplit.of(input).exactly(4).on(',').toArrayList()).throwsAssertion();

        assertStream(BasicSplit.of(input).limit(0).on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).on(',')).containsExactly("foo", "");
        assertStream(BasicSplit.of(input).limit(3).on(',')).containsExactly("foo", "", "bar");
        assertStream(BasicSplit.of(input).limit(4).on(',')).containsExactly("foo", "", "bar");
        assertStream(BasicSplit.of(input).limit(9).on(',')).containsExactly("foo", "", "bar");

        assertStream(BasicSplit.of(input).limit(0).includeRestAfterLimit().on(',')).containsExactly(input);
        assertStream(BasicSplit.of(input).limit(1).includeRestAfterLimit().on(',')).containsExactly("foo", ",bar");
        assertStream(BasicSplit.of(input).limit(2).includeRestAfterLimit().on(',')).containsExactly("foo", "", "bar");
        assertStream(BasicSplit.of(input).limit(3).includeRestAfterLimit().on(',')).containsExactly("foo", "", "bar");
        assertStream(BasicSplit.of(input).limit(9).includeRestAfterLimit().on(',')).containsExactly("foo", "", "bar");

        assertStream(BasicSplit.of(input).limit(0).skipEmpty().on(',')).isEmpty();
        assertStream(BasicSplit.of(input).limit(1).skipEmpty().on(',')).containsExactly("foo");
        assertStream(BasicSplit.of(input).limit(2).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(3).skipEmpty().on(',')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).limit(9).skipEmpty().on(',')).containsExactly("foo", "bar");
    }

    @Test
    public void split_by_dot() {
        String input = "foo.bar";

        assertStream(BasicSplit.of(input).on('.')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).skipEmpty().on('.')).containsExactly("foo", "bar");
    }

    @Test
    public void split_by_star() {
        String input = "foo*bar";

        assertStream(BasicSplit.of(input).on('*')).containsExactly("foo", "bar");
        assertStream(BasicSplit.of(input).skipEmpty().on('*')).containsExactly("foo", "bar");
    }
}
