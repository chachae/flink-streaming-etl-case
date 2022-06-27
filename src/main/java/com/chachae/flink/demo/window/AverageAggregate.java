package com.chachae.flink.demo.window;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * ReduceFunction 是 AggregateFunction 的特殊情况。
 * AggregateFunction 接收三个类型：输入数据的类型(IN)、累加器的类型（ACC）和输出数据的类型（OUT）。
 * 输入数据的类型是输入流的元素类型，AggregateFunction 接口有如下几个方法： 把每一条元素加进累加器、创建初始累加器、合并两个累加器、从累加器中提取输出（OUT 类型）。
 * <p>
 * 与 ReduceFunction 相同，Flink 会在输入数据到达窗口时直接进行增量聚合。
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:12
 */
public class AverageAggregate implements AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Long>, Double> {

    @Override
    public Tuple2<Long, Long> createAccumulator() {
        return new Tuple2<>(0L, 0L);
    }

    @Override
    public Tuple2<Long, Long> add(Tuple2<String, Long> value, Tuple2<Long, Long> accumulator) {
        return new Tuple2<>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
    }

    @Override
    public Double getResult(Tuple2<Long, Long> accumulator) {
        return ((double) accumulator.f0) / accumulator.f1;
    }

    @Override
    public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
        return new Tuple2<>(a.f0 + b.f0, a.f1 + b.f1);
    }

}
