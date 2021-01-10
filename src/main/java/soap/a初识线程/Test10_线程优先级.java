package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test10_线程优先级
 */
@Slf4j(topic = "s.Test10_线程优先级")
public class Test10_线程优先级 {


    /**
     *
     *  线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
     *  如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用
     *
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
                log.debug("count is {}" , count++);
            }
        }, "t2");

        /** 设置线程优先级 **/
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
    }
}
