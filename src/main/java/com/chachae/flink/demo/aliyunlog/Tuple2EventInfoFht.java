package com.chachae.flink.demo.aliyunlog;

import cn.hutool.core.text.StrPool;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/7/8 15:41
 */
public class Tuple2EventInfoFht implements MapFunction<Tuple2<String, Integer>, EventInfoFht> {

    @Override
    public EventInfoFht map(Tuple2<String, Integer> value) throws Exception {
        String f0 = value.f0;
        Integer pv = value.f1;

        String[] keyContent = f0.split(StrPool.DASHED);

        return EventInfoFht.builder()
                .act(keyContent[0])
                .eventId(keyContent[1])
                .dt(keyContent[2])
                .hour(Integer.parseInt(keyContent[3]))
                .type(keyContent[4])
                .status(Integer.parseInt(keyContent[5]))
                .pv(pv)
                .uv(0)
                .build();
    }

}
