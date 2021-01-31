package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by ZhangPY on 2021/1/28
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test7_设计模式_固定运行顺序_park_unpark
 */
@Slf4j(topic = "s.Test7_设计模式_固定运行顺序_park_unpark")
public class Test7_设计模式_固定运行顺序_park_unpark {


    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            /** 暂停当前线程 **/
            LockSupport.park();
            log.info("t1执行完毕");

        }, "t1");

        Thread t2 = new Thread(() -> {
            log.info("t2执行完毕");
            /** 唤醒t1线程 **/
            LockSupport.unpark(t1);

        }, "t2");

        t1.start();
        t2.start();
    }

}
