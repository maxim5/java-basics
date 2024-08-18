package io.spbx.util.collect;

import com.google.common.collect.MoreCollectors;
import io.spbx.util.base.Pair;
import io.spbx.util.base.Triple;
import io.spbx.util.base.Tuple;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Stack;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToCollectApiTest {
    private static final Integer NULL = null;

    @Test
    public void collect_simple() {
        assertThat(ToCollectApi.of().collect(MoreCollectors.toOptional())).isEmpty();
        assertThat(ToCollectApi.of(1).collect(MoreCollectors.toOptional())).hasValue(1);
        assertThrows(IllegalArgumentException.class, () -> ToCollectApi.of(1, 2).collect(MoreCollectors.toOptional()));
    }

    @Test
    public void collect_default() {
        assertThat(ToCollectApi.of(1).collect(MoreCollectors.toOptional(), Optional.empty())).hasValue(1);
        assertThat(ToCollectApi.of(1, 2).collect(MoreCollectors.toOptional(), Optional.empty())).isEmpty();
        assertThat(ToCollectApi.of(1, 2).<Optional<Integer>>collect(MoreCollectors.toOptional(), () -> Optional.of(0))).hasValue(0);
    }

    @Test
    public void toCollection_simple() {
        assertThat(ToCollectApi.of().<Stack<Object>>toCollection(Stack::new)).isEmpty();
        assertThat(ToCollectApi.of(1).<Stack<Integer>>toCollection(Stack::new)).containsExactly(1);
        assertThat(ToCollectApi.of(1, 2).<Stack<Integer>>toCollection(Stack::new)).containsExactly(1, 2);
        assertThat(ToCollectApi.of(NULL).<Stack<Integer>>toCollection(Stack::new)).containsExactly(NULL);
    }

    @Test
    public void toOptional_simple() {
        assertThat(ToCollectApi.of().toOptional()).isEmpty();
        assertThat(ToCollectApi.of(1).toOptional()).hasValue(1);
        assertThrows(IllegalArgumentException.class, () -> ToCollectApi.of(1, 2).toOptional());
        assertThrows(NullPointerException.class, () -> ToCollectApi.of(NULL).toOptional());
        assertThrows(NullPointerException.class, () -> ToCollectApi.of(1, NULL).toOptional());
    }

    @Test
    public void toAtMostOne_simple() {
        assertThat(ToCollectApi.of().toAtMostOne()).isNull();
        assertThat(ToCollectApi.of(1).toAtMostOne()).isEqualTo(1);
        assertThat(ToCollectApi.of(NULL).toAtMostOne()).isNull();
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2).toAtMostOne());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, NULL).toAtMostOne());
    }

    @Test
    public void toExactlyOne_simple() {
        assertThat(ToCollectApi.of(1).toExactlyOne()).isEqualTo(1);
        assertThat(ToCollectApi.of(NULL).toExactlyOne()).isEqualTo(NULL);
        assertThrows(NoSuchElementException.class, () -> ToCollectApi.of().toExactlyOne());
        assertThrows(IllegalArgumentException.class, () -> ToCollectApi.of(1, 2).toExactlyOne());
        assertThrows(IllegalArgumentException.class, () -> ToCollectApi.of(1, NULL).toExactlyOne());
    }

    @Test
    public void toAtMostTwo_simple() {
        assertThat(ToCollectApi.of().toAtMostTwo()).isEqualTo(Pair.of(NULL, NULL));
        assertThat(ToCollectApi.of(1).toAtMostTwo()).isEqualTo(Pair.of(1, NULL));
        assertThat(ToCollectApi.of(NULL).toAtMostTwo()).isEqualTo(Pair.of(NULL, NULL));
        assertThat(ToCollectApi.of(1, 2).toAtMostTwo()).isEqualTo(Pair.of(1, 2));
        assertThat(ToCollectApi.of(1, NULL).toAtMostTwo()).isEqualTo(Pair.of(1, NULL));
        assertThat(ToCollectApi.of(NULL, NULL).toAtMostTwo()).isEqualTo(Pair.of(NULL, NULL));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3).toAtMostTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, NULL).toAtMostTwo());
    }

    @Test
    public void toExactlyTwo_simple() {
        assertThat(ToCollectApi.of(1, 2).toExactlyTwo()).isEqualTo(Pair.of(1, 2));
        assertThat(ToCollectApi.of(1, NULL).toExactlyTwo()).isEqualTo(Pair.of(1, NULL));
        assertThat(ToCollectApi.of(NULL, NULL).toExactlyTwo()).isEqualTo(Pair.of(NULL, NULL));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of().toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1).toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(NULL).toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3).toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, NULL).toExactlyTwo());
    }

    @Test
    public void toAtMostThree_simple() {
        assertThat(ToCollectApi.of().toAtMostThree()).isEqualTo(Triple.of(NULL, NULL, NULL));
        assertThat(ToCollectApi.of(1).toAtMostThree()).isEqualTo(Triple.of(1, NULL, NULL));
        assertThat(ToCollectApi.of(NULL).toAtMostThree()).isEqualTo(Triple.of(NULL, NULL, NULL));
        assertThat(ToCollectApi.of(1, 2).toAtMostThree()).isEqualTo(Triple.of(1, 2, NULL));
        assertThat(ToCollectApi.of(1, NULL).toAtMostThree()).isEqualTo(Triple.of(1, NULL, NULL));
        assertThat(ToCollectApi.of(NULL, NULL).toAtMostThree()).isEqualTo(Triple.of(NULL, NULL, NULL));
        assertThat(ToCollectApi.of(1, 2, 3).toAtMostThree()).isEqualTo(Triple.of(1, 2, 3));
        assertThat(ToCollectApi.of(NULL, NULL, NULL).toAtMostThree()).isEqualTo(Triple.of(NULL, NULL, NULL));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3, 4).toAtMostThree());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3, NULL).toAtMostThree());
    }

    @Test
    public void toExactlyThree_simple() {
        assertThat(ToCollectApi.of(1, 2, 3).toExactlyThree()).isEqualTo(Triple.of(1, 2, 3));
        assertThat(ToCollectApi.of(1, NULL, 3).toExactlyThree()).isEqualTo(Triple.of(1, NULL, 3));
        assertThat(ToCollectApi.of(NULL, NULL, NULL).toExactlyThree()).isEqualTo(Triple.of(NULL, NULL, NULL));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of().toExactlyThree());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1).toExactlyThree());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(NULL).toExactlyThree());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2).toExactlyThree());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3, 4).toExactlyThree());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3, NULL).toExactlyThree());
    }

    @Test
    public void toAtMostN_simple() {
        assertThat(ToCollectApi.of().toAtMostN(2)).isEqualTo(Tuple.of(NULL, NULL));
        assertThat(ToCollectApi.of(1).toAtMostN(2)).isEqualTo(Tuple.of(1, NULL));
        assertThat(ToCollectApi.of(NULL).toAtMostN(2)).isEqualTo(Tuple.of(NULL, NULL));
        assertThat(ToCollectApi.of(1, 2).toAtMostN(2)).isEqualTo(Tuple.of(1, 2));
        assertThat(ToCollectApi.of(1, NULL).toAtMostN(2)).isEqualTo(Tuple.of(1, NULL));
        assertThat(ToCollectApi.of(NULL, NULL).toAtMostN(2)).isEqualTo(Tuple.of(NULL, NULL));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3).toAtMostN(2));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, NULL).toAtMostN(2));
    }

    @Test
    public void toExactlyN_simple() {
        assertThat(ToCollectApi.of(1, 2).toExactlyN(2)).isEqualTo(Tuple.of(1, 2));
        assertThat(ToCollectApi.of(1, NULL).toExactlyN(2)).isEqualTo(Tuple.of(1, NULL));
        assertThat(ToCollectApi.of(NULL, NULL).toExactlyN(2)).isEqualTo(Tuple.of(NULL, NULL));
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of().toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1).toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(NULL).toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, 3).toExactlyTwo());
        assertThrows(IllegalStateException.class, () -> ToCollectApi.of(1, 2, NULL).toExactlyTwo());
    }

    @Test
    public void toAtMostOneIgnoreNulls_simple() {
        assertThat(ToCollectApi.of().toAtMostOneIgnoreNulls()).isEmpty();
        assertThat(ToCollectApi.of(1).toAtMostOneIgnoreNulls()).hasValue(1);
        assertThat(ToCollectApi.of(NULL).toAtMostOneIgnoreNulls()).isEmpty();
        assertThat(ToCollectApi.of(NULL, NULL).toAtMostOneIgnoreNulls()).isEmpty();
        assertThat(ToCollectApi.of(NULL, 1, NULL).toAtMostOneIgnoreNulls()).hasValue(1);
        assertThat(ToCollectApi.of(1, 2).toAtMostOneIgnoreNulls()).isEmpty();
        assertThat(ToCollectApi.of(1, 2, NULL).toAtMostOneIgnoreNulls()).isEmpty();
    }

    @Test
    public void allMatch_simple() {
        assertThat(ToCollectApi.<Integer>of().allMatch(x -> x > 0)).isTrue();
        assertThat(ToCollectApi.of(1).allMatch(x -> x > 0)).isTrue();
        assertThat(ToCollectApi.of(0).allMatch(x -> x > 0)).isFalse();
        assertThat(ToCollectApi.of(0, 1).allMatch(x -> x > 0)).isFalse();
    }

    @Test
    public void anyMatch_simple() {
        assertThat(ToCollectApi.<Integer>of().anyMatch(x -> x > 0)).isFalse();
        assertThat(ToCollectApi.of(1).anyMatch(x -> x > 0)).isTrue();
        assertThat(ToCollectApi.of(0).anyMatch(x -> x > 0)).isFalse();
        assertThat(ToCollectApi.of(0, 1).anyMatch(x -> x > 0)).isTrue();
    }

    @Test
    public void noneMatch_simple() {
        assertThat(ToCollectApi.<Integer>of().noneMatch(x -> x > 0)).isTrue();
        assertThat(ToCollectApi.of(1).noneMatch(x -> x > 0)).isFalse();
        assertThat(ToCollectApi.of(0).noneMatch(x -> x > 0)).isTrue();
        assertThat(ToCollectApi.of(0, 1).noneMatch(x -> x > 0)).isFalse();
    }

    @Test
    public void allEqual_simple() {
        assertThat(ToCollectApi.<Integer>of().allEqual()).isTrue();
        assertThat(ToCollectApi.of(1).allEqual()).isTrue();
        assertThat(ToCollectApi.of(1, 1).allEqual()).isTrue();
        assertThat(ToCollectApi.of(1, 1, 1).allEqual()).isTrue();
        assertThat(ToCollectApi.of(1, 1, 1, 2).allEqual()).isFalse();
        assertThat(ToCollectApi.of(1, 2).allEqual()).isFalse();
        assertThat(ToCollectApi.of(1, 2, 3).allEqual()).isFalse();
        assertThat(ToCollectApi.of(NULL).allEqual()).isTrue();
    }
}
