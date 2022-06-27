package com.chachae.flink.demo.source;

import com.chachae.flink.demo.model.Event;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.concurrent.TimeUnit;

/**
 * TODO Description
 *
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/14 18:38
 */
public class ClickSource implements SourceFunction<Event> {

    private final long pendingMs;

    private volatile boolean cancel = false;

    public ClickSource() {
        this.pendingMs = 1000;
    }

    public ClickSource(long pendingMs) {
        this.pendingMs = pendingMs;
    }

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {
        while (!cancel) {
            sourceContext.collect(new Event("click", "/admin", System.currentTimeMillis()));
            TimeUnit.MILLISECONDS.sleep(pendingMs);
        }
    }

    @Override
    public void cancel() {
        cancel = true;
    }
}