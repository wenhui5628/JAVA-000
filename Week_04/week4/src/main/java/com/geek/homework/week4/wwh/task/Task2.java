package com.geek.homework.week4.wwh.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class Task2 implements Callable {

    private CountDownLatch latch;

    private int id;

    public Task2(int id,CountDownLatch latch){
        this.id = id;
        this.latch = latch;
    }

    @Override
    public Integer call() throws Exception {
        synchronized (this) {
            int result = sum();
            latch.countDown();
            return result;
        }
    }

    private int sum() {
        System.out.println("==="+Thread.currentThread().getName()+"线程正在执行计算");
        int result =  fibo(36);
        System.out.println("==="+Thread.currentThread().getName()+"线程完成计算");
        return result;
    }

    private int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

}
