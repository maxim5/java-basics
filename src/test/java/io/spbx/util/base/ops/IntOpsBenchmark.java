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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;

@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class IntOpsBenchmark {
    private static final int BYTE_ARRAY_LEN = 65536;
    private static final byte[] BYTE_ARRAY = ByteOps.fill(BYTE_ARRAY_LEN, (byte) -1);
    private static final int LOOP = 20_000;

    @Benchmark public void byte_buffer_wrap_as_int_buffer(Blackhole blackhole) {
        for (int iter = 0; iter < LOOP; ++iter) {
            byte[] bytes = BYTE_ARRAY;
            IntBuffer intBuf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
            int[] array = new int[intBuf.remaining()];
            intBuf.get(array);
            blackhole.consume(array);
        }
    }

    @Benchmark public void array_iteration(Blackhole blackhole) {
        for (int iter = 0; iter < LOOP; ++iter) {
            byte[] bytes = BYTE_ARRAY;
            int[] array = new int[bytes.length / IntOps.BYTES];
            for (int i = 0, j = 0; i < array.length; i++, j += IntOps.BYTES) {
                array[i] = IntOps.valueOfBigEndianBytes(bytes, j);
            }
            blackhole.consume(array);
        }
    }

    public static void main(String[] args) throws RunnerException {
        System.setProperty("jmh.separateClasspathJAR", "true");
        Options options = new OptionsBuilder().include(IntOpsBenchmark.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
