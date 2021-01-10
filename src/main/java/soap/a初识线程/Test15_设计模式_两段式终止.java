package soap.a初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/9
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test15_设计模式_两段式终止
 */
@Slf4j(topic = "s.Test15_设计模式_两段式终止")
public class Test15_设计模式_两段式终止 {

    /**
     *   两段式终止
     *      java提供了一个停止线程的放stop(),但是该方法比较粗暴,不会给被停止的线程留时间处理一些事情,如释放资源等
     *      所以就需要两段式终止来停止线程,去优雅的停止线程,给被停止的线程时间去处理最后的事情
     */

    public static void main(String[] args) throws InterruptedException {
        TwosTageTermination twosTageTermination = new TwosTageTermination();

        /** 开始执行监控线程 **/
        twosTageTermination.startT();
        /** 主线程睡眠 **/
        Thread.sleep(5000);
        /** 停止监控线程 **/
        twosTageTermination.stopT();

    }
}

@Slf4j(topic = "s.TwosTageTermination")
class TwosTageTermination {

    /** 监控线程 **/
    private Thread monitorThread;
    /** 用于优化日志,不让日志打的太多,和功能无关 **/
    private static Boolean isWork = false;

    /**
     * 启动监控线程
     */
    public void startT() {
        monitorThread = new Thread(()->{
            while (true) {
                /** 为防止cpu空转占用率100%,让线程进行短时间睡眠 **/
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    /** 打断线程 **/
                    Thread.currentThread().interrupt();
                }
                /** 判断是否打断 **/
                if (Thread.currentThread().isInterrupted()) {
                    /** 线程被打断了,开始料理后事 **/
                    log.debug("料理后事... {}" , System.currentTimeMillis());
                    break;
                } else {
                    if (!isWork) {
                        log.debug("监控线程开始执行正常业务逻辑 -- {}" , System.currentTimeMillis());
                        isWork = !isWork;
                    }
                }
            }
            log.debug("监控线程已关闭 -- {}" , System.currentTimeMillis());
        },"monitorThread");
        monitorThread.start();
    }

    /**
     * 优雅的停止线程
     */
    public void stopT() {
        /** 调用打断 **/
        monitorThread.interrupt();
    }

}
