package com.geek.homework.week4.wwh.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Task3 {

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

    //异步获取数字
    public Future<Integer> getPriceAsync(){
        CompletableFuture<Integer> future =  new CompletableFuture<>();
        new Thread(() -> {
            int price = sum();
            future.complete(price);
        }).start();
        return future;
    }
}
