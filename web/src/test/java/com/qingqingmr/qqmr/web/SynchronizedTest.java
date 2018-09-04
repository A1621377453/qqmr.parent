package com.qingqingmr.qqmr.web;

/**
 * @author crn
 * @datetime 2018-08-21 17:20:41
 */
public class SynchronizedTest {
    public synchronized void test1() {

    }

    public void test2() {
        synchronized (this) {

        }
    }
}
