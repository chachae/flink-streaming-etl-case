package com.chachae.flink.demo;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * 使用 Operator State #
 * 用户可以通过实现 CheckpointedFunction 接口来使用 operator state。
 * <p>
 * CheckpointedFunction #
 * CheckpointedFunction 接口提供了访问 non-keyed state 的方法，需要实现如下两个方法：
 * <p>
 * void snapshotState(FunctionSnapshotContext context) throws Exception;
 * <p>
 * void initializeState(FunctionInitializationContext context) throws Exception;
 * 进行 checkpoint 时会调用 snapshotState()。 用户自定义函数初始化时会调用 initializeState()，初始化包括第一次自定义函数初始化和从之前的 checkpoint 恢复。 因此 initializeState() 不仅是定义不同状态类型初始化的地方，也需要包括状态恢复的逻辑。
 * <p>
 * 当前 operator state 以 list 的形式存在。这些状态是一个 可序列化 对象的集合 List，彼此独立，方便在改变并发后进行状态的重新分派。 换句话说，这些对象是重新分配 non-keyed state 的最细粒度。根据状态的不同访问方式，有如下几种重新分配的模式：
 * <p>
 * Even-split redistribution: 每个算子都保存一个列表形式的状态集合，整个状态由所有的列表拼接而成。当作业恢复或重新分配的时候，整个状态会按照算子的并发度进行均匀分配。 比如说，算子 A 的并发读为 1，包含两个元素 element1 和 element2，当并发读增加为 2 时，element1 会被分到并发 0 上，element2 则会被分到并发 1 上。
 * <p>
 * Union redistribution: 每个算子保存一个列表形式的状态集合。整个状态由所有的列表拼接而成。当作业恢复或重新分配时，每个算子都将获得所有的状态数据。 Do not use this feature if your list may have high cardinality. Checkpoint metadata will store an offset to each list entry, which could lead to RPC framesize or out-of-memory errors.
 * <p>
 * 下面的例子中的 SinkFunction 在 CheckpointedFunction 中进行数据缓存，然后统一发送到下游，这个例子演示了列表状态数据的 event-split redistribution。
 * <p>
 * https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/fault-tolerance/state/
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/24 17:58
 */
public class BufferingSinkTest implements SinkFunction<Tuple2<String, Integer>>, CheckpointedFunction {

    public static void main(String[] args) {

    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {

    }

    @Override
    public void initializeState(FunctionInitializationContext functionInitializationContext) throws Exception {

    }

    @Override
    public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
        SinkFunction.super.invoke(value, context);
    }

}
