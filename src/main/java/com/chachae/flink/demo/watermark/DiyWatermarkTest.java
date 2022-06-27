package com.chachae.flink.demo.watermark;

import com.chachae.flink.demo.model.Event;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

/**
 * TTL 配置有以下几个选项： newBuilder 的第一个参数表示数据的有效期，是必选项。
 * <p>
 * TTL 的更新策略（默认是 OnCreateAndWrite）：
 * <p>
 * StateTtlConfig.UpdateType.OnCreateAndWrite - 仅在创建和写入时更新
 * <p>
 * StateTtlConfig.UpdateType.OnReadAndWrite - 读取时也更新
 * <p>
 * (注意: 如果你同时将状态的可见性配置为 StateTtlConfig.StateVisibility.ReturnExpiredIfNotCleanedUp， 那么在PyFlink作业中，状态的读缓存将会失效，这将导致一部分的性能损失)
 *
 * <a href="https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/fault-tolerance/state/">about state TTL config</a>
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 15:34
 */
public class DiyWatermarkTest {

    /**
     * 该 watermark 生成器可以覆盖的场景是：数据源在一定程度上乱序。
     * 即某个最新到达的时间戳为 t 的元素将在最早到达的时间戳为 t 的元素之后最多 n 毫秒到达。
     */
    public static class BoundedOutOfOrdernessGenerator implements WatermarkGenerator<Event> {

        // 3.5 秒
        private final long maxOutOfOrderness = 3_500L;

        private long currentMaxTimestamp;

        @Override
        public void onEvent(Event event, long eventTimestamp, WatermarkOutput output) {
            this.currentMaxTimestamp = Math.max(currentMaxTimestamp, eventTimestamp);
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            // 发出的 watermark = 当前最大时间戳 - 最大乱序时间
            output.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderness - 1));
        }

    }

    /**
     * 该生成器生成的 watermark 滞后于处理时间固定量。它假定元素会在有限延迟后到达 Flink。
     */
    public class TimeLagWatermarkGenerator implements WatermarkGenerator<Event> {

        private final long maxTimeLag = 5000; // 5 秒

        @Override
        public void onEvent(Event event, long eventTimestamp, WatermarkOutput output) {
            // 处理时间场景下不需要实现
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            output.emitWatermark(new Watermark(System.currentTimeMillis() - maxTimeLag));
        }
    }

}
