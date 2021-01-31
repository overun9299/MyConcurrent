package soap.d内存模型;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/31
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test2_使用volatile解决上述问题
 */
@Slf4j(topic = "s.Test2_使用volatile解决上述问题")
public class Test2_使用volatile解决上述问题 {

    /**
     * 添加volatile后变量由更改,就会进行通知, 称作可见性
     *
     * 注意:volatile适用于一个线程修改,另一个线程读取的情况, volatile只解决了可见性没解决原子性.
     *
     * 因为volatile只能保证看到最新值,不能保证多线的指令交错问题
     */

    static volatile boolean isDown = false;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            while (!isDown) {

            }
            log.info("t1 运行结束");
        }, "t1");

        t1.start();

        Thread.sleep(1000);

        isDown = true;
        log.info("主线程更改标识,试图打断t1");
    }

}
