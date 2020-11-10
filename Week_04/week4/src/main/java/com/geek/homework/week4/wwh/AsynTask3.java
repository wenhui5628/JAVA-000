package com.geek.homework.week4.wwh;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 方法一：使用Future<Integer>和Executors类的newCachedThreadPool方法结合调起一个异步线程
 */
public class AsynTask3 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");

        long start=System.currentTimeMillis();
        //1、通过Executors工具类的newCachedThreadPool方法创建一个可缓存的线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        //2、通过ExecutorService的submit方法调用Task对象
        Future<Integer> result1 = executor.submit(new Task3());
        System.out.println("异步计算结果为："+result1.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        executor.shutdown();

        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }
}

class Task3 implements Callable {

    @Override
    public Integer call() throws Exception {
        return sum();
    }

    private int sum() {
        System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
        int result = fibo(36);
        System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
        return result;
    }

    private int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}


