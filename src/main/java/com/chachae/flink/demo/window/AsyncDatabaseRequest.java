package com.chachae.flink.demo.window;

import com.chachae.flink.demo.client.MockDatabaseClient;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * <a href="https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/operators/asyncio/">async io</a>
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:31
 */
public class AsyncDatabaseRequest extends RichAsyncFunction<String, Tuple2<String, String>> {

    private MockDatabaseClient client;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.client = new MockDatabaseClient("127.0.0.1", "123456");
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(this.client)) {
            this.client.close();
        }
    }

    @Override
    public void asyncInvoke(String key, ResultFuture<Tuple2<String, String>> resultFuture) throws Exception {
        // 发送异步请求，接收 future 结果
        Future<String> result = client.query(key);

        // 设置客户端完成请求后要执行的回调函数
        // 回调函数只是简单地把结果发给 future
        CompletableFuture.supplyAsync(new Supplier<String>() {

            @Override
            public String get() {
                try {
                    return result.get();
                } catch (InterruptedException | ExecutionException e) {
                    // 显示地处理异常。
                    return null;
                }
            }
        }).thenAccept((String dbResult) -> {
            resultFuture.complete(Collections.singleton(new Tuple2<>(key, dbResult)));
        });
    }

    @Override
    public void timeout(String input, ResultFuture<Tuple2<String, String>> resultFuture) throws Exception {
        super.timeout(input, resultFuture);
    }

}
