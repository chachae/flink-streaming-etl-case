package com.chachae.flink.demo.table;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 19:22
 */
public class ConnectorTablesTest {

    public static void main(String[] args) {

        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // Using table descriptors
        final TableDescriptor sourceDescriptor = TableDescriptor.forConnector("datagen")
                .schema(Schema.newBuilder()
                        .column("f0", DataTypes.STRING())
                        .build())
                .build();

        tableEnv.createTable("SourceTableA", sourceDescriptor);
        tableEnv.createTemporaryTable("SourceTableB", sourceDescriptor);

        // Using sql ddl
        tableEnv.executeSql("CREATE [TEMPORARY] TABLE MyTable (...) WITH (...)");

    }

}
