package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/11
 */
@Slf4j(topic = "s.Test7_线程安全_转账问题")
public class Test7_线程安全_转账问题 {

    // Random 为线程安全
    static Random random = new Random();

    // 随机1~amount
    public static int random(int amount) {
        return random.nextInt(amount) + 1;
    }

    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        /** 构造两个账户 **/
        Account accountA = new Account(1000);
        Account accountB = new Account(1000);
        /** 记录线程 **/
        List<Thread> threadList = new ArrayList<>();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                accountA.transferAccounts(accountB , random(100));
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                accountB.transferAccounts(accountA , random(100));
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

//        for (int i = 0; i < 1000; i++) {
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(random(1000));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                accountA.transferAccounts(accountB , random(100));
//                try {
//                    Thread.sleep(random(100));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            threadList.add(thread);
//            thread.start();
//        }
//        for (int i = 0; i < 1000; i++) {
//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(random(300));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                accountB.transferAccounts(accountA , random(100));
//                try {
//                    Thread.sleep(random(1000));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            threadList.add(thread);
//            thread.start();
//        }
//        /** 等待线程执行完毕 **/
//        for (Thread thread : threadList) {
//            thread.join();
//        }

        log.debug("A账户余额为 : {} , B账户余额为 : {} , 总计为 : {}" , accountA.getBalance() , accountB.getBalance() , accountA.getBalance()+accountB.getBalance());
    }

}

class Account {
    private int balance;

    public Account(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    private void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * 转账
     * @param targetAccount 目标账户
     * @param money 金额
     * 注意如果此处在方法上加synchronized是不起作用的,因为在方法上加,是谁调用锁谁,很显然锁的不是同一个对象
     * 所以就要锁住一个公共的对象也就是Account  注意如果锁住Account,效率会非常低
     */
    public void transferAccounts(Account targetAccount , int money) {
        synchronized (Account.class) {
            if (this.getBalance() > money) {
                /** 扣除原账户 **/
                this.setBalance(this.getBalance()-money);
                /** 重置新账户 **/
                targetAccount.setBalance(targetAccount.getBalance()+money);
            }
        }
    }
}
