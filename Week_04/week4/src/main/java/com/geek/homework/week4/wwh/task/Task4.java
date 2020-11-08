package com.geek.homework.week4.wwh.task;

public class Task4 {

    public int sum() {
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
