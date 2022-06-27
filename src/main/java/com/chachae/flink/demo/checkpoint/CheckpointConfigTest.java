package com.chachae.flink.demo.checkpoint;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * checkpoint config
 * <a href="https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/fault-tolerance/checkpointing/"></a>
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 17:07
 */
public class CheckpointConfigTest {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();

        // 每 1000ms 开始一次 checkpoint
        env.enableCheckpointing(1000);

        // 高级选项：
        // 设置模式为精确一次 (这是默认值)
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        // 确认 checkpoints 之间的时间会进行 500 ms
        checkpointConfig.setMinPauseBetweenCheckpoints(500);

        // Checkpoint 必须在一分钟内完成，否则就会被抛弃
        checkpointConfig.setCheckpointTimeout(60000);

        // 允许两个连续的 checkpoint 错误
        checkpointConfig.setTolerableCheckpointFailureNumber(2);

        // 同一时间只允许一个 checkpoint 进行
        checkpointConfig.setMaxConcurrentCheckpoints(1);

        // 使用 externalized checkpoints，这样 checkpoint 在作业取消后仍就会被保留
        checkpointConfig.setExternalizedCheckpointCleanup(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 开启实验性的 unaligned checkpoints
        checkpointConfig.enableUnalignedCheckpoints();
    }

}
