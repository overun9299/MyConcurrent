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
        log.debug("info: {}" , s);

        Thread.sleep(5000);
        Dog dog2 = new Dog();
        String s2 = ClassLayout.parseInstance(dog2).toPrintable();
        log.debug("info: {}" , s2);

        synchronized (dog2) {
            Thread.sleep(5000);
            String s3 = ClassLayout.parseInstance(dog2).toPrintable();
            log.debug("info: {}" , s3);
        }
    }
}


class Dog {

}
