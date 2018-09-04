package com.qingqingmr.qqmr.web;

import java.util.concurrent.CountDownLatch;

/**
 * @author crn
 * @datetime 2018-07-21 09:59:01
 */
public class TreadTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                    System.out.println(countDownLatch.getCount());
                }
            }).start();
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("创建线程执行时间：" + (endTime - startTime) + "ms");
    }
}
