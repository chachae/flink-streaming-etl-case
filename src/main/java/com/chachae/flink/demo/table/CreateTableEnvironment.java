package com.chachae.flink.demo.table;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 19:13
 */
public class CreateTableEnvironment {

    public static void main(String[] args) {

        // way1
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                //.inBatchMode()
                .build();
        TableEnvironment env1 = TableEnvironment.create(settings);

        // way2
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment env2 = StreamTableEnvironment.create(env);

    }

}
