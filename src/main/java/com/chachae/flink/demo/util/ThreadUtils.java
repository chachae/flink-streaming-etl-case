package com.chachae.flink.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * thread utils
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/22 14:57
 */
@Slf4j
public abstract class ThreadUtils {

    public static void sleep(final long time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
