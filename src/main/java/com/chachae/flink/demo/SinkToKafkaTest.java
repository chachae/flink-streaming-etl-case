package com.chachae.flink.demo;

import com.alibaba.fastjson.JSON;
import com.chachae.flink.demo.model.Event;
import com.chachae.flink.demo.source.ClickSource;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * TODO Description
 *
 * @author <a href="mailto:chenyuexin@shoplineapp.com">chenyuexin</a>
 * @date 2022/6/14 21:43
 */
public class SinkToKafkaTest {

    public static void main(String[] args) throws Exception {

        LocalStreamEnvironment localEnvironment = StreamExecutionEnvironment.createLocalEnvironment();

        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka-joyytest-fs003-core001.duowan.com:8105,kafka-joyytest-fs003-core002.duowan.com:8105");

        localEnvironment
                .addSource(new ClickSource(10))
                .flatMap(new FlatMapFunction<Event, String>() {
                    @Override
                    public void flatMap(Event event, Collector<String> collector) throws Exception {
                        collector.collect(JSON.toJSONString(event));
                    }
                })
                .addSink(new FlinkKafkaProducer<>(
                        "test"
                        , new SimpleStringSchema()
                        , props
                ));


        localEnvironment.execute();

    }

}
