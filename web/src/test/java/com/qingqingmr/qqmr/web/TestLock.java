package com.qingqingmr.qqmr.web;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author crn
 * @datetime 2018-08-22 14:33:43
 */
public class TestLock {
    public static void main(String[] args){
        Ticket ticket = new Ticket();

        new Thread(ticket,"1号窗口").start();
        new Thread(ticket,"2号窗口").start();
        new Thread(ticket,"3号窗口").start();
        new Thread(ticket,"4号窗口").start();
    }
}

class Ticket implements Runnable{

    private int tick = 100;

    private Lock lock = new ReentrantLock();

    public void run(){
        while(true){
            lock.lock();
            if(tick > 0){
                try{
                    Thread.sleep(200);
                    System.out.println(Thread.currentThread().getName()+"完成售票,余票为: "+ --tick);
                }catch(InterruptedException e){

                }finally {
                    lock.unlock();
                }

            }
        }
    }
}
