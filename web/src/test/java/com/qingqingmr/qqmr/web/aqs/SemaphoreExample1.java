package com.qingqingmr.qqmr.web.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 测试Semaphore使用
 *
 * @author crn
 * @datetime 2018-08-29 10:47:36
 */
@Slf4j
public class SemaphoreExample1 {

    private final static int threadCount = 20;


    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < threadCount; i++) {
            int countNum = i;
            exec.execute(() -> {
                try {
                    if(semaphore.tryAcquire()){// 表示只在某一时刻获取到的许可的才可以执行，否则不执行
                        test1(countNum);
                        semaphore.release();
                    }
                } catch (Exception e) {
                    log.info("Exception");
                }
            });
        }
        log.info("finish");
        exec.shutdown();
    }

    private static void test1(int countNum) throws InterruptedException {
        log.info("i = " + countNum);
        Thread.sleep(1500);

    }
}
