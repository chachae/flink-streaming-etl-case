package com.chachae.flink.demo.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Future;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockDatabaseClient implements AutoCloseable {

    private String host;
    private String password;

    @Override
    public void close() {
        System.out.println("client [" + this.hashCode() + "] client closed.");
    }

    public Future<String> query(String key) {
        return null;
    }

}
