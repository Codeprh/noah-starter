package com.noah.starter.demo.web.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;

public class TestForTest {

    @Benchmark
    @Warmup(iterations = 1, time = 3)//在专业测试里面首先要进行预热，预热多少次，预热多少时间
    @Fork(5)//意思是用多少个线程去执行我们的程序
    @BenchmarkMode(Mode.Throughput)//是对基准测试的一个模式，这个模式用的最多的是Throughput吞吐量
    @Measurement(iterations = 1, time = 3)//是整个测试要测试多少遍，调用这个方法要调用多少次
    public void testForEach() {
        TestFor.foreach();
    }

    @Benchmark
    @Warmup(iterations = 1, time = 3)//在专业测试里面首先要进行预热，预热多少次，预热多少时间
    @Fork(5)//意思是用多少个线程去执行我们的程序
    @BenchmarkMode(Mode.Throughput)//是对基准测试的一个模式，这个模式用的最多的是Throughput吞吐量
    @Measurement(iterations = 1, time = 3)//是整个测试要测试多少遍，调用这个方法要调用多少次
    public void testParallel() {
        TestFor.parallel();
    }


}