package com.chachae.flink.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/24 17:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WC {

    public String word;
    public int count;

}
