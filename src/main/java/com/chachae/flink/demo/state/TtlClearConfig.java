package com.chachae.flink.demo.state;

import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 16:10
 */
public class TtlClearConfig {

    /**
     * 过期数据的清理 #
     * 默认情况下，过期数据会在读取的时候被删除，例如 ValueState#value，同时会有后台线程定期清理（如果 StateBackend 支持的话）。
     * 可以通过 StateTtlConfig 配置关闭后台清理：
     */
    public void clearBackground() {
        StateTtlConfig build = StateTtlConfig.newBuilder(Time.seconds(1))
                .disableCleanupInBackground()
                .build();
    }

    /**
     * 全量快照时进行清理 #
     * 另外，你可以启用全量快照时进行清理的策略，这可以减少整个快照的大小。
     * 当前实现中不会清理本地的状态，但从上次快照恢复时，不会恢复那些已经删除的过期数据。
     * 该策略可以通过 StateTtlConfig 配置进行配置：
     * 这种策略在 RocksDBStateBackend 的增量 checkpoint 模式下无效。
     * <p>
     * 注意:
     * <p>
     * 这种清理方式可以在任何时候通过 StateTtlConfig 启用或者关闭，比如在从 savepoint 恢复时。
     */
    public void clearAllSnapshot() {
        StateTtlConfig build = StateTtlConfig.newBuilder(Time.seconds(1))
                .cleanupFullSnapshot()
                .build();
    }

    /**
     * 另外可以选择增量式清理状态数据，在状态访问或/和处理时进行。如果某个状态开启了该清理策略，则会在存储后端保留一个所有状态的惰性全局迭代器。
     * 每次触发增量清理时，从迭代器中选择已经过期的数进行清理。
     * <p>
     * 该特性可以通过 StateTtlConfig 进行配置：
     * 该策略有两个参数。 第一个是每次清理时检查状态的条目数，在每个状态访问时触发。
     * 第二个参数表示是否在处理每条记录时触发清理。
     * Heap backend 默认会检查 5 条状态，并且关闭在每条记录时触发清理。
     */
    public void clearIncr() {
        StateTtlConfig ttlConfig = StateTtlConfig
                .newBuilder(Time.seconds(1))
                .cleanupIncrementally(10, true)
                .build();
    }

    /**
     * 在 RocksDB 压缩时清理 #
     * 如果使用 RocksDB state backend，则会启用 Flink 为 RocksDB 定制的压缩过滤器。
     * RocksDB 会周期性的对数据进行合并压缩从而减少存储空间。 Flink 提供的 RocksDB 压缩过滤器会在压缩时过滤掉已经过期的状态数据。
     * <p>
     * 该特性可以通过 StateTtlConfig 进行配置：
     * Flink 处理一定条数的状态数据后，会使用当前时间戳来检测 RocksDB 中的状态是否已经过期，
     * 你可以通过 StateTtlConfig.newBuilder(...).cleanupInRocksdbCompactFilter(long queryTimeAfterNumEntries) 方法指定处理状态的条数。
     * 时间戳更新的越频繁，状态的清理越及时，但由于压缩会有调用 JNI 的开销，因此会影响整体的压缩性能。
     * RocksDB backend 的默认后台清理策略会每处理 1000 条数据进行一次。
     * <p>
     * 你还可以通过配置开启 RocksDB 过滤器的 debug 日志： log4j.logger.org.rocksdb.FlinkCompactionFilter=DEBUG
     */
    public void clearOnRocksdbCompact() {
        StateTtlConfig ttlConfig = StateTtlConfig
                .newBuilder(Time.seconds(1))
                .cleanupInRocksdbCompactFilter(1000)
                .build();
    }


}
