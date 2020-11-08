package com.geek.homework.week4.wwh;

import com.geek.homework.week4.wwh.task.Task3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynTask6 {
    public static void main(String[] args){
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System. currentTimeMillis();
        Future<Integer> future = new Task3().getPriceAsync();
        long invocationTime = System. currentTimeMillis() - start;
        System.out.println( "调用接口时间：" + invocationTime +  "毫秒");
        int result = 0;
        try {
            result = future.get();
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }  catch (ExecutionException e) {
            e.printStackTrace();
        }

        long retrievalTime = System. currentTimeMillis() - start;
        System.out.println("异步计算结果为："+result);
        System.out.println( "返回结果消耗时间：" + retrievalTime +  "毫秒");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }

}