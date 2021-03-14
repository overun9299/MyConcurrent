package soap.utils;

import java.util.concurrent.CountDownLatch;

/**
 * 使用CountDownLatch模拟线程并发执行代码
 * 
 * @author gaopeng
 *
 */
public class CountDownLatchConTest {

    // 并发数
    private static final int THREAD_NUM = 100;

    private static volatile CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < THREAD_NUM; i++) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    /** 所有的线程在这里等待 **/
                    try {
                        countDownLatch.await();
                        /** 执行业务逻辑 **/


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });

            thread.start();

            /** 启动后，倒计时计数器减一，代表有一个线程准备就绪了 **/
            countDownLatch.countDown();
        }
    }
}