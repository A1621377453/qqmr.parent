package com.qingqingmr.qqmr.web.aqs;

import java.util.concurrent.RecursiveTask;

/**
 * @author crn
 * @datetime 2018-08-30 15:50:49
 */
public class ForkJoinCalculator extends RecursiveTask<Long> {

    private long start;
    private long end;
    private static final long THRESHOLD = 1000;// 临界值

    public ForkJoinCalculator(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        if (end - start <= THRESHOLD) {
            // 不大于临界值直接计算和
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            // 大于临界值继续进行拆分子任务
            long mid = (start + end) / 2;

            // 拆分子任务
            ForkJoinCalculator calculator1 = new ForkJoinCalculator(start, mid);
            calculator1.fork();

            // 拆分子任务
            ForkJoinCalculator calculator2 = new ForkJoinCalculator(mid + 1, end);
            calculator2.fork();

            //合并子任务结果
            return calculator1.join() + calculator2.join();
        }
    }
}
