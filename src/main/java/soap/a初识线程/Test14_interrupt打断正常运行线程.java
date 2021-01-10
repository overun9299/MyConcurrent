package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/9
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test14_interrupt打断正常运行线程
 */
@Slf4j(topic = "s.Test14_interrupt打断正常运行线程")
public class Test14_interrupt打断正常运行线程 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("进入t1线程时刻 -- {}" , System.currentTimeMillis());
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    /** 在睡眠中有可能会被打断,而sleep会重置打断标记所以在catch中要重新打断线程 **/
                    Thread.currentThread().interrupt();
                }
                if (Thread.currentThread().isInterrupted()) {
                    log.debug("被打断了,不执行了...");
                    break;
                }
            }
            log.debug("t1线程结束时刻 -- {}" , System.currentTimeMillis());
        }, "t1");

        t1.start();

        Thread.sleep(2000);
        /** 打断t1线程 **/
        t1.interrupt();

    }
}
