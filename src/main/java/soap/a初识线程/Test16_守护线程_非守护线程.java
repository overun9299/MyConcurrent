package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/10
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test16_守护线程_非守护线程
 */
@Slf4j(topic = "s.Test16_守护线程_非守护线程")
public class Test16_守护线程_非守护线程 {

    /**
     *              Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守
     *              护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。
     *
     */

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("t1线程执行完毕");
        }, "t1");

        /** 设置守护线程 **/
        t1.setDaemon(true);

        t1.start();

        Thread.sleep(2000);
        log.debug("主线程执行完毕");
    }

}
