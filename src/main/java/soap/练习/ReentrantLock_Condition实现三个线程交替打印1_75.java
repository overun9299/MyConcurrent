package soap.练习;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangPY on 2021/2/23
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: ReentrantLock_Condition实现三个线程交替打印1_75
 */
@Slf4j(topic = "s.实现三个线程交替打印1_75")
public class ReentrantLock_Condition实现三个线程交替打印1_75 {

    public static void main(String[] args) {
        Lock locks = new ReentrantLock();
        Condition condition = locks.newCondition();
        AlternatePrint alternatePrint = new AlternatePrint(1, 25, locks, condition);

        Thread t1 = new Thread(() -> {
            alternatePrint.print(1, 1, 2);
        }, "t1");
        Thread t2 = new Thread(() -> {
            alternatePrint.print(2, 2, 3);
        }, "t2");
        Thread t3 = new Thread(() -> {
            alternatePrint.print(3, 3, 1);
        }, "t3");

        /** 启动线程 **/

        t3.start();
        t1.start();
        t2.start();
    }


}


@Slf4j(topic = "s.AlternatePrint")
class AlternatePrint {

    private int printIndex;
    private int times;
    private Lock locks;
    private Condition condition;


    public void print(int iam , int printInfo , int next) {
        for (int i = 0; i < times; i++) {
            try {
                locks.lock();

                while (iam != printIndex) {
                    /** 睡眠 **/
                    condition.await();
                }

                log.info(String.valueOf(i*3+printInfo));
                /** 指定下一个坐标 **/
                printIndex = next;
                /** 唤醒所有线程 **/
                condition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                locks.unlock();
            }
        }
    }

    public AlternatePrint(int printIndex, int times, Lock locks, Condition condition) {
        this.printIndex = printIndex;
        this.times = times;
        this.locks = locks;
        this.condition = condition;
    }
}