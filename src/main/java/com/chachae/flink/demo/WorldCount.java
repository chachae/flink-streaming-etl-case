package com.chachae.flink.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.nio.charset.StandardCharsets;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/10 21:07
 */
public class WorldCount {

    private static final String SP = " ";

    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String resourcePath = parameterTool.get("resourcePath");

        StreamExecutionEnvironment localEnvironment = StreamExecutionEnvironment.createLocalEnvironment();
        localEnvironment.readTextFile(resourcePath + "\\world.txt", String.valueOf(StandardCharsets.UTF_8))
                .setParallelism(1)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String str, Collector<Tuple2<String, Integer>> collector) {
                        for (String world : str.split(SP)) {
                            collector.collect(Tuple2.of(world, 1));
                        }
                    }
                }).keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> tuple2) throws Exception {
                        return tuple2.f0;
                    }
                }).sum(1)
                .print();
        localEnvironment.execute();
    }

}
