package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/6
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 创建线程
 */
@Slf4j(topic = "s.Test2_Runnable创建线程")
public class Test2_Runnable创建线程 {

    public static void main(String[] args) {

        /** 使用runnable 这样更灵活推荐使用 **/
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        Thread t1 = new Thread(runnable, "t1");

        t1.start();

        log.debug("running...");
    }
}
