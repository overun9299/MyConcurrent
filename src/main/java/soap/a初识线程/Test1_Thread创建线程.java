package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/6
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 创建线程
 */
@Slf4j(topic = "s.Test1_Thread创建线程")
public class Test1_Thread创建线程 {

    public static void main(String[] args) {

        /** 创建线程 **/
        Thread thread = new Thread() {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        /** 执行线程 **/
        thread.setName("t1");
        thread.start();

        log.debug("running...");
    }
}
