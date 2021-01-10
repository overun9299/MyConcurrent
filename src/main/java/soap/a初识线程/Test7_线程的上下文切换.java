package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 线程的上下文切换
 */
@Slf4j(topic = "s.Test7_线程的上下文切换")
public class Test7_线程的上下文切换 {


    /**
     *    线程上下文切换（Thread Context Switch）
     *    因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码
     *      -线程的 cpu 时间片用完
     *      -垃圾回收
     *      -有更高优先级的线程需要运行
     *      -线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法
     *
     *    当线程发生上下文切换的时候，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，java中对应的概念就是 -程序计数器-
     *    它的作用就是记住下一条jvm指令的执行地址，是线程私有的
     *
     *    注意：线程的上下文切换是非常耗费资源的，所以当硬件为单核是多线程效率不一定高
     *
     */
}
