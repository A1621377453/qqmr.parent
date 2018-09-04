package com.qingqingmr.qqmr.web;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author crn
 * @datetime 2018-08-22 11:18:59
 */
@Slf4j
public class TestCallable {
    public static void main(String[] args) {
        CallableDemo threadDemo = new CallableDemo();
        FutureTask<Integer> vFutureTask = new FutureTask<Integer>((Callable<Integer>) threadDemo);
        new Thread(vFutureTask).start();
        try {
            Integer integer = vFutureTask.get();
            System.out.println(integer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() {
        // 计算 0~100 的和
        int sum = 0;

        for (int i = 0; i <= 100; i++) {
            sum += i;
        }

        return sum;
    }
}


