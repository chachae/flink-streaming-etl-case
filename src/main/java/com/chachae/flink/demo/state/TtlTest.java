package com.chachae.flink.demo.state;

import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;

/**
 * 状态有效期 (TTL) #
 * 任何类型的 keyed state 都可以有 有效期 (TTL)。如果配置了 TTL 且状态值已过期，则会尽最大可能清除对应的值，这会在后面详述。
 * <p>
 * 所有状态类型都支持单元素的 TTL。 这意味着列表元素和映射元素将独立到期。
 * <p>
 * 在使用状态 TTL 前，需要先构建一个StateTtlConfig 配置对象。 然后把配置传递到 state descriptor 中启用 TTL 功能：
 * https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/fault-tolerance/state/
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/24 16:27
 */
public class TtlTest {

    public static void main(String[] args) {
        StateTtlConfig ttlConfig = StateTtlConfig
                .newBuilder(Time.seconds(1))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();

        ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("text state", String.class);
        stateDescriptor.enableTimeToLive(ttlConfig);
    }

}
