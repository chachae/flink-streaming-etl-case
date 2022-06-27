package com.chachae.flink.demo.operator;

/**
 * 根据指定的 key 和窗口 join 两个数据流。
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 17:55
 */
public class JoinTest {

    /**
     * Window Join #
     * DataStream,DataStream → DataStream #
     * 根据指定的 key 和窗口 join 两个数据流。
     *
     * dataStream.join(otherStream)
     *     .where(<key selector>).equalTo(<key selector>)
     *     .window(TumblingEventTimeWindows.of(Time.seconds(3)))
     *     .apply (new JoinFunction () {...});
     *
     *
     *
     *     Interval Join #
     * KeyedStream,KeyedStream → DataStream #
     * 根据 key 相等并且满足指定的时间范围内（e1.timestamp + lowerBound <= e2.timestamp <= e1.timestamp + upperBound）的条件将分别属于两个 keyed stream 的元素 e1 和 e2 Join 在一起。
     // this will join the two streams so that
     // key1 == key2 && leftTs - 2 < rightTs < leftTs + 2
     keyedStream.intervalJoin(otherKeyedStream)
     .between(Time.milliseconds(-2), Time.milliseconds(2)) // lower and upper bound
     .upperBoundExclusive(true) // optional
     .lowerBoundExclusive(true) // optional
     .process(new IntervalJoinFunction() {...});
     */

}
