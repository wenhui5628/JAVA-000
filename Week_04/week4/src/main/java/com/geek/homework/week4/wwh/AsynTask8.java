package com.geek.homework.week4.wwh;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 方法一：使用wait和notify方法调起一个异步线程
 */
public class AsynTask8 {
    public static void main(String[] args) {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start = System.currentTimeMillis();
        final Task8 task8 = new Task8();

        //启动一个新线程，并指定此线程执行Task的sum方法
        Thread t1 = new Thread(() -> {
            task8.sum();

        }, "t1");
        t1.start();

        try {
            //获取任务的执行结果
            int result = task8.getResult();
            System.out.println("异步计算结果为：" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long retrievalTime = System.currentTimeMillis() - start;

        System.out.println("返回结果消耗时间：" + retrievalTime + "毫秒");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }
}

class Task8 {
    private volatile Integer result = null;

    public synchronized int sum() {
        System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
        result = fibo(36);
        notifyAll();    //计算完毕，唤醒正在等待的所有线程，可以取值
        System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
        return result;
    }

    private int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    public synchronized int getResult() throws InterruptedException {
        while (result == null) {
            wait();   //当result的值为null时，表示未进行计算，进入等待
        }
        return result;
    }
}