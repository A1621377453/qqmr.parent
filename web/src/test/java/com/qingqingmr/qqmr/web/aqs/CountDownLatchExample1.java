package com.qingqingmr.qqmr.web.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 测试CountDownLatch的await方法等待超时，并研究await是在什么时候开始等待的
 *
 * @author crn
 * @datetime 2018-08-29 10:47:36
 */
@Slf4j
public class CountDownLatchExample1 {

    private final static int threadCount = 200;

    //private static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch cdl = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            int countNum = i;
            Thread.sleep(10);
            exec.execute(() -> {
                try {
                    test1(countNum);
                } catch (Exception e) {
                    log.info("Exception");
                } finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await(10,TimeUnit.MILLISECONDS);
        log.info("finish");
        exec.shutdown();
    }

    private static void test1(int countNum) throws InterruptedException {

        log.info("i = " + countNum);
    }
}
