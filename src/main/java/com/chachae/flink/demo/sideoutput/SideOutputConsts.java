package com.chachae.flink.demo.sideoutput;

import org.apache.flink.util.OutputTag;

/**
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:45
 */
public interface SideOutputConsts {

    OutputTag<String> SIDE_OUTPUT_TAG = new OutputTag<String>("side-output") {
    };

}
