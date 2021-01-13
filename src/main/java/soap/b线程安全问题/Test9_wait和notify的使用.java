package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/13
 */
@Slf4j(topic = "s.Test9_wait和notify的使用")
public class Test9_wait和notify的使用 {

    /**
     * wait使线程进行等待,并释放锁,notify用于叫醒一个,一般不推荐使用.notifyAll用于唤醒所有线程
     * 注意:wait和notify都要写在synchronized中
     *
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
                if (foodStatus) {
                    log.debug("有吃的了,我开始干活了");
                } else {
                    log.debug("还是没有吃的,我先休息一下");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("开始干活了");

                if (!waterStatus) {
                    log.debug("没有水喝,我先休息一下");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (waterStatus) {
                    log.debug("有水喝了,我开始干活了");
                } else {
                    log.debug("还是没有水喝,我先休息一下");
                }
            }
        }, "t2");

        t1.start();
        t2.start();

        /** 主线程休息1s **/
        Thread.sleep(1000);

        synchronized (lock) {
            /** 开始送水 **/
            waterStatus = true;
            /** 进行叫醒服务 **/
            lock.notifyAll();
        }
    }
}
