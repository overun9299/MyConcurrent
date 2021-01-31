package soap.c锁_锁的种类;

import jdk.nashorn.internal.ir.Block;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/28
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test6_设计模式_固定运行顺序_wait_notity模式
 */
@Slf4j(topic = "s.Test6_设计模式_固定运行顺序_wait_notify模式")
public class Test6_设计模式_固定运行顺序_wait_notify模式 {


    /**
     *
     * 要求 t2先执行完
     *
     */

    private static final Object lock = new Object();
    private static Boolean isDown = false;

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!isDown) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
                log.info("t1执行完毕");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.info("t2执行完毕");
                isDown = true;
                lock.notifyAll();
            }
        }, "t2");

        t1.start();
        t2.start();

    }

}
