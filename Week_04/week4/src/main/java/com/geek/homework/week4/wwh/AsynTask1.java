package com.geek.homework.week4.wwh;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用FutureTask类和Thread类结合调起一个异步线程
 */
public class AsynTask1 {
    
    public static void main(String[] args) {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start=System.currentTimeMillis();
        // 1、创建一个futureTask对象，并将实现了Callable接口的Task对象通过构造器注入到futureTask对象中
        FutureTask<Integer> futureTask = new FutureTask<>(new Task1());

        //2、通过Thread类异步调起一个线程，并通过start方法调用task对象中的call方法执行计算
        new Thread(futureTask).start();

        //3、获取异步调起的线程的返回值
        int result = 0;
        try {
            result = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // 4、输出异步线程返回的结果
        System.out.println("异步计算结果为："+result);
         
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }
}
    class Task1 implements Callable {

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

