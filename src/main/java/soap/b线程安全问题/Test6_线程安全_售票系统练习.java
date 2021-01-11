package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/11
 */
@Slf4j(topic = "s.Test6_线程安全_售票系统练习")
public class Test6_线程安全_售票系统练习 {

    public static void main(String[] args) throws InterruptedException {
        /** 创建票房 **/
        Room room = new Room(1000);
        /** 成功买到的票数、线程安全 **/
        AtomicInteger count = new AtomicInteger(0);
        /** 记录线程 **/
        List<Thread> threadList = new ArrayList<>();

        /** 10000个人买票 **/
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> {
                int ticketed = room.sellTicket(new Double(Math.random() * 10).intValue());
                count.addAndGet(ticketed);
            });
            threadList.add(thread);
            thread.start();
        }

        /** 等待线程执行完毕 **/
        for (Thread thread : threadList) {
            thread.join();
        }
        /** 打印出结果 **/
        log.debug("购买到的票数 -- {}" , count.get());
        log.debug("剩余的票数 -- {}" , room.getSurplus());
    }
}


class Room {

    private int ticket;

    public Room(int ticket) {
        this.ticket = ticket;
    }

    public int getSurplus() {
        return ticket;
    }

    public synchronized int sellTicket(int count) {
        /** 判断剩余票数够不够 **/
        if (count <= ticket) {
            ticket=ticket - count;
            return count;
        } else {
            return 0;
        }
    }
}
