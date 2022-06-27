package com.chachae.flink.demo.table;

import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

/**
 * 创建表 #
 * <p>
 * 虚拟表 #
 * 在 SQL 的术语中，Table API 的对象对应于视图（虚拟表）。它封装了一个逻辑查询计划。它可以通过以下方法在 catalog 中创建：
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 19:21
 */
public class CreateTableTest {


    public static void main(String[] args) {

        // get a TableEnvironment
        // see "Create a TableEnvironment" section
        TableEnvironment tableEnv = null;

        // table is the result of a simple projection query
        Table projTable = tableEnv.from("X").select();

        // register the Table projTable as table "projectedTable"
        tableEnv.createTemporaryView("projectedTable", projTable);

    }

}
