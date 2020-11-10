package com.geek.homework.week4.wwh;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用CompletableFuture和Future<Integer>结合调起一个异步线程
 */
public class AsynTask6 {
    public static void main(String[] args){
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System.currentTimeMillis();
        //调用任务的异步方法getNumAsync异步获取计算的结果
        Future<Integer> future = new Task6().getNumAsync();
        long invocationTime = System. currentTimeMillis() - start;
        System.out.println( "调用接口时间：" + invocationTime +  "毫秒");
        int result = 0;
        try {
            result = future.get();  //获取异步方法中future的返回值
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }  catch (ExecutionException e) {
            e.printStackTrace();
        }

        long retrievalTime = System. currentTimeMillis() - start;

        //打印执行结果
        System.out.println("异步计算结果为："+result);
        System.out.println( "返回结果消耗时间：" + retrievalTime +  "毫秒");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }

}

class Task6 {

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

    /***
     * 异步获取数字
     * @return
     */
    public Future<Integer> getNumAsync(){
        CompletableFuture<Integer> future =  new CompletableFuture<>(); //构造一个CompletableFuture对象
        //启动一个线程，调用sum方法，并将结果赋值到future对象，从而在别人调getNumAsync方法时，能获取异步调起线程的返回值
        new Thread(() -> {
            int result = sum();
            future.complete(result);
        }).start();
        return future;
    }
}