package com.chachae.flink.demo.table;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 19:24
 */
public class QueryTableByAPITest {

    public static void main(String[] args) {

        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // register Orders table

        // scan registered Orders table
        Table orders = tableEnv.from("Orders");
        // compute revenue for all customers from France
        Table revenue = orders
                .filter($("cCountry").isEqual("FRANCE"))
                .groupBy($("cID"), $("cName"))
                .select($("cID"), $("cName"), $("revenue").sum().as("revSum"));

        // emit or convert Table
        // execute query

    }

}
