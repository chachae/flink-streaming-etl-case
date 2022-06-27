package com.chachae.flink.demo.window;

import com.chachae.flink.demo.model.SensorReading;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * 增量聚合的 ProcessWindowFunction #
 * ProcessWindowFunction 可以与 ReduceFunction 或 AggregateFunction 搭配使用， 使其能够在数据到达窗口的时候进行增量聚合。当窗口关闭时，ProcessWindowFunction 将会得到聚合的结果。 这样它就可以增量聚合窗口的元素并且从 ProcessWindowFunction` 中获得窗口的元数据。
 * <p>
 * 你也可以对过时的 WindowFunction 使用增量聚合。
 * <p>
 * 使用 ReduceFunction 增量聚合 #
 * 下例展示了如何将 ReduceFunction 与 ProcessWindowFunction 组合，返回窗口中的最小元素和窗口的开始时间。
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:22
 */
public class ProcessWindowFunctionTest {

    /**
     * DataStream<SensorReading> input = ...;
     * <p>
     * input
     * .keyBy(<key selector>)
     * .window(<window assigner>)
     * .reduce(new MyReduceFunction(), new MyProcessWindowFunction());
     */

    /**
     * Function definitions
     */
    public static class MyReduceFunction implements ReduceFunction<SensorReading> {

        @Override
        public SensorReading reduce(SensorReading value1, SensorReading value2) throws Exception {
            return value1.getValue() > value2.getValue() ? value2 : value1;
        }
    }

    public static class MyProcessWindowFunction extends ProcessWindowFunction<SensorReading, Tuple2<Long, SensorReading>, String, TimeWindow> {

        @Override
        public void process(
                String key
                , ProcessWindowFunction<SensorReading, Tuple2<Long, SensorReading>, String, TimeWindow>.Context context
                , Iterable<SensorReading> minReadings
                , Collector<Tuple2<Long, SensorReading>> out
        ) throws Exception {
            SensorReading min = minReadings.iterator().next();
            out.collect(new Tuple2<>(context.window().getStart(), min));
        }

    }

}
