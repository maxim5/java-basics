package io.spbx.util.base.ops;

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

import java.util.concurrent.TimeUnit;

@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class IntOpsJmhBenchmark {
    private static final int[] ARRAY = IntOps.fill(1000_000, i -> i);
    private static final int LOOP = 2000;

    @Benchmark
    public void array_iter1(Blackhole blackhole) {
        for (int iter = 0; iter < LOOP; ++iter) {
            for (int i = 0; i < ARRAY.length; i++) {
                blackhole.consume(ARRAY[i]);
            }
        }
    }

    @Benchmark
    public void array_iter2(Blackhole blackhole) {
        for (int iter = 0; iter < LOOP; ++iter) {
            for (int len = ARRAY.length, i = 0; i < len; i++) {
                blackhole.consume(ARRAY[i]);
            }
        }
    }

    @Benchmark
    public void array_iter3(Blackhole blackhole) {
        for (int iter = 0; iter < LOOP; ++iter) {
            for (int j : ARRAY) {
                blackhole.consume(j);
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.separateClasspathJAR", "true");
        Options options = new OptionsBuilder().include(IntOpsJmhBenchmark.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
