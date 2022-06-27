package com.chachae.flink.demo.operator;

import com.chachae.flink.demo.function.KeyByF0Function;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 17:45
 */
public class ReduceTest {

    public static void main(String[] args) throws Exception {
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<Tuple2<String, Integer>> wcStream = env.fromElements(
                new Tuple2<String, Integer>("hello", 1),
                new Tuple2<String, Integer>("world", 2),
                new Tuple2<String, Integer>("world", 3)
        );

        wcStream.keyBy(new KeyByF0Function())
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                })
                .setParallelism(1)
                .print();

        env.execute();
    }


}
