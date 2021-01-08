package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test12_线程的join方法
 */
@Slf4j(topic = "s.Test12_线程的join方法")
public class Test12_线程的join方法 {


    /**
     *  该例子在线程2中对全局静态属性进行赋值,但是主线程无法拿到线程2的赋值,原因是主线程较快
     *  解决办法: 1.使用在主线程中调用join方法
     *           2.使用callable
     *
     *  join的另外一种解释是,将多个线程最终合并为一个线程
     */

    static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        method();
    }

    public static void method() throws InterruptedException {
        log.debug("进入方法");
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count = 10;
            log.debug("线程1方法执行完毕");
        }, "t1");

        t1.start();
        /** 调用join等待线程t1执行完毕**/
        t1.join();

        log.debug("count -> {}" , count);

        log.debug("结束");
    }
}
