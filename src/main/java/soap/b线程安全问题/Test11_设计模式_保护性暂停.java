package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/13
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test11_设计模式_保护性暂停
 */
@Slf4j(topic = "s.Test11_设计模式_保护性暂停")
public class Test11_设计模式_保护性暂停 {



}

@Slf4j(topic = "s.GuardedObject")
class GuardedObject {
    /** 返回结果 **/
    private Object response;

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

    public void setResponse(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}