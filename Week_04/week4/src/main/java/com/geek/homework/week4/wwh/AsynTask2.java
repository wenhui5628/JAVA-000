package com.geek.homework.week4.wwh;

import com.geek.homework.week4.wwh.task.Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 方法一：使用FutureTask类和Executors类的newCachedThreadPool方法结合调起一个异步线程
 */
public class AsynTask2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start=System.currentTimeMillis();

        //1、通过Executors工具类的newCachedThreadPool方法创建一个可缓存的线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        //2、构造一个FutureTask，并将实现了Callable接口的Task对象通过构造器注入到futureTask对象中
        FutureTask futureTask = new FutureTask(new Task());

        //3、通过ExecutorService的submit方法通过FutureTask对象，最终调用Task对象的call方法
        executor.submit(futureTask);

        //4、获取Task的call方法的返回值
        System.out.println("异步计算结果为："+futureTask.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //5、关闭线程池
        executor.shutdown();

        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }

}
