package com.qingqingmr.qqmr.web.aqs;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

/**
 * @author crn
 * @datetime 2018-08-30 15:55:28
 */
public class ForkJoinTest {
    @Test
    public void test() {
        long start = System.currentTimeMillis();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Long sum = forkJoinPool.invoke(new ForkJoinCalculator(1, 1000000000L));
        System.out.println(sum);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void test1() {
        long start = System.currentTimeMillis();

        long sum = 0;
        for (long i = 1; i <= 1000000000L; i++) {
            sum += i;
        }
        System.out.println(sum);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
