package com.geek.homework.week4.wwh;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用CompletableFuture的supplyAsync方法调起一个异步线程
 */
public class AsynTask7 {
    public static void main(String[] args){
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System. currentTimeMillis();

        //通过CompletableFuture对象的supplyAsync方法直接调起一个异步线程并获取返回结果,在异步调起的操作中，指定执行Task中的sum方法
        Future<Integer> future  = CompletableFuture.supplyAsync(() -> new Task7().sum());

        long retrievalTime = System. currentTimeMillis() - start;

        //打印返回结果
        try {
            System.out.println("异步计算结果为："+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println( "返回结果消耗时间：" + retrievalTime +  "毫秒");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }

}

class Task7 {

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