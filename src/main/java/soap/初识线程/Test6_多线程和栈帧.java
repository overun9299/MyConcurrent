package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 多线程和栈帧
 */
@Slf4j(topic = "s.Test6_多线程和栈帧")
public class Test6_多线程和栈帧 {

    /**
     *   我们都知道 JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？其实就是线程，每个线程启动后，虚拟机就会为其分配一块栈内存。
     *     -每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
     *     -每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法
     *
     *     debug断点的方式选择thread 就可以切换线程
     */


    public static void main(String[] args) {
        new Thread(()->{
            method1();
        },"t1").start();

        method1();
    }

    public static Object method1() {
        Object o = method2();
        return o;
    }

    public static Object method2() {
        return 2;
    }

}
