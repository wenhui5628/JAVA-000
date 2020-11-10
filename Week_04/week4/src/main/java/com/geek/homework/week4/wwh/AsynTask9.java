package com.geek.homework.week4.wwh;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用 Join 方式调起一个异步线程
 */
public class AsynTask9 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System.currentTimeMillis();

        Task9 task = new Task9();
        //启动新线程，进行计算
        Thread thread = new Thread(() -> {
            task.value.set(task.sum());
        });
        thread.start();

        thread.join();  //等待线程执行完毕

        int result = task.value.get(); //得到返回值

        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }
}

class Task9 {
    public AtomicInteger value = new AtomicInteger();   //使用并发原子类保证操作的原子性

    public int sum() {
        System.out.println("==="+Thread.currentThread().getName()+"线程正在执行计算");
        int result =  fibo(36);
        System.out.println("==="+Thread.currentThread().getName()+"线程完成计算");
        return result;
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }
}




