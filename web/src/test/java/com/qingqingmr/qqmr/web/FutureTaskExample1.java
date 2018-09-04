package com.qingqingmr.qqmr.web;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author crn
 * @datetime 2018-08-30 15:26:32
 */
@Slf4j
public class FutureTaskExample1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("new Thread in running");
                Thread.sleep(5000);
                return "Done";
            }
        });
        new Thread(task).start();
        log.info("main in running");
        Thread.sleep(1000);
        String s = task.get();
        log.info("result is "+s);
    }
}
