package com.chachae.flink.demo.window;

/**
 * <pre>
 *     <a href="https://nightlies.apache.org/flink/flink-docs-release-1.15/zh/docs/dev/datastream/operators/windows/">
 *         about flink windows</a>
 *     <code>
 * ------Keyed Windows------
 *
 *         stream
 *        .keyBy(...)               <-  仅 keyed 窗口需要
 *        .window(...)              <-  必填项："assigner"
 *       [.trigger(...)]            <-  可选项："trigger" (省略则使用默认 trigger)
 *       [.evictor(...)]            <-  可选项："evictor" (省略则不使用 evictor)
 *       [.allowedLateness(...)]    <-  可选项："lateness" (省略则为 0)
 *       [.sideOutputLateData(...)] <-  可选项："output tag" (省略则不对迟到数据使用 side output)
 *        .reduce/aggregate/apply()      <-  必填项："function"
 *       [.getSideOutput(...)]      <-  可选项："output tag"
 *
 * ------Non-Keyed Windows------
 *
 *       stream
 *        .windowAll(...)           <-  必填项："assigner"
 *       [.trigger(...)]            <-  可选项："trigger" (else default trigger)
 *       [.evictor(...)]            <-  可选项："evictor" (else no evictor)
 *       [.allowedLateness(...)]    <-  可选项："lateness" (else zero)
 *       [.sideOutputLateData(...)] <-  可选项："output tag" (else no side output for late data)
 *        .reduce/aggregate/apply()      <-  必填项："function"
 *       [.getSideOutput(...)]      <-  可选项："output tag"
 *
 *       上面方括号（[…]）中的命令是可选的。也就是说，Flink 允许你自定义多样化的窗口操作来满足你的需求。
 *     </code>
 * </pre>
 *
 * @author <a href="chachae@foxmail.com">chenyuexin</a>
 * @date 2022/6/27 18:04
 */
public class AboutKeyedAndNonKeyedWindows {
}
