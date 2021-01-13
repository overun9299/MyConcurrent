package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/13
 */
@Slf4j(topic = "s.Test10_wait配合if和while的区别")
public class Test10_wait配合if和while的区别 {

    /**
     * 建议配合wait使用的是while而不是if
     *      因为线程在wait处等待后,再次醒来也是从等待的地方醒了,如果用if判断条件可能已经失效,如果用while就会在判断一次
     */

    static final Object lock = new Object();
    static Boolean foodStatus = false;
    static Boolean waterStatus = false;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                log.debug("开始干活了");

                if (!foodStatus) {
                    log.debug("没有吃的,我先休息一下");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.debug("有吃的了,我开始干活了");

            }
        }, "t1");


        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("开始干活了");

                while (!foodStatus) {
                    log.debug("没有吃的,我先休息一下");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.debug("有吃的了,我开始干活了");

            }
        }, "t2");


        t1.start();
        t2.start();

        /** 主线程休息1s **/
        Thread.sleep(1000);

        synchronized (lock) {
            /** 进行叫醒服务 **/
            lock.notifyAll();
        }

    }
}
