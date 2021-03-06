package com.geek.homework.week4.wwh;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用FutureTask类和Executors类的newCachedThreadPool方法结合调起一个异步线程
 */
public class AsynTask2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start=System.currentTimeMillis();

        //1、通过Executors工具类的newCachedThreadPool方法创建一个可缓存的线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        //2、构造一个FutureTask，并将实现了Callable接口的Task对象通过构造器注入到futureTask对象中
        FutureTask futureTask = new FutureTask(new Task2());

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

    class Task2 implements Callable {

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


