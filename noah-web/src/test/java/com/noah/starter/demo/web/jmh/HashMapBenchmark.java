package com.noah.starter.demo.web.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 3, time = 5)
@State(Scope.Benchmark)
@Fork(2)
public class HashMapBenchmark {

    private static final String KEY = "mxsm";

    private final Map<String, Object> concurrentMap = new ConcurrentHashMap<>();

    @Setup(Level.Iteration)
    public void setup() {
        concurrentMap.clear();
    }

    @Benchmark
    @Threads(16)
    public Object benchmarkGetBeforeComputeIfAbsent() {
        Object result = concurrentMap.get(KEY);
        if (null == result) {
            result = concurrentMap.computeIfAbsent(KEY, key -> 1);
        }
        return result;
    }

    @Benchmark
    @Threads(16)
    public Object benchmarkComputeIfAbsent() {
        return concurrentMap.computeIfAbsent(KEY, key -> 1);
    }

}