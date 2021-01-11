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


    /**
     *          分析Thread和Runnable的关系
     *            -Thread是吧线程和任务合并在一起、Runnable是把线程和任务分开了
     *            -用Runnable更容易的与线程池等高级API配合
     *            -用Runnable让任务类脱离了Thread的继承,更灵活
     *
     */

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
