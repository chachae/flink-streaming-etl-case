package com.chachae.flink.demo.project.uvpvcompute;

import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/28 15:24
 */
public class UvPvComputeTest {

    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String topicName = parameterTool.get(Consts.KAFKA_TOPIC_KEY);
        String servers = parameterTool.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);

        // kafka source
        KafkaSource<UserBehaviorEvent> kafkaSource = KafkaSource.<UserBehaviorEvent>builder()
                .setBootstrapServers(servers)
                .setTopics(topicName)
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new UserBehaviorKafkaSourceSchema())
                .build();

        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStreamSource<UserBehaviorEvent> dataStreamSource = env.fromSource(kafkaSource, new Wm(), "kafka-source");

        dataStreamSource.windowAll(SlidingEventTimeWindows.of(Time.seconds(5), Time.seconds(1)))
                .allowedLateness(Time.minutes(5))
                .process(new ProcessAllWindowFunction<UserBehaviorEvent, Tuple4<Long, Long, Long, Long>, TimeWindow>() {
                    @Override
                    public void process(
                            ProcessAllWindowFunction<UserBehaviorEvent, Tuple4<Long, Long, Long, Long>, TimeWindow>.Context context
                            , Iterable<UserBehaviorEvent> elements
                            , Collector<Tuple4<Long, Long, Long, Long>> out
                    ) {
                        Set<Integer> userSet = new HashSet<>();
                        long pv = 0;
                        for (UserBehaviorEvent userBehaviorEvent : elements) {
                            userSet.add(userBehaviorEvent.getUserId());
                            ++pv;
                        }
                        out.collect(Tuple4.of(context.window().getStart(), context.window().getEnd(), pv, (long) userSet.size()));
                    }
                })
                .print()
                .setParallelism(1);

        env.execute();
    }

    public static class Wm implements WatermarkStrategy<UserBehaviorEvent> {

        @Override
        public WatermarkGenerator<UserBehaviorEvent> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
            return new PeriodicWatermarkGenerator();
        }

        @Override
        public TimestampAssigner<UserBehaviorEvent> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
            return new TimeStampExtractor();
        }

    }

    public static class PeriodicWatermarkGenerator implements WatermarkGenerator<UserBehaviorEvent> {
        private long currentWatermark = Long.MIN_VALUE;

        @Override
        public void onEvent(UserBehaviorEvent event, long eventTimestamp, WatermarkOutput output) {
            this.currentWatermark = eventTimestamp;
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            long effectiveWatermark =
                    currentWatermark == Long.MIN_VALUE ? Long.MIN_VALUE : currentWatermark - 1;
            output.emitWatermark(new Watermark(effectiveWatermark));
        }
    }

    public static class TimeStampExtractor implements TimestampAssigner<UserBehaviorEvent> {

        @Override
        public long extractTimestamp(UserBehaviorEvent element, long recordTimestamp) {
            return element.getTs();
        }
    }

}
