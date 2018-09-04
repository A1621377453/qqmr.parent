package com.qingqingmr.qqmr.web;

import java.util.concurrent.CountDownLatch;

/**
 * @author crn
 * @datetime 2018-08-22 10:42:53
 */
public class TestCountDownLatch {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(10);
        LatchDemo ld = new LatchDemo(latch);

        long start = System.currentTimeMillis();

        // 创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(ld).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {

        }

        long end = System.currentTimeMillis();

        System.out.println("耗费时间为:" + (end - start));

    }
}

class LatchDemo implements Runnable {
    private CountDownLatch latch;

    // 有参构造器
    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {

        synchronized (this) {
            try {
                // 打印50000以内的偶数
                for (int i = 0; i < 50000; i++) {
                    if (i % 2 == 0) {
                        System.out.println(i);
                    }
                }
            } finally {
                // 线程数量递减
                latch.countDown();
            }
        }
    }
}
