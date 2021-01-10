package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test8_线程的sleep和线程的状态
 */
@Slf4j(topic = "s.Test8_线程的sleep和线程的状态")
public class Test8_线程的sleep和线程的状态 {


    /**
     * 1. 调用 sleep 会让当前线程从 Running 进入 Timed Waiting 状态（阻塞）
     * 2. 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException
     * 3. 睡眠结束后的线程未必会立刻得到执行
     * 4. 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性
     *
     */


    /**
     * 演示线程在睡眠中被打断
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(()->{
            try {
                Thread.sleep(2000);
                log.debug("sleep good");
            } catch (InterruptedException e) {
                log.debug("thread is Interrupted");
            }
        },"t1");
        /** 启动线程 **/
        t1.start();
        /** 打印t1状态 **/
        log.debug("t1 state  {}" , t1.getState());
        /** 主线程睡眠 **/
        Thread.sleep(500);
        /** 打断t1 **/
        t1.interrupt();
        /** 打印t1状态 **/
        log.debug("t1 state  {}" , t1.getState());

    }
}
