package com.chachae.flink.demo;

import com.chachae.flink.demo.model.Event;
import com.chachae.flink.demo.source.ClickSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO Description
 *
 * @author <a href="mailto:chenyuexin@shoplineapp.com">chenyuexin</a>
 * @date 2022/6/14 18:41
 */
public class BroadcastTest {

    public static void main(String[] args) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 读取数据源，并行度为 1
        DataStreamSource<Event> stream = env.addSource(new ClickSource());
        // 经广播后打印输出，并行度为 4
        stream.broadcast().print("broadcast").setParallelism(4);
        env.execute();
    }

}