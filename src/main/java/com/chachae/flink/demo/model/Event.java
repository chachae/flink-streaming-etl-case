package com.chachae.flink.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:chenyuexin@shoplineapp.com">chenyuexin</a>
 * @date 2022/6/14 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    private String name;
    private String path;
    private Long ms;

}
