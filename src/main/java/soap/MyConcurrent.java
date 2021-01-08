package soap;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/6
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 并发
 */
@Slf4j(topic = "s.MyConcurrent")
public class MyConcurrent {

    public static void main(String[] args) {
        new Thread(()->{
            while (true) {
                log.debug("running...");
            }
        } , "t1").start();

        new Thread(()->{
            while (true) {
                log.debug("running...");
            }
        } , "t2").start();
    }
}
