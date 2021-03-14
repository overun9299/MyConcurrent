package soap.f线程池;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZhangPY on 2021/2/7
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: Test5_ForkJoin
 */
@Slf4j(topic = "s.Test5_ForkJoin")
public class Test5_ForkJoin {

    public static void main(String[] args) {

        Integer t1 = new Integer(1);
        Integer t2 = new Integer(1);
        System.out.println(t1 == t2); // false

        Integer t3 = 126;
        Integer t4 = 126;
        int t5 = 126;
        System.out.println(t3 == t4); // true
        System.out.println(t3 == t5); // true t3自动拆箱相当于两个int比较

        Integer t6 = 129;
        Integer t7 = 129;
        int t8 = 129;
        System.out.println(t6 == t7); // false 超过127相当于两个new Integer
        System.out.println(t7 == t8); // true  t7自动拆箱相当于两个int比较
    }



}


