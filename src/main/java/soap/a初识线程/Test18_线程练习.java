package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/10
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test18_线程练习
 */
@Slf4j(topic = "s.Test18_线程练习")
public class Test18_线程练习 {

    /**
     *   比如，想泡壶茶喝。当时的情况是：开水没有；水壶要洗，茶壶、茶杯要洗；火已生了，茶叶也有了。怎么办?
     *      -办法甲：洗好水壶(1s)，灌上凉水，放在火上；在等待水开的时间里(15)，洗茶壶(1s)、洗茶杯(2s)、拿茶叶(1s)；等水开了，泡茶喝。(两个线程总共16s)
     */


    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            /** t1线程用来 洗好水壶(1s) 和烧开水 **/
            log.debug("t1线程准备洗水壶 , {}" , System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1线程洗水壶完毕,并开始烧水 , {}" , System.currentTimeMillis());
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1线程烧水完毕!!! , {}" , System.currentTimeMillis());

        }, "t1");

        t1.start();
        /** 主线程 **/

        log.debug("主线程开始洗茶壶(1s) , {}" , System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("主线程开始洗茶杯(2s) , {}" , System.currentTimeMillis());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("主线程开始拿茶叶(1s) , {}" , System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("等待水开 {}" , System.currentTimeMillis());
        t1.join();

        log.debug("泡茶完毕!!! {}" , System.currentTimeMillis());

    }
}
