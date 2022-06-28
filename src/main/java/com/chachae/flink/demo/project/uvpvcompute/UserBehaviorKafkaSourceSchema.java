package com.chachae.flink.demo.project.uvpvcompute;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/28 16:12
 */
public class UserBehaviorKafkaSourceSchema implements DeserializationSchema<UserBehaviorEvent> {

    private static final long serialVersionUID = 1977653458434987582L;

    @Override
    public TypeInformation<UserBehaviorEvent> getProducedType() {
        return TypeInformation.of(UserBehaviorEvent.class);
    }

    @Override
    public UserBehaviorEvent deserialize(byte[] message) throws IOException {
        return JSON.parseObject(message, UserBehaviorEvent.class);
    }

    @Override
    public boolean isEndOfStream(UserBehaviorEvent nextElement) {
        return false;
    }
}
