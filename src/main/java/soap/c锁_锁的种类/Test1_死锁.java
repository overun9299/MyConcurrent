package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/18
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test1_死锁
 */
@Slf4j(topic = "s.Test1_死锁")
public class Test1_死锁 {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                log.debug("lock A");
                synchronized (lockB) {
                    log.debug("lock A - B");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                log.debug("lock B");
                synchronized (lockA) {
                    log.debug("lock B - A");
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
