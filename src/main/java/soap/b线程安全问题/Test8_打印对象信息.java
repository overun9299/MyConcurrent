package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/12
 */
@Slf4j(topic = "s.Test8_打印对象信息")
public class Test8_打印对象信息 {

    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        String s = ClassLayout.parseInstance(dog).toPrintable();

        /** 此时打印的信息为  01 00 00 00 (00000001 ...   后三位001中后三位的第一个0表示:不是偏向锁 最后两位表示:无锁. 所以刚创建出来的对象是非偏向无锁 **/
        log.debug(s);

        /** jdk1.6及以后默认是采用延迟加载偏向锁，这个延迟时间大概是4s **/
//        Thread.sleep(5000);
        Dog dog2 = new Dog();
        String s2 = ClassLayout.parseInstance(dog2).toPrintable();
        /** 此时打印的信息为  05 00 00 00 (00000101 ...  101的第一个1表示:是偏向锁 最后两位表示:无锁 所以4s后创建出来的对象是无锁可偏向状态 **/
        log.debug(s2);

        /** 调用hashCode会将无锁可偏向状态 -> 非偏向无锁 **/
//        dog2.hashCode();

        Thread thread = new Thread(() -> {
            dog2.getInfo();
        });
        thread.start();
        thread.join();

        Thread.sleep(1000);
        dog2.getInfo();


    }
}

@Slf4j(topic = "s.Dog")
class Dog {

    public synchronized void getInfo() {
        /** 如果调用hashcode就会变成010  及重量级锁 **/
        this.hashCode();

        String s3 = ClassLayout.parseInstance(this).toPrintable();
        /**  如果不调用 hashcode此时打印的信息为  10000000 ...  000的第一个0表示:是非偏向锁 最后两位表示:轻量级  **/
        log.debug(s3);
    }
}
