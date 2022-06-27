package com.chachae.flink.demo.table;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 19:27
 */
public class OutputTableTest {

    public static void main(String[] args) {

        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // create an output Table
        final Schema schema = Schema.newBuilder()
                .column("a", DataTypes.INT())
                .column("b", DataTypes.STRING())
                .column("c", DataTypes.BIGINT())
                .build();

        tableEnv.createTemporaryTable("CsvSinkTable", TableDescriptor.forConnector("filesystem")
                .schema(schema)
                .option("path", "/path/to/file")
                .format(FormatDescriptor.forFormat("csv")
                        .option("field-delimiter", "|")
                        .build())
                .build());

        // compute a result Table using Table API operators and/or SQL queries
        Table result = null;

        // Prepare the insert into pipeline
        TablePipeline pipeline = result.insertInto("CsvSinkTable");

        // Print explain details
        pipeline.printExplain();

        // emit the result Table to the registered TableSink
        pipeline.execute();


    }

}
