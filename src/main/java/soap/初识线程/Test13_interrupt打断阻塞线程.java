package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/9
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test13_interrupet打断阻塞线程
 */
@Slf4j(topic = "s.Test13_interrupt打断阻塞线程")
public class Test13_interrupt打断阻塞线程 {


    /**
     *   sleep，wait，join 都会让线程进入阻塞状态,此时打断这些阻塞的线程会直接打断
     *   并且调用这些方法后,会重置线程阻塞状态为false
     *   所以打断后 会返回 false
     *
     */


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("进入线程中");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "t1");

        t1.start();

        Thread.sleep(2000);
        /** 打断线程t1 **/
        t1.interrupt();
        log.debug("获取t1的打断标记 -- {}" , t1.isInterrupted());

    }

}
