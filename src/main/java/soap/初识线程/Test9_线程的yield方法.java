package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test9_线程的yield方法
 */
@Slf4j(topic = "s.Test9_线程的yield方法")
public class Test9_线程的yield方法 {

    /**
     * 1. 调用 yield 会让当前线程从 Running 进入 Runnable 就绪状态，然后调度执行其它线程
     * 2. 具体的实现依赖于操作系统的任务调度器
     * 在单核心的机器下，就可以发现线程1打印的数字比线程2多
     *
     * @param args
     * @throws InterruptedException
     */


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            int count = 1;
            while (true) {
                log.debug("count is {}" , count++);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            int count = 1;
            while (true) {
                Thread.yield();
                log.debug("count is {}" , count++);
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
