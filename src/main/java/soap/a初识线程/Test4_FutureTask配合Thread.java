package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/7
 */
@Slf4j(topic = "s.Test4_FutureTask配合Thread")
public class Test4_FutureTask配合Thread {


    /**
     *        FutureTask 能够接收 Callable 类型的参数，用来处理有返回结果的情况
     *
     *
     */



    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<Integer> futureTask = new FutureTask<Integer>(()->{
            log.debug("running...");
            return 1;
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        /** 主线程阻塞，同步等待 task 执行完毕的结果 **/
        Integer integer = futureTask.get();

        log.debug("running...  {}",integer);
    }




}
