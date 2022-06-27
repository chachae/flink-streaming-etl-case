package com.chachae.flink.demo.sideoutput;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 旁路输出case
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:47
 */
public class SideOutputTest {

    public static void main(String[] args) {

        DataStream<Integer> input = null;

        input.process(new ProcessFunction<Integer, Integer>() {
            @Override
            public void processElement(Integer value, ProcessFunction<Integer, Integer>.Context context, Collector<Integer> out) {

                // 发送数据到主要的输出
                out.collect(value);

                // 发送数据到旁路输出
                context.output(SideOutputConsts.SIDE_OUTPUT_TAG, "sideout-" + value);
            }
        });

        // 可以在 DataStream 运算结果上使用 getSideOutput(OutputTag) 方法获取旁路输出流。这将产生一个与旁路输出流结果类型一致的 DataStream：
        SingleOutputStreamOperator<Integer> mainDataStream = null;
        DataStream<String> sideOutputStream = mainDataStream.getSideOutput(SideOutputConsts.SIDE_OUTPUT_TAG);

    }

}
