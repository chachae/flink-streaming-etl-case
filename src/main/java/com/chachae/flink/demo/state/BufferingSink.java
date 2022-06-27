package com.chachae.flink.demo.state;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户可以通过实现 CheckpointedFunction 接口来使用 operator state。
 * https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/fault-tolerance/state/
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 16:27
 */
public class BufferingSink implements SinkFunction<Tuple2<String, Integer>>, CheckpointedFunction {

    /**
     * 发送数据阈值
     */
    private final int threshold;

    private transient ListState<Tuple2<String, Integer>> checkpointedState;

    private List<Tuple2<String, Integer>> bufferedElements;

    public BufferingSink(int threshold) {
        this.threshold = threshold;
        this.bufferedElements = new ArrayList<>();
    }

    @Override
    public void invoke(Tuple2<String, Integer> value, Context contex) throws Exception {
        bufferedElements.add(value);
        if (bufferedElements.size() >= threshold) {
            for (Tuple2<String, Integer> element : bufferedElements) {
                // add to sink
            }
        }
        bufferedElements.clear();
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        this.checkpointedState.clear();
        for (Tuple2<String, Integer> element : bufferedElements) {
            this.checkpointedState.add(element);
        }
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        this.checkpointedState = context.getOperatorStateStore().getListState(new ListStateDescriptor<Tuple2<String, Integer>>(
                "buffered-elements"
                , TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {
        })
        ));

        if (context.isRestored()) {
            for (Tuple2<String, Integer> element : this.checkpointedState.get()) {
                bufferedElements.add(element);
            }
        }
    }
}
