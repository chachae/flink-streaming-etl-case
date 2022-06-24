package com.chachae.flink.demo;

import com.chachae.flink.demo.model.Event;
import com.chachae.flink.demo.source.ClickSource;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO Description
 *
 * @author <a href="mailto:chenyuexin@shoplineapp.com">chenyuexin</a>
 * @date 2022/6/14 21:43
 */
public class SinkToMysqlTest {

    public static void main(String[] args) throws Exception {

        LocalStreamEnvironment localEnvironment = StreamExecutionEnvironment.createLocalEnvironment();

        localEnvironment
                .addSource(new ClickSource(10))
                .addSink(JdbcSink.sink(
                        "insert into flink_test.click values (?,?)"
                        , (JdbcStatementBuilder<Event>) (preparedStatement, s) -> {
                            preparedStatement.setString(1, s.getPath());
                            preparedStatement.setString(2, s.getPath());
                            preparedStatement.execute();
                        },
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withDriverName("com.cj.mysql.jdbc.driver")
                                .withUrl("jdbc:mysql://127.0.0.1:3306/flink_test")
                                .withUsername("root")
                                .withPassword("123456")
                                .build()
                ));


        localEnvironment.execute();

    }

}
