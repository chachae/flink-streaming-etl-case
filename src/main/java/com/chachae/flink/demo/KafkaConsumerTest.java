package com.chachae.flink.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * TODO Description
 *
 * @author <a href="mailto:chenyuexin@shoplineapp.com">chenyuexin</a>
 * @date 2022/6/14 21:56
 */
public class KafkaConsumerTest {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka-joyytest-fs003-core001.duowan.com:8105,kafka-joyytest-fs003-core002.duowan.com:8105");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("group.id","test");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //订阅主题列表topic
        consumer.subscribe(Collections.singletonList("test"));

        for (; ; ) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ZERO);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

    }

}
