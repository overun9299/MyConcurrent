package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/7
 */
@Slf4j(topic = "s.Test5_观察多个线程同时执行")
public class Test5_观察多个线程同时执行 {

    public static void main(String[] args) {

       new Thread(()->{
          while (true) {
              log.debug("running...");
          }
       }, "t1").start();

        new Thread(()->{
            while (true) {
                log.debug("running...");
            }
        }, "t2").start();
    }
}
