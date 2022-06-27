package com.chachae.flink.demo.watermark;

import com.chachae.flink.demo.model.Event;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

/**
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
        private final long maxOutOfOrderness = 3500;

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
