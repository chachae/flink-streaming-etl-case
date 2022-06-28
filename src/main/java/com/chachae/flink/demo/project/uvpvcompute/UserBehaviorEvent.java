package com.chachae.flink.demo.project.uvpvcompute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/28 14:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorEvent implements Serializable {

    private static final long serialVersionUID = 7143737489555026478L;

    private Integer userId;
    private Integer itemId;
    private String category;
    private String clientIp;
    private String action;
    private Long ts;

}
