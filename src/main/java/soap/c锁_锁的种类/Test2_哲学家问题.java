package soap.c锁_锁的种类;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/20
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test2_哲学家问题
 */
public class Test2_哲学家问题 {

    public static void main(String[] args) {
        /** 创建筷子对象 **/
        Chopsticks c1 = new Chopsticks("1");
        Chopsticks c2 = new Chopsticks("2");
        Chopsticks c3 = new Chopsticks("3");
        Chopsticks c4 = new Chopsticks("4");
        Chopsticks c5 = new Chopsticks("5");

        /** 创建哲学家 **/
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();

    }

}

@Slf4j(topic = "s.Philosopher")
class Philosopher extends Thread{
    private Chopsticks left;
    private Chopsticks right;

    public Philosopher(String name, Chopsticks left, Chopsticks right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (left) {
                synchronized (right) {
                    eat(this.getName());
                }
            }
        }
    }

    private void eat(String name) {
        log.debug("{} 开始吃饭..." , name);
    }
}


@Slf4j(topic = "s.chopsticks")
class Chopsticks {
    private String name;

    public Chopsticks(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "chopsticks{" +
                "筷子名称='" + name + '\'' +
                '}';
    }
}
