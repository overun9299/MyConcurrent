package soap.b线程安全问题;

/**
 * Created by ZhangPY on 2021/1/10
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test3_synchronized到底锁住了什么
 */
public class Test3_synchronized到底锁住了什么 {

    /**
     * synchronized加在静态方法,锁的是这个类
     */
    public synchronized static void methodA() {

    }
    /** 等价 **/
    public static void methodAPlus() {
        synchronized (Test3_synchronized到底锁住了什么.class) {

        }
    }


    /**
     * synchronized加在非static方法上(成员方法),锁的是this
     */
    public synchronized void methodB() {

    }
    /** 等价 **/
    public void methodBPlus() {
        synchronized (this) {

        }
    }


}
