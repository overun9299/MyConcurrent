package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangPY on 2021/1/20
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test4_ReentrantLock的锁超时
 */
@Slf4j(topic = "s.Test4_ReentrantLock的锁超时")
public class Test4_ReentrantLock的锁超时 {

    /**
     * 在ReentrantLock可以打断线程对锁的等待,但是这是被动的
     *
     * 使用tryLock可以设置主动超时时间
     **/

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {

            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("等待1s后获取锁失败");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log.debug("获取锁成功,执行业务代码");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        t1.start();
    }
}
