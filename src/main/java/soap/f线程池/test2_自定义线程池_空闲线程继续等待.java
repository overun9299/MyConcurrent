package soap.f线程池;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;

/**
 * Created by ZhangPY on 2021/2/7
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: test2_自定义线程池_空闲线程继续等待
 */
public class test2_自定义线程池_空闲线程继续等待 {

    /**
     *  实现线程池-空闲线程继续等待
     *    1) 该模式在任务进来就会创建一个线程知道线程数量到达核心线程数容量
     *    2) 超过的任务数量会放入阻塞队列等待执行
     *    3) 再所有任务都执行完毕时,原来创建的线程会阻塞等待新的任务到来
     *
     * **/

    public static void main(String[] args) {
        ThreadPoolTwo threadPool = new ThreadPoolTwo(2, 5);
        for (int i = 0 ; i<20; i++) {
            int j = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("这是任务:"+j);
                }
            };
            threadPool.execute(runnable);
        }


    }

}


@Slf4j(topic = "s.ThreadPool")
class ThreadPoolTwo {

    /** 任务队列 **/
    private BlockingQueueTwo<Runnable> taskQueue;

    private HashSet<Worker> workers = new HashSet<>();

    /** 核心线程数 **/
    private int coreSize;




    public ThreadPoolTwo(int coreSize, int capacity) {
        this.coreSize = coreSize;
        this.taskQueue = new BlockingQueueTwo<>(capacity);
    }

    /** 执行器 **/
    public void execute(Runnable task) {

        /** workers共享资源保证线程安全 **/
        synchronized (workers) {
            /** 1.当任务数没有超过核心数,直接执行 **/
            /** 2.当任务数超过核心数,加入任务队列 **/
            if (workers.size() < coreSize) {
                log.info("线程池直接执行");
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start();
            } else {
                log.info("放入阻塞队列");
                taskQueue.put(task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            /** 1.有空闲核心线程数-直接执行 **/
            /** 2.执行完毕后去任务队列中拿去执行 **/

            /** 使用take 创建出来的线程会一直保活阻塞 **/
            while (task != null || (task = taskQueue.take()) != null) {

                log.info("执行任务");
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.info("任务执行完毕、并无等待任务,移除worker");
                workers.remove(this);
            }
        }
    }


}


@Slf4j(topic = "s.BlockingQueue")
class BlockingQueueTwo<T> {

    /** 任务队列 **/
    private Deque<T> queue = new ArrayDeque<>();

    /** 锁 **/
    private ReentrantLock lock = new ReentrantLock();

    /** 生产者条件变量 **/
    private Condition producerWait = lock.newCondition();

    /** 消费者件变量 **/
    private Condition consumerWait = lock.newCondition();

    /** 容量 **/
    private int capacity;

    public BlockingQueueTwo(int capacity) {
        this.capacity = capacity;
    }



    /** 获取 **/
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    consumerWait.await();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            T t = queue.removeFirst();
            producerWait.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }


    /** 放入 - 死等 **/
    public void put(T t) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    producerWait.await();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            queue.addLast(t);
            consumerWait.signal();
        } finally {
            lock.unlock();
        }
    }

}