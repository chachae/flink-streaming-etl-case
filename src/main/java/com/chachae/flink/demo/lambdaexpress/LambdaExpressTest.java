package com.chachae.flink.demo.lambdaexpress;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:54
 */
public class LambdaExpressTest {

    public static void main(String[] args) {

        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        // 使用显式的 ".returns(...)"
        env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map(i -> Tuple2.of(i, i))
                .returns(Types.TUPLE(Types.INT, Types.INT))
                .print();


        // 使用类来替代
        env.fromElements(1, 2, 3)
                .map(new MyTuple2Mapper())
                .print();

        // 使用匿名类来替代
        env.fromElements(1, 2, 3)
                .map(new MapFunction<Integer, Tuple2<Integer, Integer>>() {
                    @Override
                    public Tuple2<Integer, Integer> map(Integer i) throws Exception {
                        return Tuple2.of(i, i);
                    }
                }).print();

        // 也可以像这个示例中使用 Tuple 的子类来替代
        env.fromElements(1, 2, 3)
                .map(i -> new DoubleTuple(i, i))
                .print();

    }

    public static class MyTuple2Mapper implements MapFunction<Integer, Tuple2<Integer, Integer>> {
        @Override
        public Tuple2<Integer, Integer> map(Integer i) {
            return Tuple2.of(i, i);
        }

    }


    public static class DoubleTuple extends Tuple2<Integer, Integer> {
        public DoubleTuple(int f0, int f1) {
            this.f0 = f0;
            this.f1 = f1;
        }

    }

}
