package com.chachae.flink.demo.model;

import lombok.Data;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:22
 */
@Data
public class SensorReading {

    private long time;
    private double value;

}
