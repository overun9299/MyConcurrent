package soap.初识线程;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/1/8
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test11_sleep应用防止cpu百分之100占用
 */
public class Test11_sleep应用防止cpu百分之100占用 {

    /**
     *   案例-防止cpu100%占用
     *      再有的需求中需要有一个线程一直处于运行状态,所以就有了while(true)的写法,
     *      但是如果不做处理while(true)一直在空转,及其浪费cpu资源
     *   解决:在while(true)中让线程sleep一下就可解决
     *
     */


    /**
     *  sleep 实现
     *
     *  在单核cpu中如果没有sleep cpu的占用为98%左右 ,如果有sleep cpu的占用为3%左右  效果非常明显
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        while (true) {

            Thread.sleep(1);

            System.out.println("运行中" + System.currentTimeMillis());
        }
    }
}
