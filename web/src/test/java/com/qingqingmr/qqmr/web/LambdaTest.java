package com.qingqingmr.qqmr.web;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author crn
 * @datetime 2018-07-19 14:10:14
 */
public class LambdaTest {
    @Test
    public void test1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("当前类LambdaTest的run方法输出："+i);
                }
            }
        }).start();
        new Thread(()->System.out.println("当前类LambdaTest的run方法输出：lambda")).start();
    }
    @Test
    public void test2(){
        List<Integer> number = Arrays.asList(1,2,3);
//        number.forEach(n->System.out.println("当前类LambdaTest的test2方法输出："+n));
//        number.stream().forEach(n->System.out.println("当前类LambdaTest的test2方法输出："+n));
        number.parallelStream().forEach(n->System.out.println("当前类LambdaTest的test2方法输出："+n));
    }
}
