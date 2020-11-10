package com.geek.homework.week4.wwh;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
 * <p>
 * 使用Lock结合Condition方式调起异步线程
 */
public class AsynTask11 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
        long start=System.currentTimeMillis();
        final Task11 task11 = new Task11();
        Thread thread = new Thread(() -> {
            task11.sum(36);
        });
        thread.start();

        int result = task11.getValue(); //这是得到的返回值

        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
    }

}

class Task11 {
    private volatile Integer value = null;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void sum(int num) {
        System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
        lock.lock();
        try {
            value = fibo(num);
            condition.signal(); //给等待线程发送唤醒信号，类似notify操作，此处表示已经计算完成，唤醒其他线程可以获取值
        } finally {
            lock.unlock();
        }
        System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    public int getValue() throws InterruptedException {
        lock.lock();
        try {
            while (value == null) {
                condition.await();  //等待，类似Object的wait操作
            }
        } finally {
            lock.unlock();
        }
        return value;
    }
}
