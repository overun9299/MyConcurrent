package soap.f线程池;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangPY on 2021/2/7
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: test4_自定义线程池_阻塞队列实现拒绝策略
 */
@Slf4j(topic = "s.test4_自定义线程池_阻塞队列实现拒绝策略")
public class test4_自定义线程池_阻塞队列实现拒绝策略 {

    /**
     * 在原来的例子中,阻塞队列满了后,后续的任务放入会一直等待
     *  现在我们要实现拒绝策略
     *
     */
    
    public static void main(String[] args) {
        ThreadPoolFour threadPoolFour = new ThreadPoolFour(1, 3, TimeUnit.SECONDS, 2, ((blockingQueue, task) -> {
            // 1) 死等
//            blockingQueue.put(task);
            // 2) 超时
//            blockingQueue.putWithTimeOut(task,1 , TimeUnit.SECONDS);
            // 3) 让调用者放弃任务等待
//            log.info("放弃任务等待 {}" , task.toString());
            // 4) 让调用者抛出异常
//            throw new RuntimeException("阻塞队列已满");
            // 5) 让调用者自己执行
//            task.run();

        }));
        for (int i = 0 ; i<20; i++) {
            int j = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    System.out.println("这是任务:"+j);
                    log.info("这是任务: {}" , this.toString());
                }
            };
            threadPoolFour.execute(runnable);
        }


    }

}


/**
 * 拒绝策略
 */
interface RejectionStrategy<T> {
    void rejection(BlockingQueueFour<T> blockingQueue, Runnable task);
}


@Slf4j(topic = "s.ThreadPoolFour")
class ThreadPoolFour {

    /** 任务队列 **/
    private BlockingQueueFour<Runnable> taskQueue;

    private HashSet<Worker> workers = new HashSet<>();

    /** 核心线程数 **/
    private int coreSize;

    /** 任务超时时间 **/
    private long timeOut;

    /** 拒绝策略 **/
    private RejectionStrategy<Runnable> rejectionStrategy;

    private TimeUnit timeUnit;



    public ThreadPoolFour(int coreSize, long timeOut, TimeUnit timeUnit, int capacity, RejectionStrategy rejectionStrategy) {
        this.coreSize = coreSize;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueueFour<>(capacity);
        this.rejectionStrategy = rejectionStrategy;
    }

    /** 执行器 **/
    public void execute(Runnable task) {

        /** workers共享资源保证线程安全 **/
        synchronized (workers) {
            /** 1.当任务数没有超过核心数,直接执行 **/
            /** 2.当任务数超过核心数,加入任务队列 **/
            if (workers.size() < coreSize) {
                log.info("线程池直接执行: {}" , task.toString());
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start();
            } else {
                /** 调用拒绝策略 **/
                rejectionStrategy.rejection(taskQueue ,task);

                /**
                 * 1) 死等
                 * 2) 带超时等待
                 * 3) 让调用者放弃任务等待
                 * 4) 让调用者抛出异常
                 * 5) 让调用者自己执行任务
                 * 当然如果在ThreadPool中直接实现拒绝策略,就相当于把代码写死,没有什么扩展性.
                 * 所以我们提供一个拒绝策略接口,让调用者自己实现拒绝策略. 这也是设计模式-策略模式
                 **/
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
            while (task != null || (task = taskQueue.take()) != null) {
                log.info("执行任务: {}" , task.toString());
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


@Slf4j(topic = "s.BlockingQueueFour")
class BlockingQueueFour<T> {

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

    public BlockingQueueFour(int capacity) {
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
                log.info("阻塞线程满-进入等待");
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
                        log.error("放入阻塞队列失败 {}" , t.toString());
                        return false;
                    }
                    nanos = producerWait.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            log.info("放入阻塞队列: {}" , t.toString());
            queue.addLast(t);
            consumerWait.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }
}


