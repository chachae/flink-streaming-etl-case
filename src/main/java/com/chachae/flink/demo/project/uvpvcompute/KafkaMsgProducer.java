package com.chachae.flink.demo.project.uvpvcompute;

import com.alibaba.fastjson.JSON;
import com.chachae.flink.demo.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/28 14:48
 */
@Slf4j
public class KafkaMsgProducer {

    public static void main(String[] args) {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        String topicName = parameterTool.get(Consts.KAFKA_TOPIC_KEY);
        String servers = parameterTool.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);

        if (StringUtils.isBlank(topicName) || StringUtils.isBlank(servers)) {
            throw new IllegalArgumentException("kafka brokers or topic name cannot be empty.");
        }

        System.out.printf("kafka info print\nbrokers: %s\ntopic: %s\n", servers, topicName);

        Map<String, Object> kafkaParam = new HashMap<>(16);
        kafkaParam.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        kafkaParam.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaParam.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaParam)) {
            for (; ; ) {
                String msg = genMessage();
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, msg);
                producer.send(record);
                ThreadUtils.sleep(1, TimeUnit.SECONDS);
            }
        }

    }

    public static String genMessage() {
        String[] action = {"click", "bug", "login", "logout"};
        String[] category = {"c1", "c2", "c3", "c4"};
        UserBehaviorEvent userBehaviorEvent = new UserBehaviorEvent();
        userBehaviorEvent.setUserId(new Random().nextInt(10000));
        userBehaviorEvent.setItemId(new Random().nextInt(10000));
        userBehaviorEvent.setCategory(category[new Random().nextInt(category.length)]);
        userBehaviorEvent.setAction(action[new Random().nextInt(action.length)]);
        userBehaviorEvent.setTs(System.currentTimeMillis());
        String msgStr = JSON.toJSONString(userBehaviorEvent);
        System.out.println(msgStr);
        return msgStr;
    }

}
