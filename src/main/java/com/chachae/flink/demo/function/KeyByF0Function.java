package com.chachae.flink.demo.function;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 17:51
 */
public class KeyByF0Function implements KeySelector<Tuple2<String, Integer>, String> {

    @Override
    public String getKey(Tuple2<String, Integer> value) {
        return value.f0;
    }

}
