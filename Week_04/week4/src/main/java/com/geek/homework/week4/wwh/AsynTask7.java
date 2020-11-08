package com.geek.homework.week4.wwh;

import com.geek.homework.week4.wwh.task.Task3;
import com.geek.homework.week4.wwh.task.Task4;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynTask7 {
    public static void main(String[] args){
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System. currentTimeMillis();

        Future<Integer> future  = CompletableFuture.supplyAsync(() -> new Task4().sum());

        long retrievalTime = System. currentTimeMillis() - start;
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