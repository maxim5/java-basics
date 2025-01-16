package io.spbx.util.collect.map;

import io.spbx.util.base.ops.ObjArrayOps;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class OpenHashTableJmhBenchmark {
    private static final String[] STRINGS = ObjArrayOps.fill(new String[1000], String::valueOf);

    @Benchmark
    public void HashMap_put_get(Blackhole blackhole) {
        HashMap<String, String> hashMap = new HashMap<>(10000);
        for (String s : STRINGS) {
            hashMap.put(s, s);
        }
        for (String s : STRINGS) {
            hashMap.get(s);
        }
        blackhole.consume(hashMap);
    }

    @Benchmark
    public void OpenHashTable_put_get(Blackhole blackhole) {
        OpenHashTable<String, String> table = new OpenHashTable<>(10000);
        for (String s : STRINGS) {
            table.put(s, s);
        }
        for (String s : STRINGS) {
            table.get(s);
        }
        blackhole.consume(table);
    }

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.separateClasspathJAR", "true");
        Options options = new OptionsBuilder().include(OpenHashTableJmhBenchmark.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
