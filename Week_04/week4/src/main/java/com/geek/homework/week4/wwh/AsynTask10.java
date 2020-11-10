package com.geek.homework.week4.wwh;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用 CyclicBarrier方式调起一个异步线程
 */
public class AsynTask10 {
    public static void main(String[] args){
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System.currentTimeMillis();

        Task10 task10 = new Task10();
        //构造一个初始值为1的CyclicBarrier对象，并指定计数器的值为0时的回调函数为Task的getValue方法
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
            @Override
            public void run() {
                System.out.println("异步计算结束，结果为：" +task10.getValue());   //打印计算完毕的结果
            }
        });
        task10.setCyclicBarrier(cyclicBarrier);     //将cyclicBarrier复制给Task对象，让两者关联起来
        new Thread(task10).start(); //调起一个新的线程，执行task的run方法

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }
}

class Task10 implements Runnable{
    private CyclicBarrier barrier;

    public void setCyclicBarrier(CyclicBarrier barrier){
        this.barrier = barrier;
    }

    private volatile Integer result = null;

    public void sum(){
        System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
        result = fibo(36);
        System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
        try {
            barrier.await();    //通过这个方法，对计数器的值减1，当计数器的值为0时，会自动回调指定的回调函数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    public int getValue(){
        return result;
    }

    @Override
    public void run() {
        //这里加上synchronized关键字，保证同一时间只有一个线程在进行此计算
        synchronized (this){
            sum();
        }
    }
}