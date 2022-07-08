package com.chachae.flink.demo.aliyunlog;

import cn.hutool.core.collection.CollUtil;
import com.aliyun.openservices.log.flink.data.RawLogGroup;
import com.aliyun.openservices.log.flink.data.RawLogGroupList;
import org.apache.flink.api.common.functions.FilterFunction;

import java.util.List;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/7/8 14:26
 */
public class RawLogGroupNullFilter implements FilterFunction<RawLogGroupList> {

    @Override
    public boolean filter(RawLogGroupList value) {
        List<RawLogGroup> rawLogGroupList = value.getRawLogGroups();
        return CollUtil.isNotEmpty(rawLogGroupList)
                && CollUtil.isNotEmpty(CollUtil.getFirst(rawLogGroupList).getLogs());
    }

}
