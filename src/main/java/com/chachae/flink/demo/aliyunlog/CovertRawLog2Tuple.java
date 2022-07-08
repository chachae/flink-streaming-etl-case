package com.chachae.flink.demo.aliyunlog;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.log.flink.data.RawLog;
import com.aliyun.openservices.log.flink.data.RawLogGroup;
import com.aliyun.openservices.log.flink.data.RawLogGroupList;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:chachae@foxmail.com">chenyuexin</a>
 * @date 2022/7/8 14:33
 */
@Slf4j
public class CovertRawLog2Tuple implements FlatMapFunction<RawLogGroupList, Tuple3<String, Integer, Integer>> {

    /**
     * content example /eclytics/c?cmp=1&enc=b64&_pid=1657262391002_4a9709f740b547a591f8ec1c851dd39e
     * &_act=webslcodetrack_pageView1657262391770_0ee4b412bbc94fc6890da73deaddc0b2,view1657262391829_cae0e631b98f4d62a1de96771b7263da
     * :websdkprotocol_86000101,85000101&_sid=c0000e69-a70f-4073-a4c0-3f3fa2a43e5f&_sct=1657262318750
     * &_tid=dd55afa6-941d-4755-9587-edaa7a404d37&_pdppv=1&_bcount=4
     *
     * @param value The input value.
     * @param out   The collector for returning result values.
     */
    @Override
    public void flatMap(RawLogGroupList value, Collector<Tuple3<String, Integer, Integer>> out) {
        List<RawLogGroup> rawLogGroupList = value.getRawLogGroups();

        for (RawLogGroup group : rawLogGroupList) {
            List<RawLog> logList = group.getLogs();

            for (RawLog rawLog : logList) {
                this.parseLogCollector(rawLog, out);
            }

        }
    }

    private void parseLogCollector(RawLog rawLog, Collector<Tuple3<String, Integer, Integer>> out) {
        Map<String, String> contentMap = rawLog.getContents();
        int time = rawLog.getTime();

        LocalDateTime localDateTime = LocalDateTimeUtil.of(time);
        int hour = localDateTime.getHour();
        String dt = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String actBatchContent = MapUtil.getStr(contentMap, "_act");
        Integer status = MapUtil.getInt(contentMap, "status");
        String ingressClass = MapUtil.getStr(contentMap, "http_x_ingress_class");

        if (StrUtil.isBlank(actBatchContent)) {
            return;
        }

        String[] actArray = actBatchContent.split(StrPool.COLON);
        for (String actContent : actArray) {
            String[] actAndEventIdArray = actContent.split(StrPool.UNDERLINE);
            String act = actAndEventIdArray[0];
            String eventIdStr = actAndEventIdArray[1];

            String[] eventIdArray = eventIdStr.split(StrPool.COMMA);

            for (String eventId : eventIdArray) {
                String key = String.format("%s-%s-%s-%s-%s-%s", act, eventId, dt, hour, ingressClass, status);
                // collect

                Tuple3<String, Integer, Integer> tp = Tuple3.of(key, 1, time);

                log.info("tuple3:{}", tp);

                out.collect(tp);
            }

        }

    }

}
