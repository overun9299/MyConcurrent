package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/10
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test1_多线程共享资源问题
 */
@Slf4j(topic = "s.Test1_多线程共享资源问题")
public class Test1_多线程共享资源问题 {

    /**
     *   有两个线程对一个共享资源进行操作,一个+5000次,一个-5000次,在没有做线程安全的情况下会出现什么问题
     *      可以看到,在没有做线程安全的情况下,最终输出不是0
     */

    /** 共享资源 **/
    static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        /** +5000次 **/
        Thread t1 = new Thread(() -> {
            for (int i = 0;i<5000;i++) {
                count++;
            }
        }, "t1");

        /** -5000次 **/
        Thread t2 = new Thread(() -> {
            for (int i = 0;i<5000;i++) {
                count--;
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
