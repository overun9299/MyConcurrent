package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;
import soap.utils.Downloader;

import java.util.List;

/**
 * Created by ZhangPY on 2021/1/13
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test11_设计模式_保护性暂停
 */
@Slf4j(topic = "s.Test11_设计模式_保护性暂停")
public class Test11_设计模式_保护性暂停 {

    public static void main(String[] args) throws InterruptedException {
        GuardedObject guardedObject = new GuardedObject();
        Thread t1 = new Thread(() -> {
            List<String> download = Downloader.download();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guardedObject.setResponse(download);
        },"t1");

        Thread t2 = new Thread(() -> {
            List<String> response = (List<String>)guardedObject.getResponse(10000);
            if (response != null) {
                log.debug("获取到的数据为 {}" , response.size());
            } else {
                log.debug("没有获取到");
            }
        },"t2");

        Thread t3 = new Thread(() -> {
            guardedObject.setResponse(null);
        },"t3");

        t1.start();
        t2.start();
        Thread.sleep(1000);
        t3.start();
    }


}

@Slf4j(topic = "s.GuardedObject")
class GuardedObject {
    /** 返回结果 **/
    private Object response;

    /**
     * 获取返回结果
     * @return
     */
    public Object getResponse (){
        synchronized (this) {
            while (response == null) {
                try {
                    log.debug("没有返回参数,开始wait");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    /**
     * 获取返回结果,带超时时间
     * @param timeout 超时时间
     * @return
     */
    public Object getResponse (long timeout){
        synchronized (this) {
            /** 经历时间 **/
            long undergoTime = 0;
            long inTime = System.currentTimeMillis();
            while (response == null) {
                log.debug("获取方法已经历:{}" , undergoTime);
                if (undergoTime >= timeout) {
                    /** 等待超时,退出等待 **/
                    log.debug("等待超时,退出等待");
                    break;
                }
                try {
                    log.debug("没有返回参数,开始wait");
                    this.wait(timeout - undergoTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long outTime = System.currentTimeMillis();
                undergoTime = outTime-inTime;
            }
            return response;
        }
    }

    public void setResponse(Object response) {
        synchronized (this) {
            log.debug("放入结果,唤醒所有线程");
            this.response = response;
            this.notifyAll();
        }
    }
}