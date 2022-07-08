package com.chachae.flink.demo.aliyunlog;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.log.flink.ConfigConstants;
import com.aliyun.openservices.log.flink.FlinkLogConsumer;
import com.aliyun.openservices.log.flink.data.RawLogGroupList;
import com.aliyun.openservices.log.flink.data.RawLogGroupListDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.IOUtils;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/7/8 13:51
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        InputStream inputStream = Main.class.getResourceAsStream("/aliyun-log-config-test.properties");
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(inputStream);

        IOUtils.closeStream(inputStream);

        // env
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        log.info("config: {}", JSON.toJSONString(parameterTool.getProperties()));

        // config
        Properties configProps = new Properties();
        configProps.put(ConfigConstants.LOG_ENDPOINT, parameterTool.get("ap.southeast.aliyun.host"));
        configProps.put(ConfigConstants.LOG_ACCESSKEYID, parameterTool.get("ap.southeast.aliyun.access.id"));
        configProps.put(ConfigConstants.LOG_ACCESSKEY, parameterTool.get("ap.southeast.aliyun.access.key"));
        configProps.put(ConfigConstants.LOG_PROJECT, parameterTool.get("ap.southeast.aliyun.project"));
        configProps.put(ConfigConstants.LOG_LOGSTORE, parameterTool.get("ap.southeast.aliyun.log.store"));

        // aliyun log source
        RawLogGroupListDeserializer deserializer = new RawLogGroupListDeserializer();
        FlinkLogConsumer<RawLogGroupList> flinkLogConsumer = new FlinkLogConsumer<>(
                parameterTool.get("ap.southeast.aliyun.project")
                , parameterTool.get("ap.southeast.aliyun.log.store")
                , deserializer
                , configProps
        );

        // add source
        DataStreamSource<RawLogGroupList> rawLogGroupListDataStreamSource = env.addSource(flinkLogConsumer);

        // 过滤
        SingleOutputStreamOperator<RawLogGroupList> filterDs = rawLogGroupListDataStreamSource
                .filter(new RawLogGroupNullFilter());

        // 打平
        SingleOutputStreamOperator<Tuple3<String, Integer, Integer>> tupleStream = filterDs
                .flatMap(new CovertRawLog2Tuple());

        // watermark
        final SingleOutputStreamOperator<Tuple3<String, Integer, Integer>> watermarksStream = tupleStream.
                assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, Integer, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner((event, timestamp) -> (event.f2 * 1000L)));

        // 分组
        KeyedStream<Tuple3<String, Integer, Integer>, String> keyedStream = watermarksStream
                .keyBy(t -> t.f0);

        // 统计
        SingleOutputStreamOperator<Tuple2<String, Integer>> sumStream = keyedStream
                .flatMap(new SumWindowAggregateFunction());

        // windows
        SingleOutputStreamOperator<Tuple2<String, Integer>> windowsStream = sumStream
                .keyBy(data -> data.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(60)))
                .max(1);

        // 转换
        SingleOutputStreamOperator<EventInfoFht> resultStream = windowsStream
                .map(new Tuple2EventInfoFht());

        // 打印
        resultStream.print();

        env.execute();

    }

}
