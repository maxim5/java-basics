package io.spbx.util.collect;

import com.google.common.collect.MoreCollectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.BasicExceptions.newUnsupportedOperationException;
import static io.spbx.util.testing.AssertFailure.assertFailure;

@Tag("fast")
public class ToCollectKvApiTest {
    private static final IntEntry NULL_ENTRY = null;
    private static final IntEntry ENTRY1 = new IntEntry(1, 1);
    private static final IntEntry ENTRY2 = new IntEntry(2, 2);

    @Test
    public void collect_simple() {
        assertThat(ToCollectKvApi.of().collect(MoreCollectors.toOptional())).isEmpty();
        assertThat(ToCollectKvApi.of(ENTRY1).collect(MoreCollectors.toOptional())).hasValue(ENTRY1);
        assertFailure(() -> ToCollectKvApi.of(NULL_ENTRY).collect(MoreCollectors.toOptional())).throwsNPE();
        assertFailure(() -> ToCollectKvApi.of(ENTRY1, ENTRY2).collect(MoreCollectors.toOptional())).throwsIllegalArgument();
    }

    @Test
    public void collect_default() {
        assertThat(ToCollectKvApi.of(ENTRY1).collect(MoreCollectors.toOptional(), Optional.empty())).hasValue(ENTRY1);
        assertThat(ToCollectKvApi.of(ENTRY1, ENTRY2).collect(MoreCollectors.toOptional(), Optional.empty())).isEmpty();
        assertThat(
            ToCollectKvApi.of(ENTRY1, ENTRY2).<Optional<IntEntry>>collect(MoreCollectors.toOptional(), Optional::empty))
            .isEmpty();
    }

    @Test
    public void toCollection_simple() {
        assertThat(ToCollectKvApi.of(new IntEntry[0]).<Stack<IntEntry>>toCollection(Stack::new)).isEmpty();
        assertThat(ToCollectKvApi.of(ENTRY1).<Stack<IntEntry>>toCollection(Stack::new)).containsExactly(ENTRY1);
        assertThat(ToCollectKvApi.of(ENTRY1, ENTRY2).<Stack<IntEntry>>toCollection(Stack::new)).containsExactly(ENTRY1, ENTRY2);
        assertThat(ToCollectKvApi.of(NULL_ENTRY).<Stack<IntEntry>>toCollection(Stack::new)).containsExactly(NULL_ENTRY);
    }

    @Test
    public void allMatch_simple() {
        assertThat(ToCollectKvApi.of(new IntEntry[0]).allMatch((x, y) -> x < 2 && y < 2)).isTrue();
        assertThat(ToCollectKvApi.of(ENTRY1).allMatch((x, y) -> x < 2 && y < 2)).isTrue();
        assertThat(ToCollectKvApi.of(ENTRY2).allMatch((x, y) -> x < 2 && y < 2)).isFalse();
        assertThat(ToCollectKvApi.of(ENTRY1, ENTRY2).allMatch((x, y) -> x < 2 && y < 2)).isFalse();
    }

    @Test
    public void anyMatch_simple() {
        assertThat(ToCollectKvApi.of(new IntEntry[0]).anyMatch((x, y) -> x < 2 && y < 2)).isFalse();
        assertThat(ToCollectKvApi.of(ENTRY1).anyMatch((x, y) -> x < 2 && y < 2)).isTrue();
        assertThat(ToCollectKvApi.of(ENTRY2).anyMatch((x, y) -> x < 2 && y < 2)).isFalse();
        assertThat(ToCollectKvApi.of(ENTRY1, ENTRY2).anyMatch((x, y) -> x < 2 && y < 2)).isTrue();
    }

    @Test
    public void noneMatch_simple() {
        assertThat(ToCollectKvApi.of(new IntEntry[0]).noneMatch((x, y) -> x < 2 && y < 2)).isTrue();
        assertThat(ToCollectKvApi.of(ENTRY1).noneMatch((x, y) -> x < 2 && y < 2)).isFalse();
        assertThat(ToCollectKvApi.of(ENTRY2).noneMatch((x, y) -> x < 2 && y < 2)).isTrue();
        assertThat(ToCollectKvApi.of(ENTRY1, ENTRY2).noneMatch((x, y) -> x < 2 && y < 2)).isFalse();
    }

    private record IntEntry(Integer getKey, Integer getValue) implements Map.Entry<Integer, Integer> {
        @Override public Integer setValue(Integer value) {
            throw newUnsupportedOperationException("setValue() unsupported");
        }
    }
}
