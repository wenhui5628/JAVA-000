package com.geek.homework.week4.wwh;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 方法一：使用CountDownLatch和ExecutorService结合调起一个异步线程
 */
public class AsynTask5 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        CountDownLatch latch = new CountDownLatch(1);  //定义一个初始值为1的计数器
        //初始化线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        //构造将要执行的任务对象，并将计数器传入此对象
        Task5 task5 = new Task5(latch);
        //通过线程池异步调起任务
        executor.execute(()->{
            task5.sum();
        });
        //实现对计数器等于 0 的等待，当计数器的值为0时，就可以进行后面的操作
        latch.await();
        //获取任务的执行结果
        System.out.println("异步计算结果为："+task5.getValue());
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
        executor.shutdown();
    }
}

    class Task5 {
        private CountDownLatch latch;   //计数器

        public Task5(CountDownLatch latch){
            this.latch = latch;
        }

        private volatile Integer result = null;

        public int sum() {
            System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
            result = fibo(36);
            System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
            latch.countDown();  //计算完毕后，对计数器的值减1
            return result;
        }

        private int fibo(int a) {
            if (a < 2)
                return 1;
            return fibo(a - 1) + fibo(a - 2);
        }

        public int getValue(){
            return result;
        }
    }