package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/29
 */
@Slf4j(topic = "s.Test8_设计模式_交替打印")
public class Test8_设计模式_交替打印 {

    public static void main(String[] args) {
        waitNotify waitNotify = new waitNotify(1, 6);

        Thread t1 = new Thread(() -> {
            waitNotify.print(1 , "a" ,2);
        }, "t1");
        Thread t2 = new Thread(() -> {
            waitNotify.print(2 , "b" ,3);
        }, "t2");
        Thread t3 = new Thread(() -> {
            waitNotify.print(3 , "c" ,1);
        }, "t3");

        t1.start();
        t2.start();
        t3.start();
    }

}

class waitNotify {
    private int index;
    private int printTimes;

    public void print(int myIndex , String printS,int nextIndex) {
        for (int i = 0 ; i< printTimes ;i++) {
            synchronized (this) {
                while (!(myIndex == index)) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(printS);
                index = nextIndex;
                this.notifyAll();
            }
        }
    }

    public waitNotify(int index, int printTimes) {
        this.index = index;
        this.printTimes = printTimes;
    }
}