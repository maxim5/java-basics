package io.spbx.util.time;

import io.spbx.util.base.DataSize;
import io.spbx.util.base.Tuple;
import io.spbx.util.collect.BasicIterables;
import io.spbx.util.collect.RowListTabular;
import io.spbx.util.testing.MoreRandomArrays;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.ToIntFunction;

import static io.spbx.util.collect.TabularFormatter.BORDERLESS_FORMATTER;
import static io.spbx.util.testing.TestingBasics.longStreamOf;

public class RealTimeLogMemoryBenchmark {
    public static void main(String[] args) {
        RealTimeLogMemoryBenchmark benchmark = new RealTimeLogMemoryBenchmark();

        int[] freqBound = { 2, 10, 20, 24, 30, 40, 45, 50, 80, 90, 100, 120, 150, 180, 200, 220, 250 };

        List<Tuple> tuples = BasicIterables.newMutableList();
        for (int freq : freqBound) {
            tuples.add(Tuple.of("RealTimeLog8", 4, freq / 2, DataSize.ofBytes(benchmark.avgMemorySizeOf(RTL_8, 4, freq))));
        }
        for (int freq : freqBound) {
            tuples.add(Tuple.of("RealTimeLog8", 8, freq / 2, DataSize.ofBytes(benchmark.avgMemorySizeOf(RTL_8, 8, freq))));
        }
        for (int freq : freqBound) {
            tuples.add(Tuple.of("RealTimeLog8", 16, freq / 2, DataSize.ofBytes(benchmark.avgMemorySizeOf(RTL_8, 16, freq))));
        }
        for (int freq : freqBound) {
            tuples.add(Tuple.of("RealTimeLog8", 32, freq / 2, DataSize.ofBytes(benchmark.avgMemorySizeOf(RTL_8, 32, freq))));
        }

        System.out.println(BORDERLESS_FORMATTER.formatIntoTableString(RowListTabular.ofTuples(tuples)));
    }

    private <RTL extends RealTimeLog> double avgMemorySizeOf(@NotNull RtlProvider<RTL> provider, int num, long bound) {
        int size = 100_000;
        long[] seeds = { 0, 10, 20, 30, 40 };
        return longStreamOf(seeds).map(
            seed -> memorySizeOf(provider.create(num), seed, size, bound, provider::size)
        ).average().orElse(0);
    }

    private <RTL extends RealTimeLog> int memorySizeOf(@NotNull RTL realTimeLog, long seed, int size, long bound,
                                                       @NotNull ToIntFunction<RTL> sizer) {
        long[] array = MoreRandomArrays.of(seed).nextIncreasingLongs(size, bound);
        longStreamOf(array).forEach(realTimeLog::append);
        return sizer.applyAsInt(realTimeLog);
    }

    private static final Rtl8 RTL_8 = new Rtl8();
    
    private interface RtlProvider<RTL extends RealTimeLog> {
        RTL create(int num);
        int size(RTL instance);
    }

    private static class Rtl8 implements RtlProvider<RealTimeLog8> {
        @Override public RealTimeLog8 create(int num) {
            return RealTimeLog8.allocate(num);
        }
        @Override public int size(RealTimeLog8 log) {
            return log.internalHistory().length + log.internalSpans().length * 4;
        }
    }
}
