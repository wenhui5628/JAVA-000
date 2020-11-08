package com.geek.homework.week4.wwh;

import com.geek.homework.week4.wwh.task.Task;
import com.geek.homework.week4.wwh.task.Task2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AsynTask5 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Task2 task = new Task2(1,countDownLatch);
        FutureTask<Integer> futureTask = new FutureTask<>(task);
        new Thread(futureTask).start();
        //获取异步调起的线程的返回值
        int result = 0;
        try {
            result = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        countDownLatch.await();
        System.out.println("异步计算结果为："+result);
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }
    

}