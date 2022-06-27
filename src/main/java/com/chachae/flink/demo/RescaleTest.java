package com.chachae.flink.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

/**
 * TODO Description
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/14 18:27
 */
@Slf4j
public class RescaleTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里使用了并行数据源的富函数版本
        // 这样可以调用 getRuntimeContext 方法来获取运行时上下文的一些信息
        env.addSource(new RichParallelSourceFunction<Integer>() {
                    @Override
                    public void run(SourceContext<Integer> sourceContext) throws
                            Exception {
                        for (int i = 0; i < 8; i++) {
                            // 将奇数发送到索引为 1 的并行子任务
                            // 将偶数发送到索引为 0 的并行子任务
                            int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
                            if ((i + 1) % 2 ==
                                    getRuntimeContext().getIndexOfThisSubtask()) {
                                System.out.printf("indexOfThisSubtask -> %s | i -> %s%n", indexOfThisSubtask, i);
                                sourceContext.collect(i + 1);
                            }
                        }
                    }

                    @Override
                    public void cancel() {
                    }
                })
                .setParallelism(2)
                .rescale()
                .print().setParallelism(4);
        env.execute();
    }

}
