package io.spbx.util.base;

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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.spbx.util.testing.TestingBasics.longStreamOf;

@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class Int128JmhBenchmark {
    private static final int N = 2000;
    private static final Random RAND = new Random(42);
    private static final long[] LONGS = IntStream.range(0, N).mapToLong(i -> RAND.nextLong()).toArray();
    private static final List<Int128> DOUBLES_64 = longStreamOf(LONGS).mapToObj(Int128::from).toList();
    private static final List<Int128> DOUBLES_128 = longStreamOf(LONGS).mapToObj(v -> Int128.fromBits(v, ~v)).toList();
    private static final List<BigInteger> BIGS_64 = DOUBLES_64.stream().map(Int128::toBigInteger).toList();
    private static final List<BigInteger> BIGS_128 = DOUBLES_128.stream().map(Int128::toBigInteger).toList();

    @Benchmark public void Int128_add_64(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_64)
                blackhole.consume(x.add(y));
    }

    @Benchmark public void Int128_add_128(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_128)
                blackhole.consume(x.add(y));
    }

    @Benchmark public void BigInteger_add_64(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_64)
                blackhole.consume(x.add(y));
    }

    @Benchmark public void BigInteger_add_128(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_128)
                blackhole.consume(x.add(y));
    }


    @Benchmark public void Int128_subtract_64(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_64)
                blackhole.consume(x.subtract(y));
    }

    @Benchmark public void Int128_subtract_128(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_128)
                blackhole.consume(x.subtract(y));
    }

    @Benchmark public void BigInteger_subtract_64(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_64)
                blackhole.consume(x.subtract(y));
    }

    @Benchmark public void BigInteger_subtract_128(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_128)
                blackhole.consume(x.subtract(y));
    }

    @Benchmark public void Int128_multiply_64(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_64)
                blackhole.consume(x.multiply(y));
    }

    @Benchmark public void Int128_multiply_128(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_128)
                blackhole.consume(x.multiply(y));
    }

    @Benchmark public void BigInteger_multiply_64(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_64)
                blackhole.consume(x.multiply(y));
    }

    @Benchmark public void BigInteger_multiply_128(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_128)
                blackhole.consume(x.multiply(y));
    }

    @Benchmark public void Int128_divide_64(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_64)
                blackhole.consume(x.divide(y));
    }

    @Benchmark public void Int128_divide_128(Blackhole blackhole) {
        for (Int128 x : DOUBLES_128)
            for (Int128 y : DOUBLES_128)
                blackhole.consume(x.divide(y));
    }

    @Benchmark public void BigInteger_divide_64(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_64)
                blackhole.consume(x.divide(y));
    }

    @Benchmark public void BigInteger_divide_128(Blackhole blackhole) {
        for (BigInteger x : BIGS_128)
            for (BigInteger y : BIGS_128)
                blackhole.consume(x.divide(y));
    }

    public static void main(String[] args) throws RunnerException {
        System.out.println(Arrays.toString(LONGS));
        System.setProperty("jmh.separateClasspathJAR", "true");
        Options options = new OptionsBuilder().include(Int128JmhBenchmark.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
