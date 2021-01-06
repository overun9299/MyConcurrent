package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/6
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test3_Runnable_lambda创建线程
 */
@Slf4j(topic = "s.Test3_Runnable_lambda创建线程")
public class Test3_Runnable_lambda创建线程 {

    public static void main(String[] args) {
        /** 使用lambda创建 **/
        Runnable runnable = () -> {
            log.debug("running");
        };

        Thread t1 = new Thread(runnable , "t1");

        t1.start();

        log.debug("running");
    }
}
