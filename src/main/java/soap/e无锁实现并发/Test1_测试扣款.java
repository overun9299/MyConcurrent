package soap.e无锁实现并发;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ZhangPY on 2021/2/2
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test1_测试扣款
 */
@Slf4j(topic = "s.Test1_测试扣款")
public class Test1_测试扣款 {

    public static void main(String[] args) {

        Account accountUnsafe = new AccountUnsafe(10000);
        Account accountSafeButHeavy = new AccountSafeButHeavy(10000);
        Account accountCAS = new AccountCAS(10000);
        Account.demo(accountUnsafe);
        Account.demo(accountSafeButHeavy);
        Account.demo(accountCAS);
    }
}


/**
 * 扣款方式-1 不加锁非线程安全
 */
@Slf4j(topic = "s.AccountUnsafe")
class AccountUnsafe implements Account {

    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {
        this.balance -= amount;
    }
}

/**
 * 扣款方式-2 使用原始加锁方式
 */
@Slf4j(topic = "s.AccountUnsafe")
class AccountSafeButHeavy implements Account {

    private Integer balance;

    public AccountSafeButHeavy(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}


/**
 * 扣款方式-3 使用jdk自带原子Integer
 */
class AccountCAS implements Account {
    private AtomicInteger balance;

    public AccountCAS(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return this.balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true) {
            /** 获取当前最新的值 **/
            int prev = this.balance.get();
            /** 要修改的值 **/
            int next = this.balance.get() - amount ;
            /** 真正的修改 **/
            if (balance.compareAndSet(prev , next)) {
                break;
            }
        }

//        this.balance.getAndAdd(-amount);
    }
}

interface Account {
    // 获取余额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance()
                + " cost: " + (end-start)/1000_000 + " ms  " + account.getClass().getName());
    }
}
