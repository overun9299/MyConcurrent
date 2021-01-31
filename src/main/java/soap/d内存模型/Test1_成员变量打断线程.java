package soap.d内存模型;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/31
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test_成员变量打断线程
 */
@Slf4j(topic = "s.Test_成员变量打断线程")
public class Test1_成员变量打断线程 {

    /**
     * 主线程更改isDown时候,t1线程是无法感知的. 因为JMM中内存会拷贝一份到自己线程中
     */

    static boolean isDown = false;

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
