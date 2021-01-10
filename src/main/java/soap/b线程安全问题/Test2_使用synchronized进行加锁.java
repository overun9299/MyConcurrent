package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/10
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test2_使用synchronized进行加锁
 */
@Slf4j(topic = "s.Test2_使用synchronized进行加锁")
public class Test2_使用synchronized进行加锁 {

    /**
     *
     * 现在讨论使用synchronized来进行解决，即俗称的对象锁，它采用互斥的方式让同一时刻至多只有一个线程持有对象锁，其
     * 他线程如果想获取这个锁就会阻塞住，这样就能保证拥有锁的线程可以安全的执行临界区内的代码，不用担心线程上下文切换
     */

    /** 共享资源 **/
    static int count = 0;
    /** 创建加锁对象 **/
    static Object lockObject = new Object();

    public static void main(String[] args) throws InterruptedException {

        /** +5000次 **/
        Thread t1 = new Thread(() -> {

            for (int i = 0;i<5000;i++) {

                /** 注意、加锁的粒度一定要细,不然多线程基本没啥用 **/
                synchronized (lockObject) {
                    count++;
                    log.debug("t1操作了 -- 1");
                }
            }

        }, "t1");

        /** -5000次 **/
        Thread t2 = new Thread(() -> {

            for (int i = 0;i<5000;i++) {
                synchronized (lockObject) {
                    count--;
                    log.debug("t2操作了 -- 2");
                }
            }

        }, "t2");

        t1.start();

        t2.start();

        /** 等待两个线程执行完毕 **/
        t1.join();
        t2.join();

        /** 最终结果 **/
        log.debug("count的最终结果为  {}" , count);
    }
}
