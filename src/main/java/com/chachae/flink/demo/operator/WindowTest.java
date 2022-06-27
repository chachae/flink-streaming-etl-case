package com.chachae.flink.demo.operator;

import com.chachae.flink.demo.function.KeyByF0Function;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 17:50
 */
public class WindowTest {

    public static void main(String[] args) {
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<Tuple2<String, Integer>> wcStream = env.fromElements(
                new Tuple2<String, Integer>("hello", 1),
                new Tuple2<String, Integer>("world", 2),
                new Tuple2<String, Integer>("world", 3)
        );

        wcStream.keyBy(new KeyByF0Function())
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new WindowFunction<Tuple2<String, Integer>, Integer, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow timeWindow, Iterable<Tuple2<String, Integer>> iterable, Collector<Integer> out) throws Exception {
                        int sum = 0;
                        for (Tuple2<String, Integer> element : iterable) {
                            sum += element.f1;
                        }
                        out.collect(sum);
                    }
                }).print();
    }

}
