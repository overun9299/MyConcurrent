package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangPY on 2021/1/21
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test5_使用锁超时解决哲学家就餐问题
 */
@Slf4j(topic = "s.Test5_使用锁超时解决哲学家就餐问题")
public class Test5_使用锁超时解决哲学家就餐问题 {

    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();
        ReentrantLock lock3 = new ReentrantLock();
        ReentrantLock lock4 = new ReentrantLock();
        ReentrantLock lock5 = new ReentrantLock();

        /** 创建哲学家 **/
        new Philosophers("苏格拉底", lock1, lock2).start();
        new Philosophers("柏拉图", lock2, lock3).start();
        new Philosophers("亚里士多德", lock3, lock4).start();
        new Philosophers("赫拉克利特", lock4, lock5).start();
        new Philosophers("阿基米德", lock5, lock1).start();
    }

}

@Slf4j(topic = "s.Philosopher")
class Philosophers extends Thread{

    private ReentrantLock left;
    private ReentrantLock right;

    public Philosophers(String name, ReentrantLock left, ReentrantLock right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                if (left.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        if (right.tryLock(1, TimeUnit.SECONDS)) {
                            try {
                                eat();
                            } finally {
                                right.unlock();
                            }
                        }
                    } finally {
                        left.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void eat() {
        log.debug("哲学家: {} 吃饭" , this.getName());
    }
}



