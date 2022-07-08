package com.chachae.flink.demo.aliyunlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/7/8 14:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventInfoFht {

    private String act;

    private String eventId;

    private String dt;

    private Integer hour;

    private String type;

    private Integer status;

    private Integer pv;

    private Integer uv;

}
