package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangPY on 2021/1/20
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test3_ReentrantLock的可打断性质
 */
@Slf4j(topic = "s.Test3_ReentrantLock的可打断性质")
public class Test3_ReentrantLock的可打断性质 {

    /**
     * 在synchronized中如果一个线程没有争抢到锁,那么他会一直等待下去
     *
     * 在ReentrantLock可以打断线程对锁的等待  reentrantLock.lockInterruptibly()
     **/

    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            try {
                log.debug("尝试获取锁");
                reentrantLock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.error("获取锁失败");
                return;
            }
            try {
                log.debug("获取锁成功,执行代码逻辑");
            } finally {
                reentrantLock.unlock();
            }
        }, "t1");

        reentrantLock.lock();
        t1.start();
        Thread.sleep(1000);
        /** 打断t1的等待 **/
        t1.interrupt();
    }
}
