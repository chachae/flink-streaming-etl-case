package com.chachae.flink.demo.aliyunlog;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.Objects;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/7/8 15:11
 */
public class SumWindowAggregateFunction extends RichFlatMapFunction<Tuple3<String, Integer, Integer>, Tuple2<String, Integer>> {

    /**
     * running flag
     */
    private volatile boolean isRunning;

    @Getter(AccessLevel.PRIVATE)
    private ValueState<Tuple2<String, Integer>> keyValueState;

    private final StateTtlConfig ttlConfig = StateTtlConfig
            .newBuilder(Time.minutes(65))
            .cleanupInRocksdbCompactFilter(1000)
            .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
            .build();

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Tuple2<String, Integer>> descriptor = new ValueStateDescriptor<>(
                "sumWindowAggregateFunction"
                // @formatter:off
                , TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {})
                // @formatter:on
        );

        descriptor.enableTimeToLive(ttlConfig);
        keyValueState = this.getRuntimeContext().getState(descriptor);
        isRunning = true;
    }

    @Override
    public void close() throws Exception {
        isRunning = false;
    }

    @Override
    public void flatMap(Tuple3<String, Integer, Integer> in, Collector<Tuple2<String, Integer>> out) throws Exception {
        if (!isRunning) {
            return;
        }

        ValueState<Tuple2<String, Integer>> valueState = this.getKeyValueState();
        Tuple2<String, Integer> value = valueState.value();

        // check
        value = Objects.isNull(value) ? Tuple2.of(in.f0, in.f1) : Tuple2.of(in.f0, value.f1 + in.f1);

        // update
        this.getKeyValueState().update(value);

        // collect
        out.collect(value);
    }

}
