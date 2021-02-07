package soap.f线程池;

import javafx.concurrent.Worker;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangPY on 2021/2/6
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: test1_自定义线程池
 */
@Slf4j(topic = "s.test1_自定义线程池")
public class test1_自定义线程池 {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(5, 3, TimeUnit.SECONDS, 5);
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
class ThreadPool {

    /** 任务队列 **/
    private BlockingQueue<Runnable> taskQueue;

    private HashSet<Worker> workers = new HashSet<>();

    /** 核心线程数 **/
    private int coreSize;

    /** 任务超时时间 **/
    private long timeOut;

    private TimeUnit timeUnit;



    public ThreadPool(int coreSize, long timeOut, TimeUnit timeUnit, int capacity) {
        this.coreSize = coreSize;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(capacity);
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
//            while (task != null || (task = taskQueue.take()) != null) {


            /** 使用takeWithTimeOut 如果没有任务线程就会结束运行 **/
            while (task != null || (task = taskQueue.takeWithTimeOut(timeOut , timeUnit)) != null) {
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
class BlockingQueue<T> {

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

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /** 带超时获取 **/
    public T takeWithTimeOut(long timeOut , TimeUnit unit) {
        lock.lock();
        long nanos = unit.toNanos(timeOut);
        try {
            while (queue.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = consumerWait.awaitNanos(nanos);

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

    /** 放入 带超时时间 **/
    public boolean putWithTimeOut(T t , long timeOut , TimeUnit unit) {
        lock.lock();
        long nanos = unit.toNanos(timeOut);
        try {
            while (queue.size() == capacity) {
                try {
                    if (nanos < 0) {
                        /** 执行拒绝策略 **/

                        return false;
                    }
                    nanos = producerWait.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            queue.addLast(t);
            consumerWait.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }
}
