package com.geek.homework.week4.wwh.task;

import java.util.concurrent.Callable;

public class Task implements Callable {

    @Override
    public Integer call() throws Exception {
        return sum();
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
